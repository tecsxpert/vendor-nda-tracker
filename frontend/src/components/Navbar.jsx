import { useState, useEffect, useRef } from 'react';

/**
 * Navbar — Sticky navigation with active link highlighting.
 */
export default function Navbar({ activePage, setPage }) {
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handler = () => setScrolled(window.scrollY > 10);
    window.addEventListener('scroll', handler);
    return () => window.removeEventListener('scroll', handler);
  }, []);

  const links = [
    { id: 'home',     label: 'Home',       icon: '⬡' },
    { id: 'describe', label: 'Describe',   icon: '📄' },
    { id: 'recommend',label: 'Recommend',  icon: '💡' },
    { id: 'report',   label: 'Risk Report',icon: '📊' },
  ];

  return (
    <nav className="navbar" style={{ boxShadow: scrolled ? '0 2px 24px rgba(0,0,0,0.4)' : 'none' }}>
      <div className="navbar-inner">
        <a
          href="#"
          className="navbar-logo"
          onClick={e => { e.preventDefault(); setPage('home'); }}
        >
          <div className="logo-icon">🔐</div>
          <span>NDA<span className="gradient-text">Tracker</span></span>
        </a>

        <ul className="navbar-links">
          {links.map(link => (
            <li key={link.id}>
              <a
                href="#"
                className={activePage === link.id ? 'active' : ''}
                onClick={e => { e.preventDefault(); setPage(link.id); }}
              >
                {link.icon} {link.label}
              </a>
            </li>
          ))}
        </ul>

        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span className="badge badge-green" style={{ fontSize: '0.7rem' }}>
            <span style={{ width: 6, height: 6, borderRadius: '50%', background: 'currentColor', display: 'inline-block' }}></span>
            AI Ready
          </span>
        </div>
      </div>
    </nav>
  );
}
