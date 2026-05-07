import { useState } from 'react';
import { generateReport } from '../services/api';

const SAMPLE = `Vendor NDA with 3-year confidentiality period enforced globally. The agreement includes an unlimited liability clause with no damage cap. There is no exit clause or termination mechanism. All intellectual property developed during the engagement is fully owned by the vendor. A 2-year non-compete clause restricts working with competitors after contract end. Dispute resolution is limited to the vendor's home jurisdiction only.`;

/**
 * ReportPage — Full NDA risk report with sections: Summary, Overview, Key Items, Recommendations.
 */
export default function ReportPage({ addToast }) {
  const [input, setInput]     = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult]   = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    const text = input.trim();
    if (!text || text.length < 10) {
      addToast('error', 'Please enter at least 10 characters of NDA text.');
      return;
    }

    setLoading(true);
    setResult(null);
    try {
      const data = await generateReport(text);
      setResult(data);
      addToast('success', 'Full NDA risk report generated!');
    } catch (err) {
      addToast('error', err.message || 'Failed to connect to backend. Is it running?');
    } finally {
      setLoading(false);
    }
  }

  const report = result?.report;

  return (
    <div className="page-wrapper">
      <div className="container">
        <div className="page-header">
          <span className="badge badge-amber" style={{ marginBottom: 12 }}>📊 Risk Report Mode</span>
          <h2>Full NDA <span className="gradient-text">Risk Report</span></h2>
          <p>Generate a comprehensive risk assessment covering summary, key clauses, and prioritised recommendations.</p>
        </div>

        <div className="info-bar" style={{ borderColor: 'rgba(245,158,11,0.4)', background: 'rgba(245,158,11,0.08)', color: 'var(--accent-amber)' }}>
          📊 This is the most detailed analysis mode — includes title, summary, overview, key items, and recommendations.
        </div>

        <div className="analyzer-layout">
          {/* ── Input ── */}
          <div>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
                <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)' }}>
                  NDA Text Input
                </label>
                <button type="button" className="btn btn-secondary btn-sm" onClick={() => { setInput(SAMPLE); setResult(null); }}>
                  Load Sample
                </button>
              </div>

              <div className="textarea-wrapper">
                <textarea
                  id="report-input"
                  value={input}
                  onChange={e => setInput(e.target.value)}
                  placeholder="Paste the full vendor NDA text here…&#10;&#10;Include all clauses: confidentiality, liability, IP, termination, non-compete."
                  maxLength={5000}
                  style={{ minHeight: 320 }}
                />
                <span className={`char-count ${input.length > 4500 ? 'warn' : ''}`}>
                  {input.length}/5000
                </span>
              </div>

              <button
                type="submit"
                className="btn btn-full"
                style={{ marginTop: 12, background: 'linear-gradient(135deg, var(--accent-amber), #d97706)', color: '#000', fontWeight: 700, boxShadow: '0 4px 16px rgba(245,158,11,0.3)' }}
                disabled={loading || input.trim().length < 10}
              >
                {loading ? (
                  <><div className="spinner" style={{ width: 16, height: 16, borderWidth: 2, borderTopColor: '#000' }} /> Generating Report…</>
                ) : (
                  <>📊 Generate Risk Report</>
                )}
              </button>
            </form>

            {/* Metadata */}
            {result && (
              <div className="card" style={{ marginTop: 16 }}>
                <p style={{ fontSize: '0.75rem', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--text-muted)', marginBottom: 12 }}>
                  Report Metadata
                </p>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: '0.82rem' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: 'var(--text-muted)' }}>Status</span>
                    <span className={`badge ${result.is_fallback ? 'badge-amber' : 'badge-green'}`}>
                      {result.is_fallback ? '⚠ Fallback' : '✓ AI Generated'}
                    </span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: 'var(--text-muted)' }}>Cached</span>
                    <span style={{ color: result.cached ? 'var(--accent-light)' : 'var(--text-secondary)' }}>
                      {result.cached ? '⚡ Yes' : 'No'}
                    </span>
                  </div>
                  {result.generated_at && (
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                      <span style={{ color: 'var(--text-muted)' }}>Generated</span>
                      <span style={{ fontFamily: 'JetBrains Mono, monospace', fontSize: '0.72rem', color: 'var(--text-secondary)' }}>
                        {new Date(result.generated_at).toLocaleTimeString()}
                      </span>
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>

          {/* ── Report Panel ── */}
          <div className="result-panel" style={{ minHeight: 400 }}>
            <div className="result-panel-header">
              <h3>📊 Risk Report</h3>
              {report && (
                <span className="badge badge-amber">
                  {report.title || 'Report'}
                </span>
              )}
            </div>

            <div className="result-body">
              {loading && (
                <div className="empty-state">
                  <div className="spinner" />
                  <p>AI is generating your full NDA risk report…<br/>This may take a few seconds.</p>
                </div>
              )}

              {!loading && !result && (
                <div className="empty-state">
                  <div className="empty-icon">📊</div>
                  <p>Your full NDA risk report will appear here with summary, key clauses, and recommendations.</p>
                </div>
              )}

              {!loading && report && (
                <div className="animate-fade-up">
                  {/* Title */}
                  <h3 style={{ marginBottom: 16, color: 'var(--accent-light)' }}>{report.title}</h3>

                  <hr className="report-divider" />

                  {/* Summary */}
                  {report.summary && (
                    <div className="report-section">
                      <p className="report-section-title">Summary</p>
                      <p className="report-summary">{report.summary}</p>
                    </div>
                  )}

                  {/* Overview */}
                  {report.overview && (
                    <div className="report-section">
                      <p className="report-section-title">Overview</p>
                      <p className="report-overview">{report.overview}</p>
                    </div>
                  )}

                  <hr className="report-divider" />

                  {/* Key Items */}
                  {report.key_items?.length > 0 && (
                    <div className="report-section">
                      <p className="report-section-title">Key Clauses / Items</p>
                      <ul className="key-items-list">
                        {report.key_items.map((item, i) => (
                          <li key={i}><span>{item}</span></li>
                        ))}
                      </ul>
                    </div>
                  )}

                  {/* Recommendations */}
                  {report.recommendations?.length > 0 && (
                    <div className="report-section">
                      <p className="report-section-title">Recommendations</p>
                      <ul className="rec-list">
                        {report.recommendations.map((rec, i) => (
                          <li key={i}><span style={{ color: 'var(--accent-green)' }}>→</span><span>{rec}</span></li>
                        ))}
                      </ul>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
