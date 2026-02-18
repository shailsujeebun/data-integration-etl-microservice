import type { SourceType } from '../../types'

const TYPE_CONFIG: Record<SourceType, { classes: string; label: string }> = {
  API: { classes: 'bg-purple-500/20 text-purple-300 border-purple-500/30', label: 'API' },
  CSV: { classes: 'bg-cyan-500/20 text-cyan-300 border-cyan-500/30', label: 'CSV' },
  DB: { classes: 'bg-orange-500/20 text-orange-300 border-orange-500/30', label: 'DB' },
}

interface SourceTypeBadgeProps {
  type: SourceType
}

export function SourceTypeBadge({ type }: SourceTypeBadgeProps) {
  const config = TYPE_CONFIG[type]
  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-mono font-medium border ${config.classes}`}
    >
      {config.label}
    </span>
  )
}
