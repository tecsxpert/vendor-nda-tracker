import { useState } from 'react';
import { recommendNda } from '../services/api';

const SAMPLE = `Vendor NDA includes unlimited liability clause with no cap on damages. There is no exit clause or termination mechanism. The confidentiality period spans 5 years globally. Intellectual property created during the engagement is fully owned by the vendor. The agreement restricts the company from working with any competitor for 2 years after termination.`;

const ACTION_STYLES = {
  Review:    { class: 'badge-indigo', icon: '🔍' },
  Negotiate: { class: 'badge-amber',  icon: '🤝' },
  Flag:      { class: 'badge-red',    icon: '🚩' },
  Consult:   { class: 'badge-green',  icon: '💬' },
  Accept:    { class: 'badge-green',  icon: '✅' },
};

function getActionStyle(actionType) {
  return ACTION_STYLES[actionType] || { class: 'badge-indigo', icon: '📌' };
}

/**
 * RecommendPage — Sends NDA text to /vendor/recommend and shows action items.
 */
export default function RecommendPage({ addToast }) {
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
      const data = await recommendNda(text);
      setResult(data);
      addToast('success', `Got ${data.recommendations?.length || 0} recommendations!`);
    } catch (err) {
      addToast('error', err.message || 'Failed to connect to backend. Is it running?');
    } finally {
      setLoading(false);
    }
  }

  const recs = result?.recommendations || [];

  return (
    <div className="page-wrapper">
      <div className="container">
        <div className="page-header">
          <span className="badge badge-green" style={{ marginBottom: 12 }}>💡 Recommend Mode</span>
          <h2>AI-Powered <span className="gradient-text">NDA Recommendations</span></h2>
          <p>Get structured, actionable steps — what to Review, Negotiate, Flag, or Accept.</p>
        </div>

        <div className="info-bar" style={{ borderColor: 'rgba(16,185,129,0.4)', background: 'rgba(16,185,129,0.08)', color: 'var(--accent-green)' }}>
          💡 Each recommendation includes an action type and clear description to guide your decision.
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
                  id="recommend-input"
                  value={input}
                  onChange={e => setInput(e.target.value)}
                  placeholder="Paste NDA clauses here for recommendations…&#10;&#10;Focus on: liability, IP, termination, non-compete clauses."
                  maxLength={5000}
                />
                <span className={`char-count ${input.length > 4500 ? 'warn' : ''}`}>
                  {input.length}/5000
                </span>
              </div>

              <button
                type="submit"
                className="btn btn-full"
                style={{ marginTop: 12, background: 'linear-gradient(135deg, var(--accent-green), #059669)', color: '#fff', boxShadow: '0 4px 16px rgba(16,185,129,0.3)' }}
                disabled={loading || input.trim().length < 10}
              >
                {loading ? (
                  <><div className="spinner" style={{ width: 16, height: 16, borderWidth: 2 }} /> Generating…</>
                ) : (
                  <>💡 Get Recommendations</>
                )}
              </button>
            </form>

            {/* Legend */}
            {!loading && recs.length > 0 && (
              <div className="card" style={{ marginTop: 16 }}>
                <p style={{ fontSize: '0.75rem', fontWeight: 700, letterSpacing: '0.05em', textTransform: 'uppercase', color: 'var(--text-muted)', marginBottom: 10 }}>
                  Action Types
                </p>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
                  {Object.entries(ACTION_STYLES).map(([k, v]) => (
                    <span key={k} className={`badge ${v.class}`}>{v.icon} {k}</span>
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* ── Results ── */}
          <div className="result-panel">
            <div className="result-panel-header">
              <h3>💡 Recommendations {recs.length > 0 && <span style={{ color: 'var(--text-muted)', fontWeight: 400 }}>({recs.length})</span>}</h3>
              {result && (
                <span className={`badge ${result.is_fallback ? 'badge-amber' : 'badge-green'}`}>
                  {result.is_fallback ? '⚠ Fallback' : '✓ AI Generated'}
                </span>
              )}
            </div>

            <div className="result-body">
              {loading && (
                <div className="empty-state">
                  <div className="spinner" />
                  <p>AI is generating recommendations…</p>
                </div>
              )}

              {!loading && !result && (
                <div className="empty-state">
                  <div className="empty-icon">💡</div>
                  <p>Recommendations will appear here after analysis.</p>
                </div>
              )}

              {!loading && result && recs.length === 0 && (
                <div className="empty-state">
                  <div className="empty-icon">🤔</div>
                  <p>No recommendations returned. Try a more detailed NDA text.</p>
                </div>
              )}

              {!loading && recs.length > 0 && (
                <div className="recommendation-list animate-fade-up">
                  {recs.map((rec, i) => {
                    const style = getActionStyle(rec.action_type);
                    return (
                      <div className="recommendation-item" key={i}>
                        <div className="rec-action-badge">
                          <span className={`badge ${style.class}`}>
                            {style.icon} {rec.action_type}
                          </span>
                        </div>
                        <p className="rec-desc">{rec.description}</p>
                      </div>
                    );
                  })}
                  {result.generated_at && (
                    <p style={{ fontSize: '0.72rem', color: 'var(--text-muted)', marginTop: 8, fontFamily: 'JetBrains Mono, monospace' }}>
                      Generated: {new Date(result.generated_at).toLocaleString()}
                    </p>
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
