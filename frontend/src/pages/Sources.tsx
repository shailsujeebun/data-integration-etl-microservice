import { useSources } from '../hooks/useSources'
import { SourceTypeBadge } from '../components/shared/SourceTypeBadge'
import { LoadingSpinner } from '../components/shared/LoadingSpinner'

export function Sources() {
  const { data: sources, loading, error } = useSources()

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-headline font-bold text-frost">Data Sources</h1>
        <p className="text-sm text-frost-dim/60 mt-1">Configured ingestion sources and their connection details</p>
      </div>

      {loading && (
        <div className="flex items-center justify-center py-16">
          <LoadingSpinner size="lg" />
        </div>
      )}

      {error && (
        <div className="bg-coral/10 border border-coral/30 rounded-lg px-5 py-4 text-coral text-sm">
          {error}
        </div>
      )}

      {!loading && !error && sources.length === 0 && (
        <div className="bg-steel/20 border border-steel/40 rounded-xl p-12 text-center">
          <p className="text-frost-dim/50">No data sources configured.</p>
        </div>
      )}

      {!loading && sources.length > 0 && (
        <div className="space-y-3">
          {sources.map(source => (
            <div
              key={source.id}
              className="bg-steel/20 border border-steel/40 rounded-xl p-5"
            >
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-3 mb-2">
                    <SourceTypeBadge type={source.type} />
                    <h3 className="font-headline font-semibold text-frost">{source.name}</h3>
                    {source.hasAuthConfig && (
                      <span className="text-xs font-mono text-frost-dim/40 border border-steel/50 rounded px-1.5 py-0.5" title="Authentication configured">
                        🔒 AUTH
                      </span>
                    )}
                    {!source.isActive && (
                      <span className="text-xs font-mono text-frost-dim/40 border border-steel/50 rounded px-1.5 py-0.5">
                        INACTIVE
                      </span>
                    )}
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-x-8 gap-y-2 text-sm mt-3">
                    <div>
                      <p className="text-xs text-frost-dim/40 uppercase tracking-wider mb-1">Connection</p>
                      <p className="font-mono text-frost-dim text-xs truncate">{source.connectionString}</p>
                    </div>
                    <div>
                      <p className="text-xs text-frost-dim/40 uppercase tracking-wider mb-1">Schedule (Cron)</p>
                      <p className="font-mono text-frost-dim text-xs">{source.scheduleCron ?? '—'}</p>
                    </div>
                    <div>
                      <p className="text-xs text-frost-dim/40 uppercase tracking-wider mb-1">Last Run</p>
                      <p className="font-mono text-frost-dim text-xs">
                        {source.lastRunTimestamp
                          ? new Date(source.lastRunTimestamp).toLocaleString()
                          : 'Never'}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="text-right text-xs text-frost-dim/40 font-mono whitespace-nowrap">
                  ID: {source.id}
                  <br />
                  <span className="text-frost-dim/30">
                    {new Date(source.createdAt).toLocaleDateString()}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
