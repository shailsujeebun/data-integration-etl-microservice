import { useState, useEffect, useCallback, useRef } from 'react'
import { getJobs } from '../api/client'
import type { JobHistory, PageResponse } from '../types'

const TERMINAL_STATUSES = new Set(['SUCCESS', 'FAILED', 'PARTIAL'])

function hasRunningJob(data: PageResponse<JobHistory> | null): boolean {
  if (!data) return false
  return data.content.some(job => !TERMINAL_STATUSES.has(job.status))
}

/**
 * Fetches paginated job history with smart polling:
 * - Polls every `activePollMs` (default 3s) while any job is RUNNING
 * - Drops to `idlePollMs` (default 30s) when all jobs are in a terminal state
 * - Immediate re-fetch available via `refetch` (used by trigger buttons)
 */
export function useJobs(activePollMs = 3000, idlePollMs = 30000) {
  const [data, setData] = useState<PageResponse<JobHistory> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null)

  const fetchJobs = useCallback(async (): Promise<PageResponse<JobHistory> | null> => {
    try {
      const result = await getJobs()
      setData(result)
      setError(null)
      return result
    } catch {
      setError('Failed to load jobs')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  // Re-schedule the poll interval based on whether any job is currently active
  const schedulePoll = useCallback((currentData: PageResponse<JobHistory> | null) => {
    if (intervalRef.current) clearInterval(intervalRef.current)
    const interval = hasRunningJob(currentData) ? activePollMs : idlePollMs
    intervalRef.current = setInterval(async () => {
      const result = await fetchJobs()
      schedulePoll(result)
    }, interval)
  }, [fetchJobs, activePollMs, idlePollMs])

  useEffect(() => {
    // Initial load, then kick off adaptive polling
    fetchJobs().then(result => schedulePoll(result))
    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current)
    }
  }, [fetchJobs, schedulePoll])

  // Exposed so trigger buttons can force an immediate refresh + reset the poll timer
  const refetch = useCallback(async () => {
    const result = await fetchJobs()
    schedulePoll(result)
  }, [fetchJobs, schedulePoll])

  return { data, loading, error, refetch }
}
