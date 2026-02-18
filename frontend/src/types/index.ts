export type SourceType = 'API' | 'CSV' | 'DB'
export type JobStatus = 'RUNNING' | 'SUCCESS' | 'FAILED' | 'PARTIAL'
export type TriggerType = 'SCHEDULER' | 'MANUAL'

export interface SourceConfig {
  id: number
  name: string
  type: SourceType
  connectionString: string
  // authConfig is never returned by the API — secrets stay server-side.
  // hasAuthConfig indicates whether credentials are configured (for UI lock icon).
  hasAuthConfig: boolean
  scheduleCron: string | null
  lastRunTimestamp: string | null
  isActive: boolean
  createdAt: string
}

export interface JobHistory {
  id: number
  sourceConfigId: number | null
  sourceName: string | null
  jobName: string
  status: JobStatus
  recordsProcessed: number
  errorCount: number
  startTime: string
  endTime: string | null
  triggeredBy: TriggerType
  durationMs: number | null
}

export interface ErrorLogEntry {
  id: number
  rawData: string | null
  errorReason: string
  occurredAt: string
}

export interface JobDetail {
  job: JobHistory
  errors: ErrorLogEntry[]
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  page: number
  size: number
}

export interface UnifiedData {
  id: number
  sourceConfigId: number | null
  externalId: string
  sourceType: SourceType
  sourceName: string | null
  payload: Record<string, unknown>
  ingestedAt: string
  updatedAt: string
}
