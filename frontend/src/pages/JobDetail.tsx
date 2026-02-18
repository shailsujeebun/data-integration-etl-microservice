import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getJobDetail } from '../api/client'
import { StatusBadge } from '../components/shared/StatusBadge'
import { LoadingSpinner } from '../components/shared/LoadingSpinner'
import type { JobDetail } from '../types'

export function JobDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [detail, setDetail] = useState<JobDetail | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) return
    getJobDetail(Number(id))
      .then(setDetail)
      .catch(() => setError('Failed to load job details'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) {
    return (
      <div className="flex items-center justify-center py-24">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (error || !detail) {
    return (
      <div className="space-y-4">
        <button onClick={() => navigate(-1)} className="text-sm text-frost-dim/60 hover:text-frost flex items-center gap-1">
          ← Back
        </button>
        <div className="bg-coral/10 border border-coral/30 rounded-lg px-5 py-4 text-coral text-sm">
          {error ?? 'Job not found'}
        </div>
      </div>
    )
  }

  const { job, errors } = detail

  const metaItems = [
    { label: 'Source', value: job.sourceName ?? '—' },
    { label: 'Status', value: <StatusBadge status={job.status} /> },
    { label: 'Triggered By', value: <span className="font-mono text-sm">{job.triggeredBy}</span> },
    { label: 'Records Processed', value: job.recordsProcessed.toLocaleString() },
    { label: 'Error Count', value: <span className={job.errorCount > 0 ? 'text-coral' : ''}>{job.errorCount}</span> },
    { label: 'Duration', value: job.durationMs != null ? `${job.durationMs}ms` : '—' },
    { label: 'Started', value: new Date(job.startTime).toLocaleString() },
    { label: 'Ended', value: job.endTime ? new Date(job.endTime).toLocaleString() : '—' },
  ]

  return (
    <div className="space-y-8">
      {/* Back + Header */}
      <div>
        <button onClick={() => navigate(-1)} className="text-sm text-frost-dim/60 hover:text-frost flex items-center gap-1 mb-4">
          ← Back to Dashboard
        </button>
        <h1 className="text-2xl font-headline font-bold text-frost">Job Detail</h1>
        <p className="text-sm text-frost-dim/60 font-mono mt-1">{job.jobName}</p>
      </div>

      {/* Meta Grid */}
      <div className="bg-steel/20 border border-steel/40 rounded-xl p-6">
        <h2 className="text-sm font-semibold text-frost-dim/60 uppercase tracking-wider mb-4">Run Summary</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-x-8 gap-y-4">
          {metaItems.map(item => (
            <div key={item.label}>
              <p className="text-xs text-frost-dim/40 uppercase tracking-wider mb-1">{item.label}</p>
              <div className="text-sm font-medium text-frost">{item.value}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Error Log */}
      <section>
        <h2 className="text-lg font-headline font-semibold text-frost mb-4">
          Error Log
          {errors.length > 0 && (
            <span className="ml-2 text-sm font-mono text-coral">({errors.length} errors)</span>
          )}
        </h2>

        {errors.length === 0 ? (
          <div className="bg-mint/5 border border-mint/20 rounded-xl p-8 text-center">
            <p className="text-mint/70 text-sm">No errors — all records processed successfully</p>
          </div>
        ) : (
          <div className="space-y-3">
            {errors.map(err => (
              <div
                key={err.id}
                className="bg-coral/5 border border-coral/20 rounded-xl p-4"
              >
                <div className="flex items-center justify-between mb-2">
                  <span className="text-xs font-mono text-coral">Error #{err.id}</span>
                  <span className="text-xs font-mono text-frost-dim/40">
                    {new Date(err.occurredAt).toLocaleString()}
                  </span>
                </div>
                <p className="text-sm text-coral mb-2">{err.errorReason}</p>
                {err.rawData && (
                  <div className="mt-2">
                    <p className="text-xs text-frost-dim/40 mb-1">Raw Data:</p>
                    <pre className="text-xs font-mono bg-carbon/50 border border-steel/30 rounded p-3 overflow-x-auto text-frost-dim/70 whitespace-pre-wrap break-all">
                      {err.rawData}
                    </pre>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  )
}
