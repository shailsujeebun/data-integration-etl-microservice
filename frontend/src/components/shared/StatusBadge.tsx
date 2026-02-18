import type { JobStatus } from '../../types'

const STATUS_CONFIG: Record<JobStatus, { classes: string; dot?: string; label: string }> = {
  RUNNING: {
    classes: 'bg-blue-500/20 text-blue-300 border-blue-500/30',
    dot: 'bg-blue-400 animate-pulse',
    label: 'RUNNING',
  },
  SUCCESS: {
    classes: 'bg-mint/20 text-mint border-mint/30',
    label: 'SUCCESS',
  },
  FAILED: {
    classes: 'bg-coral/20 text-coral border-coral/30',
    label: 'FAILED',
  },
  PARTIAL: {
    classes: 'bg-yellow-500/20 text-yellow-300 border-yellow-500/30',
    label: 'PARTIAL',
  },
}

interface StatusBadgeProps {
  status: JobStatus
}

export function StatusBadge({ status }: StatusBadgeProps) {
  const config = STATUS_CONFIG[status]
  return (
    <span
      className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-mono font-medium border ${config.classes}`}
    >
      {config.dot && (
        <span className={`h-1.5 w-1.5 rounded-full ${config.dot}`} />
      )}
      {config.label}
    </span>
  )
}
