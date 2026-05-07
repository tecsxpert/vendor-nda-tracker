import { useState, useCallback, useEffect } from 'react';
import './index.css';
import Navbar from './components/Navbar';
import Toast from './components/Toast';
import HomePage from './pages/HomePage';
import DescribePage from './pages/DescribePage';
import RecommendPage from './pages/RecommendPage';
import ReportPage from './pages/ReportPage';

let toastIdCounter = 0;

/**
 * App — Root component managing routing and toast notifications.
 *
 * Uses simple state-based routing (no React Router needed for this SPA).
 * Pages: home | describe | recommend | report
 */
export default function App() {
  const [page, setPage]   = useState('home');
  const [toasts, setToasts] = useState([]);

  // ── Toast helpers ──
  const addToast = useCallback((type, message) => {
    const id = ++toastIdCounter;
    setToasts(prev => [...prev, { id, type, message }]);
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 4000);
  }, []);

  const removeToast = useCallback(id => {
    setToasts(prev => prev.filter(t => t.id !== id));
  }, []);

  // ── Page title sync ──
  useEffect(() => {
    const titles = {
      home:     'Vendor NDA Tracker — AI-Powered NDA Analysis',
      describe: 'Describe NDA — Vendor NDA Tracker',
      recommend:'Recommendations — Vendor NDA Tracker',
      report:   'Risk Report — Vendor NDA Tracker',
    };
    document.title = titles[page] || titles.home;
  }, [page]);

  // ── Render current page ──
  const renderPage = () => {
    switch (page) {
      case 'describe':  return <DescribePage  addToast={addToast} />;
      case 'recommend': return <RecommendPage addToast={addToast} />;
      case 'report':    return <ReportPage    addToast={addToast} />;
      default:          return <HomePage      setPage={setPage} />;
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar activePage={page} setPage={setPage} />

      <main style={{ flex: 1 }}>
        {renderPage()}
      </main>

      <footer className="footer">
        <p>
          Built by <span>Avinash</span> · Vendor NDA Tracker ·{' '}
          <span>Java Spring Boot</span> + <span>Flask AI</span> + <span>React</span>
        </p>
      </footer>

      <Toast toasts={toasts} removeToast={removeToast} />
    </div>
  );
}
