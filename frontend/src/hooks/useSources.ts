import { useState, useEffect, useCallback } from 'react'
import { getSources } from '../api/client'
import type { SourceConfig } from '../types'

export function useSources() {
  const [data, setData] = useState<SourceConfig[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchSources = useCallback(async () => {
    try {
      const result = await getSources()
      setData(result)
      setError(null)
    } catch {
      setError('Failed to load sources')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchSources()
  }, [fetchSources])

  return { data, loading, error, refetch: fetchSources }
}
