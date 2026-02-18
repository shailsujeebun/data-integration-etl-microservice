import type { Config } from 'tailwindcss'

const config: Config = {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        carbon: '#111827',
        steel: '#374151',
        'steel-light': '#4B5563',
        frost: '#F3F4F6',
        'frost-dim': '#D1D5DB',
        mint: '#34D399',
        'mint-dark': '#10B981',
        coral: '#FB7185',
        'coral-dark': '#F43F5E',
      },
      fontFamily: {
        headline: ['Space Grotesk', 'sans-serif'],
        body: ['Source Sans 3', 'sans-serif'],
        mono: ['IBM Plex Mono', 'monospace'],
      },
      animation: {
        'pulse-slow': 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
      },
    },
  },
  plugins: [],
}

export default config
