import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Layout } from './components/layout/Layout'
import { Dashboard } from './pages/Dashboard'
import { Sources } from './pages/Sources'
import { JobDetail } from './pages/JobDetail'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="sources" element={<Sources />} />
          <Route path="jobs/:id" element={<JobDetail />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
