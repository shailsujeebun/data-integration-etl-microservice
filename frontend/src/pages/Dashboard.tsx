import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useJobs } from '../hooks/useJobs'
import { useSources } from '../hooks/useSources'
import { triggerJob } from '../api/client'
import { StatusBadge } from '../components/shared/StatusBadge'
import { SourceTypeBadge } from '../components/shared/SourceTypeBadge'
import { LoadingSpinner } from '../components/shared/LoadingSpinner'
import type { JobHistory, SourceConfig } from '../types'

// ── Stats Cards ──────────────────────────────────────────────────────────────

function StatsCards({ jobs }: { jobs: JobHistory[] }) {
  const total = jobs.length
  const successful = jobs.filter(j => j.status === 'SUCCESS').length
  const partial = jobs.filter(j => j.status === 'PARTIAL').length
  const failed = jobs.filter(j => j.status === 'FAILED').length
  const totalRecords = jobs.reduce((sum, j) => sum + j.recordsProcessed, 0)
  const successRate = total > 0 ? Math.round((successful / total) * 100) : 0

  const cards = [
    { label: 'Total Runs', value: total, color: 'text-frost' },
    { label: 'Success Rate', value: `${successRate}%`, color: 'text-mint' },
    { label: 'Records Loaded', value: totalRecords.toLocaleString(), color: 'text-frost' },
    { label: 'Partial / Failed', value: `${partial} / ${failed}`, color: failed > 0 ? 'text-coral' : 'text-frost-dim' },
  ]

  return (
    <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
      {cards.map(card => (
        <div key={card.label} className="bg-steel/30 border border-steel/50 rounded-xl p-5">
          <p className="text-xs text-frost-dim/60 font-medium uppercase tracking-wider mb-2">{card.label}</p>
          <p className={`text-2xl font-headline font-bold ${card.color}`}>{card.value}</p>
        </div>
      ))}
    </div>
  )
}

// ── Source Card ───────────────────────────────────────────────────────────────

function SourceCard({ source, onTrigger }: { source: SourceConfig; onTrigger: () => void }) {
  const [triggering, setTriggering] = useState(false)

  const handleTrigger = async () => {
    setTriggering(true)
    try {
      await onTrigger()
    } finally {
      // Keep spinner briefly to give feedback even if fast
      setTimeout(() => setTriggering(false), 1500)
    }
  }

  const lastRun = source.lastRunTimestamp
    ? new Date(source.lastRunTimestamp).toLocaleString()
    : 'Never'

  return (
    <div className="bg-steel/20 border border-steel/40 rounded-xl p-5 flex flex-col gap-3">
      <div className="flex items-start justify-between gap-2">
        <div className="flex-1 min-w-0">
          <p className="font-headline font-semibold text-frost truncate">{source.name}</p>
          <p className="text-xs text-frost-dim/50 font-mono mt-0.5 truncate">{source.connectionString}</p>
        </div>
        <SourceTypeBadge type={source.type} />
      </div>

      <div className="text-xs text-frost-dim/50 space-y-1">
        <p>Cron: <span className="font-mono text-frost-dim/70">{source.scheduleCron ?? '—'}</span></p>
        <p>Last run: <span className="font-mono text-frost-dim/70">{lastRun}</span></p>
      </div>

      <button
        onClick={handleTrigger}
        disabled={triggering || !source.isActive}
        className="mt-auto flex items-center justify-center gap-2 w-full py-2 px-4 rounded-lg
          bg-mint/10 border border-mint/20 text-mint text-sm font-medium
          hover:bg-mint/20 hover:border-mint/40 transition-colors
          disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {triggering ? (
          <>
            <LoadingSpinner size="sm" />
            <span>Triggering...</span>
          </>
        ) : (
          <>
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>Trigger Now</span>
          </>
        )}
      </button>
    </div>
  )
}

// ── Job History Table ─────────────────────────────────────────────────────────

function JobHistoryTable({ jobs, loading }: { jobs: JobHistory[]; loading: boolean }) {
  const navigate = useNavigate()

  if (loading) {
    return (
      <div className="flex items-center justify-center py-16">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (jobs.length === 0) {
    return (
      <div className="bg-steel/20 border border-steel/40 rounded-xl p-12 text-center">
        <p className="text-frost-dim/50">No pipeline runs yet. Trigger a job to get started.</p>
      </div>
    )
  }

  return (
    <div className="bg-steel/20 border border-steel/40 rounded-xl overflow-hidden">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-steel/50">
            <th className="text-left px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Source</th>
            <th className="text-left px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Status</th>
            <th className="text-right px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Records</th>
            <th className="text-right px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Errors</th>
            <th className="text-right px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Duration</th>
            <th className="text-left px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Trigger</th>
            <th className="text-left px-5 py-3 text-xs font-medium text-frost-dim/60 uppercase tracking-wider">Started</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-steel/30">
          {jobs.map(job => (
            <tr
              key={job.id}
              onClick={() => navigate(`/jobs/${job.id}`)}
              className="hover:bg-steel/30 cursor-pointer transition-colors"
            >
              <td className="px-5 py-3 font-medium text-frost truncate max-w-[180px]">
                {job.sourceName ?? '—'}
              </td>
              <td className="px-5 py-3">
                <StatusBadge status={job.status} />
              </td>
              <td className="px-5 py-3 text-right font-mono text-frost-dim">
                {job.recordsProcessed.toLocaleString()}
              </td>
              <td className="px-5 py-3 text-right font-mono">
                <span className={job.errorCount > 0 ? 'text-coral' : 'text-frost-dim'}>
                  {job.errorCount}
                </span>
              </td>
              <td className="px-5 py-3 text-right font-mono text-frost-dim">
                {job.durationMs != null ? `${job.durationMs}ms` : '—'}
              </td>
              <td className="px-5 py-3">
                <span className="text-xs font-mono text-frost-dim/60">{job.triggeredBy}</span>
              </td>
              <td className="px-5 py-3 text-xs text-frost-dim/60 font-mono">
                {new Date(job.startTime).toLocaleString()}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

// ── Dashboard Page ────────────────────────────────────────────────────────────

export function Dashboard() {
  const { data: jobsPage, loading: jobsLoading, refetch } = useJobs(3000)
  const { data: sources } = useSources()

  const jobs = jobsPage?.content ?? []

  const handleTrigger = async (sourceId: number) => {
    await triggerJob(sourceId)
    await refetch()
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-headline font-bold text-frost">Pipeline Dashboard</h1>
        <p className="text-sm text-frost-dim/60 mt-1">Monitor ETL runs, trigger jobs, and inspect errors</p>
      </div>

      {/* Stats */}
      <StatsCards jobs={jobs} />

      {/* Source Cards */}
      <section>
        <h2 className="text-lg font-headline font-semibold text-frost mb-4">Data Sources</h2>
        {sources.length === 0 ? (
          <p className="text-frost-dim/50 text-sm">No sources configured.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {sources.map(source => (
              <SourceCard
                key={source.id}
                source={source}
                onTrigger={() => handleTrigger(source.id)}
              />
            ))}
          </div>
        )}
      </section>

      {/* Job History */}
      <section>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-headline font-semibold text-frost">Pipeline Run History</h2>
          <span className="text-xs text-frost-dim/40 font-mono">auto-refreshes every 3s</span>
        </div>
        <JobHistoryTable jobs={jobs} loading={jobsLoading} />
      </section>
    </div>
  )
}
