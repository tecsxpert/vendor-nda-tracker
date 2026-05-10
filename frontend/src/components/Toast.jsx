/**
 * Toast — Lightweight notification component.
 * Auto-dismisses after 4 seconds.
 */
export default function Toast({ toasts, removeToast }) {
  return (
    <div className="toast-container">
      {toasts.map(t => (
        <div
          key={t.id}
          className={`toast toast-${t.type}`}
          onClick={() => removeToast(t.id)}
          style={{ cursor: 'pointer' }}
        >
          <span>{t.type === 'error' ? '⚠️' : '✅'}</span>
          <span style={{ flex: 1 }}>{t.message}</span>
          <span style={{ opacity: 0.5, fontSize: '0.75rem' }}>✕</span>
        </div>
      ))}
    </div>
  );
}
