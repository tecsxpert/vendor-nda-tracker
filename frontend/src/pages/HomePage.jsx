/**
 * HomePage — Landing page with hero, features, and call to action.
 */
export default function HomePage({ setPage }) {
  const features = [
    {
      icon: '📄',
      emoji_bg: 'rgba(99,102,241,0.15)',
      title: 'Plain-Language Describe',
      desc: 'Submit raw NDA text and get an instant, easy-to-understand summary of what the agreement actually says.',
      page: 'describe',
      badge: 'AI Powered',
      badgeClass: 'badge-indigo',
    },
    {
      icon: '💡',
      emoji_bg: 'rgba(16,185,129,0.15)',
      title: 'Smart Recommendations',
      desc: 'Get structured action items — what to Review, Negotiate, or Flag before signing any vendor agreement.',
      page: 'recommend',
      badge: 'Actionable',
      badgeClass: 'badge-green',
    },
    {
      icon: '📊',
      emoji_bg: 'rgba(245,158,11,0.15)',
      title: 'Full Risk Report',
      desc: 'Generate a complete NDA risk assessment including summary, key clauses, and prioritized recommendations.',
      page: 'report',
      badge: 'Comprehensive',
      badgeClass: 'badge-amber',
    },
  ];

  const howItWorks = [
    { step: '01', title: 'Paste NDA Text', desc: 'Copy the NDA clause or full document text into the input field.' },
    { step: '02', title: 'Choose Analysis', desc: 'Select Describe, Recommend, or full Risk Report analysis.' },
    { step: '03', title: 'Get AI Insights', desc: 'Our LLaMA 3.3 powered AI returns structured, actionable insights instantly.' },
  ];

  return (
    <>
      {/* ── Hero ── */}
      <section className="hero">
        <div className="hero-glow" />
        <div className="container">
          <div className="hero-eyebrow animate-fade-up">
            <span>⚡</span> Powered by LLaMA 3.3 via Groq API
          </div>

          <h1 className="animate-fade-up delay-1">
            Understand Every<br />
            <span className="gradient-text">Vendor NDA</span> Instantly
          </h1>

          <p className="hero-desc animate-fade-up delay-2">
            AI-powered NDA analysis that turns complex legal language into clear summaries,
            smart recommendations, and comprehensive risk reports — in seconds.
          </p>

          <div className="hero-actions animate-fade-up delay-3">
            <button className="btn btn-primary" onClick={() => setPage('describe')}>
              🚀 Try NDA Describe
            </button>
            <button className="btn btn-secondary" onClick={() => setPage('report')}>
              📊 Generate Report
            </button>
          </div>

          <div className="hero-stats animate-fade-up delay-4">
            {[
              { num: '3', label: 'AI Endpoints' },
              { num: '<2s', label: 'Avg Response Time' },
              { num: '100%', label: 'Fallback Safe' },
              { num: 'LLaMA 3.3', label: 'AI Model' },
            ].map(s => (
              <div className="stat-item" key={s.label}>
                <div className="stat-number">{s.num}</div>
                <div className="stat-label">{s.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Features ── */}
      <section style={{ padding: '0 24px 80px' }}>
        <div className="container">
          <div style={{ textAlign: 'center', marginBottom: 12 }}>
            <span className="badge badge-indigo">Features</span>
          </div>
          <h2 style={{ textAlign: 'center', marginBottom: 8 }}>
            Three Modes of <span className="gradient-text">NDA Intelligence</span>
          </h2>
          <p style={{ textAlign: 'center', color: 'var(--text-secondary)', marginBottom: 0 }}>
            Choose the level of analysis you need — from quick summaries to full risk assessments.
          </p>

          <div className="features-grid">
            {features.map(f => (
              <div className="feature-card" key={f.title}>
                <div className="feature-icon" style={{ background: f.emoji_bg }}>
                  {f.icon}
                </div>
                <span className={`badge ${f.badgeClass}`} style={{ marginBottom: 12 }}>
                  {f.badge}
                </span>
                <h3>{f.title}</h3>
                <p style={{ fontSize: '0.9rem', lineHeight: 1.7, marginBottom: 20 }}>{f.desc}</p>
                <button
                  className="btn btn-secondary btn-sm"
                  onClick={() => setPage(f.page)}
                >
                  Try it →
                </button>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── How it works ── */}
      <section style={{ padding: '0 24px 80px' }}>
        <div className="container">
          <div style={{ textAlign: 'center', marginBottom: 12 }}>
            <span className="badge badge-indigo">How It Works</span>
          </div>
          <h2 style={{ textAlign: 'center', marginBottom: 40 }}>
            Three Simple <span className="gradient-text">Steps</span>
          </h2>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 20 }}>
            {howItWorks.map(s => (
              <div className="card" key={s.step} style={{ textAlign: 'center' }}>
                <div style={{
                  width: 48, height: 48, borderRadius: '50%',
                  background: 'var(--accent-glow)', border: '1px solid var(--border-accent)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  margin: '0 auto 16px',
                  fontWeight: 800, fontSize: '1rem', color: 'var(--accent-light)',
                  fontFamily: 'JetBrains Mono, monospace',
                }}>
                  {s.step}
                </div>
                <h3 style={{ marginBottom: 8 }}>{s.title}</h3>
                <p style={{ fontSize: '0.875rem' }}>{s.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── CTA Banner ── */}
      <section style={{ padding: '0 24px 80px' }}>
        <div className="container">
          <div className="card card-accent" style={{ textAlign: 'center', padding: '48px 24px' }}>
            <h2 style={{ marginBottom: 12 }}>Ready to Analyse Your <span className="gradient-text">NDA?</span></h2>
            <p style={{ marginBottom: 28 }}>
              Paste your NDA text and get AI-powered insights in under 2 seconds.
            </p>
            <div style={{ display: 'flex', gap: 12, justifyContent: 'center', flexWrap: 'wrap' }}>
              <button className="btn btn-primary" onClick={() => setPage('describe')}>Start Now →</button>
              <button className="btn btn-secondary" onClick={() => setPage('recommend')}>Get Recommendations</button>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
