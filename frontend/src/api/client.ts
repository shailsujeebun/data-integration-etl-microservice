import axios from 'axios'
import type { AxiosError, InternalAxiosRequestConfig } from 'axios'
import type {
  SourceConfig,
  JobHistory,
  JobDetail,
  PageResponse,
  UnifiedData,
} from '../types'

const ADMIN_USER_KEY = 'etl_admin_user'
const ADMIN_PASS_KEY = 'etl_admin_pass'
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim() || '/api'

type AdminCredentials = {
  username: string
  password: string
}

type RetriableRequestConfig = InternalAxiosRequestConfig & { _retryWithPrompt?: boolean }

function isWriteMethod(method?: string): boolean {
  if (!method) return false
  const normalized = method.toLowerCase()
  return normalized === 'post' || normalized === 'put' || normalized === 'delete' || normalized === 'patch'
}

function getStoredCredentials(): AdminCredentials | null {
  if (typeof window === 'undefined') return null

  const username = window.localStorage.getItem(ADMIN_USER_KEY)
  const password = window.localStorage.getItem(ADMIN_PASS_KEY)
  if (!username || !password) return null

  return { username, password }
}

function clearStoredCredentials() {
  if (typeof window === 'undefined') return
  window.localStorage.removeItem(ADMIN_USER_KEY)
  window.localStorage.removeItem(ADMIN_PASS_KEY)
}

function promptForCredentials(): AdminCredentials | null {
  if (typeof window === 'undefined') return null

  const username = window.prompt('Admin username required for write actions')?.trim()
  if (!username) return null

  const password = window.prompt('Admin password') ?? ''
  if (!password) return null

  window.localStorage.setItem(ADMIN_USER_KEY, username)
  window.localStorage.setItem(ADMIN_PASS_KEY, password)
  return { username, password }
}

function applyBasicAuth(config: InternalAxiosRequestConfig, credentials: AdminCredentials) {
  const token = btoa(`${credentials.username}:${credentials.password}`)
  config.headers.Authorization = `Basic ${token}`
}

const api = axios.create({
  baseURL: apiBaseUrl,
  timeout: 15_000,
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  if (isWriteMethod(config.method)) {
    const credentials = getStoredCredentials()
    if (credentials) {
      applyBasicAuth(config, credentials)
    }
  }
  return config
})

api.interceptors.response.use(
  response => response,
  async (error: AxiosError) => {
    const config = error.config as RetriableRequestConfig | undefined

    if (error.response?.status === 401 && config?._retryWithPrompt) {
      clearStoredCredentials()
    }

    if (!config || config._retryWithPrompt || error.response?.status !== 401 || !isWriteMethod(config.method)) {
      return Promise.reject(error)
    }

    const credentials = promptForCredentials()
    if (!credentials) {
      return Promise.reject(error)
    }

    config._retryWithPrompt = true
    applyBasicAuth(config, credentials)
    return api.request(config)
  }
)

// --- Sources ---

export const getSources = (): Promise<SourceConfig[]> =>
  api.get<SourceConfig[]>('/sources').then(r => r.data)

export const getSource = (id: number): Promise<SourceConfig> =>
  api.get<SourceConfig>(`/sources/${id}`).then(r => r.data)

export const createSource = (data: Partial<SourceConfig>): Promise<SourceConfig> =>
  api.post<SourceConfig>('/sources', data).then(r => r.data)

export const updateSource = (id: number, data: Partial<SourceConfig>): Promise<SourceConfig> =>
  api.put<SourceConfig>(`/sources/${id}`, data).then(r => r.data)

export const deleteSource = (id: number): Promise<void> =>
  api.delete(`/sources/${id}`).then(() => undefined)

// --- Jobs ---

export const getJobs = (page = 0, size = 20): Promise<PageResponse<JobHistory>> =>
  api.get<PageResponse<JobHistory>>(`/jobs?page=${page}&size=${size}`).then(r => r.data)

export const getJobDetail = (id: number): Promise<JobDetail> =>
  api.get<JobDetail>(`/jobs/${id}`).then(r => r.data)

export const triggerJob = (sourceConfigId: number): Promise<{ message: string; status: string }> =>
  api.post('/jobs/trigger', { sourceConfigId }).then(r => r.data)

// --- Data ---

export const getData = (params: {
  page?: number
  size?: number
  sourceType?: string
  from?: string
  to?: string
}): Promise<PageResponse<UnifiedData>> =>
  api.get<PageResponse<UnifiedData>>('/data', { params }).then(r => r.data)
