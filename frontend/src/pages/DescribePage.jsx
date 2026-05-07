import { useState } from 'react';
import { describeNda } from '../services/api';

const SAMPLE = `This Non-Disclosure Agreement ("NDA") is entered into between Acme Corp ("Disclosing Party") and Vendor Ltd ("Receiving Party"). The Receiving Party agrees to keep all confidential information strictly confidential for a period of 3 years. The NDA includes unlimited liability for any breach, with no cap on damages. There is no exit clause or termination mechanism. The agreement applies globally with no geographic restrictions.`;

/**
 * DescribePage — Sends NDA text to /vendor/describe and shows plain-language result.
 */
export default function DescribePage({ addToast }) {
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
    if (text.length > 5000) {
      addToast('error', 'Input exceeds 5000 character limit.');
      return;
    }

    setLoading(true);
    setResult(null);
    try {
      const data = await describeNda(text);
      setResult(data);
      addToast('success', 'NDA description generated successfully!');
    } catch (err) {
      addToast('error', err.message || 'Failed to connect to backend. Is it running?');
    } finally {
      setLoading(false);
    }
  }

  function useSample() {
    setInput(SAMPLE);
    setResult(null);
  }

  const charWarn = input.length > 4500;

  return (
    <div className="page-wrapper">
      <div className="container">
        <div className="page-header">
          <span className="badge badge-indigo" style={{ marginBottom: 12 }}>📄 Describe Mode</span>
          <h2>Plain-Language <span className="gradient-text">NDA Description</span></h2>
          <p>Paste complex NDA text and get a clear, human-readable explanation of what it means.</p>
        </div>

        <div className="info-bar">
          ℹ️ The AI will rephrase the legal language into plain English, highlighting key terms and obligations.
        </div>

        <div className="analyzer-layout">
          {/* ── Input Panel ── */}
          <div>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
                <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)' }}>
                  NDA Text Input
                </label>
                <button type="button" className="btn btn-secondary btn-sm" onClick={useSample}>
                  Load Sample
                </button>
              </div>

              <div className="textarea-wrapper">
                <textarea
                  id="describe-input"
                  value={input}
                  onChange={e => setInput(e.target.value)}
                  placeholder="Paste your NDA clause or full document text here...&#10;&#10;Example: 'The vendor shall maintain strict confidentiality of all technical specifications...'"
                  maxLength={5000}
                />
                <span className={`char-count ${charWarn ? 'warn' : ''}`}>
                  {input.length}/5000
                </span>
              </div>

              <button
                type="submit"
                className="btn btn-primary btn-full"
                style={{ marginTop: 12 }}
                disabled={loading || input.trim().length < 10}
              >
                {loading ? (
                  <><div className="spinner" style={{ width: 16, height: 16, borderWidth: 2 }} /> Generating…</>
                ) : (
                  <> 🔍 Describe NDA</>
                )}
              </button>
            </form>
          </div>

          {/* ── Result Panel ── */}
          <div className="result-panel">
            <div className="result-panel-header">
              <h3>📄 AI Description</h3>
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
                  <p>AI is analysing your NDA…</p>
                </div>
              )}

              {!loading && !result && (
                <div className="empty-state">
                  <div className="empty-icon">📄</div>
                  <p>Your NDA description will appear here once you submit.</p>
                </div>
              )}

              {!loading && result && (
                <div className="animate-fade-up">
                  <p className="describe-text">{result.result}</p>
                  {result.generated_at && (
                    <p style={{ fontSize: '0.72rem', color: 'var(--text-muted)', marginTop: 16, fontFamily: 'JetBrains Mono, monospace' }}>
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
