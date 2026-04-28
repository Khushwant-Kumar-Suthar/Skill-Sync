import { useState, useEffect, useCallback, createContext, useContext } from "react";

// ─── API Configuration ────────────────────────────────────────────────────────
// Use Vite proxy (`vite.config.js`) so the app works without CORS issues.
const API_BASE = "/api";

const api = {
  async request(path, options = {}) {
    const token = localStorage.getItem("token");
    const res = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
      },
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || "Request failed");
    return data;
  },
  get:    (path)         => api.request(path),
  post:   (path, body)   => api.request(path, { method: "POST",   body: JSON.stringify(body) }),
  put:    (path, body)   => api.request(path, { method: "PUT",    body: JSON.stringify(body) }),
  patch:  (path, body)   => api.request(path, { method: "PATCH",  body: JSON.stringify(body) }),
  delete: (path)         => api.request(path, { method: "DELETE" }),
};

// ─── Auth Context ─────────────────────────────────────────────────────────────
const AuthContext = createContext(null);
const useAuth = () => useContext(AuthContext);

function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem("user");
    return saved ? JSON.parse(saved) : null;
  });

  const login = (userData) => {
    localStorage.setItem("token", userData.token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// ─── Design Tokens ────────────────────────────────────────────────────────────
const styles = `
  @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;1,9..40,300&display=swap');

  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :root {
    --bg:        #0a0a0f;
    --bg2:       #111118;
    --bg3:       #1a1a24;
    --border:    #2a2a3a;
    --border2:   #3a3a4e;
    --text:      #e8e8f0;
    --text2:     #9090a8;
    --text3:     #5a5a72;
    --accent:    #6ee7b7;
    --accent2:   #34d399;
    --accent3:   #059669;
    --amber:     #fbbf24;
    --red:       #f87171;
    --blue:      #60a5fa;
    --purple:    #a78bfa;
    --radius:    12px;
    --radius2:   8px;
    --shadow:    0 4px 24px rgba(0,0,0,0.4);
    --shadow2:   0 2px 12px rgba(0,0,0,0.3);
    --font-head: 'Syne', sans-serif;
    --font-body: 'DM Sans', sans-serif;
    --transition: all 0.2s cubic-bezier(0.4,0,0.2,1);
  }

  html { scroll-behavior: smooth; }

  body {
    font-family: var(--font-body);
    background: var(--bg);
    color: var(--text);
    min-height: 100vh;
    line-height: 1.6;
    -webkit-font-smoothing: antialiased;
  }

  /* ── Layout ── */
  .app-shell { display: flex; min-height: 100vh; }

  .sidebar {
    width: 240px; flex-shrink: 0;
    background: var(--bg2);
    border-right: 1px solid var(--border);
    display: flex; flex-direction: column;
    position: fixed; top: 0; left: 0; bottom: 0;
    z-index: 100; transition: var(--transition);
  }
  .sidebar-logo {
    padding: 28px 24px 20px;
    border-bottom: 1px solid var(--border);
  }
  .logo-text {
    font-family: var(--font-head);
    font-size: 22px; font-weight: 800;
    letter-spacing: -0.5px;
    background: linear-gradient(135deg, var(--accent), var(--blue));
    -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  }
  .logo-sub { font-size: 11px; color: var(--text3); margin-top: 2px; letter-spacing: 0.5px; }

  .sidebar-nav { flex: 1; padding: 16px 12px; overflow-y: auto; }
  .nav-section-label {
    font-size: 10px; font-weight: 600; letter-spacing: 1.5px;
    color: var(--text3); text-transform: uppercase;
    padding: 12px 12px 6px;
  }
  .nav-item {
    display: flex; align-items: center; gap: 10px;
    padding: 10px 12px; border-radius: var(--radius2);
    cursor: pointer; transition: var(--transition);
    color: var(--text2); font-size: 14px; font-weight: 400;
    border: none; background: none; width: 100%; text-align: left;
    margin-bottom: 2px;
  }
  .nav-item:hover { background: var(--bg3); color: var(--text); }
  .nav-item.active {
    background: linear-gradient(135deg, rgba(110,231,183,0.15), rgba(96,165,250,0.1));
    color: var(--accent); font-weight: 500;
    border: 1px solid rgba(110,231,183,0.2);
  }
  .nav-item .nav-icon { font-size: 16px; width: 20px; text-align: center; flex-shrink: 0; }

  .sidebar-footer {
    padding: 16px 12px;
    border-top: 1px solid var(--border);
  }
  .user-card {
    display: flex; align-items: center; gap: 10px;
    padding: 10px 12px; border-radius: var(--radius2);
    background: var(--bg3); cursor: pointer;
    transition: var(--transition);
  }
  .user-card:hover { border-color: var(--border2); }
  .user-avatar {
    width: 32px; height: 32px; border-radius: 50%;
    background: linear-gradient(135deg, var(--accent3), var(--blue));
    display: flex; align-items: center; justify-content: center;
    font-family: var(--font-head); font-weight: 700; font-size: 13px; color: white;
    flex-shrink: 0;
  }
  .user-info { flex: 1; min-width: 0; }
  .user-name { font-size: 13px; font-weight: 500; color: var(--text); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
  .user-role { font-size: 11px; color: var(--text3); }

  .main-content {
    margin-left: 240px; flex: 1;
    padding: 32px;
    min-height: 100vh;
    background: var(--bg);
  }

  /* ── Page Header ── */
  .page-header { margin-bottom: 32px; }
  .page-title {
    font-family: var(--font-head);
    font-size: 28px; font-weight: 800;
    letter-spacing: -0.5px; color: var(--text);
    margin-bottom: 4px;
  }
  .page-sub { color: var(--text2); font-size: 14px; }

  /* ── Cards ── */
  .card {
    background: var(--bg2);
    border: 1px solid var(--border);
    border-radius: var(--radius);
    padding: 24px;
    transition: var(--transition);
  }
  .card:hover { border-color: var(--border2); }
  .card-sm { padding: 16px; }

  /* ── Stat Cards ── */
  .stats-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; margin-bottom: 28px; }
  .stat-card {
    background: var(--bg2); border: 1px solid var(--border);
    border-radius: var(--radius); padding: 20px;
    transition: var(--transition); position: relative; overflow: hidden;
  }
  .stat-card::before {
    content: ''; position: absolute;
    top: 0; left: 0; right: 0; height: 2px;
  }
  .stat-card.green::before  { background: linear-gradient(90deg, var(--accent), var(--accent2)); }
  .stat-card.blue::before   { background: linear-gradient(90deg, var(--blue), var(--purple)); }
  .stat-card.amber::before  { background: linear-gradient(90deg, var(--amber), #f97316); }
  .stat-card.red::before    { background: linear-gradient(90deg, var(--red), #fb923c); }
  .stat-card.purple::before { background: linear-gradient(90deg, var(--purple), var(--blue)); }
  .stat-label { font-size: 12px; color: var(--text3); font-weight: 500; letter-spacing: 0.5px; text-transform: uppercase; margin-bottom: 10px; }
  .stat-value { font-family: var(--font-head); font-size: 32px; font-weight: 800; color: var(--text); line-height: 1; }
  .stat-sub { font-size: 12px; color: var(--text3); margin-top: 6px; }

  /* ── Grids ── */
  .grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
  .grid-3 { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
  @media (max-width: 900px) {
    .grid-2, .grid-3 { grid-template-columns: 1fr; }
    .sidebar { transform: translateX(-100%); }
    .main-content { margin-left: 0; }
  }

  /* ── Progress Bar ── */
  .progress-bar-wrap { background: var(--bg3); border-radius: 99px; height: 6px; overflow: hidden; }
  .progress-bar-fill {
    height: 100%; border-radius: 99px;
    background: linear-gradient(90deg, var(--accent3), var(--accent));
    transition: width 0.8s cubic-bezier(0.4,0,0.2,1);
  }
  .progress-bar-fill.amber { background: linear-gradient(90deg, #d97706, var(--amber)); }
  .progress-bar-fill.red   { background: linear-gradient(90deg, #dc2626, var(--red)); }

  /* ── Badges / Chips ── */
  .badge {
    display: inline-flex; align-items: center;
    padding: 3px 10px; border-radius: 99px;
    font-size: 11px; font-weight: 600; letter-spacing: 0.3px;
  }
  .badge-green  { background: rgba(110,231,183,0.12); color: var(--accent); border: 1px solid rgba(110,231,183,0.2); }
  .badge-blue   { background: rgba(96,165,250,0.12);  color: var(--blue);   border: 1px solid rgba(96,165,250,0.2);  }
  .badge-amber  { background: rgba(251,191,36,0.12);  color: var(--amber);  border: 1px solid rgba(251,191,36,0.2);  }
  .badge-red    { background: rgba(248,113,113,0.12); color: var(--red);    border: 1px solid rgba(248,113,113,0.2); }
  .badge-purple { background: rgba(167,139,250,0.12); color: var(--purple); border: 1px solid rgba(167,139,250,0.2); }
  .badge-gray   { background: rgba(144,144,168,0.12); color: var(--text2);  border: 1px solid rgba(144,144,168,0.2); }

  /* ── Buttons ── */
  .btn {
    display: inline-flex; align-items: center; gap: 8px;
    padding: 10px 18px; border-radius: var(--radius2);
    font-family: var(--font-body); font-size: 14px; font-weight: 500;
    cursor: pointer; border: none; transition: var(--transition);
    text-decoration: none; white-space: nowrap;
  }
  .btn-primary {
    background: linear-gradient(135deg, var(--accent3), var(--accent2));
    color: #0a0a0f;
    box-shadow: 0 0 20px rgba(52,211,153,0.25);
  }
  .btn-primary:hover { transform: translateY(-1px); box-shadow: 0 0 28px rgba(52,211,153,0.35); }
  .btn-secondary {
    background: var(--bg3); color: var(--text);
    border: 1px solid var(--border);
  }
  .btn-secondary:hover { border-color: var(--border2); background: var(--border); }
  .btn-danger { background: rgba(248,113,113,0.15); color: var(--red); border: 1px solid rgba(248,113,113,0.25); }
  .btn-danger:hover { background: rgba(248,113,113,0.25); }
  .btn-sm { padding: 6px 14px; font-size: 13px; }
  .btn-xs { padding: 4px 10px; font-size: 12px; }
  .btn:disabled { opacity: 0.5; cursor: not-allowed; transform: none !important; }

  /* ── Forms ── */
  .form-group { margin-bottom: 20px; }
  .form-label { display: block; font-size: 13px; font-weight: 500; color: var(--text2); margin-bottom: 8px; }
  .form-input, .form-select, .form-textarea {
    width: 100%; padding: 11px 14px;
    background: var(--bg3); border: 1px solid var(--border);
    border-radius: var(--radius2); color: var(--text);
    font-family: var(--font-body); font-size: 14px;
    transition: var(--transition); outline: none;
  }
  .form-input:focus, .form-select:focus, .form-textarea:focus {
    border-color: var(--accent3);
    box-shadow: 0 0 0 3px rgba(5,150,105,0.15);
  }
  .form-select option { background: var(--bg3); }
  .form-textarea { resize: vertical; min-height: 80px; }
  .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
  .form-error { font-size: 12px; color: var(--red); margin-top: 6px; }

  /* ── Table ── */
  .table-wrap { overflow-x: auto; border-radius: var(--radius); }
  table { width: 100%; border-collapse: collapse; }
  thead tr { border-bottom: 1px solid var(--border); }
  th { padding: 12px 16px; font-size: 12px; font-weight: 600; color: var(--text3); text-align: left; text-transform: uppercase; letter-spacing: 0.5px; white-space: nowrap; }
  td { padding: 14px 16px; font-size: 14px; color: var(--text2); border-bottom: 1px solid var(--border); }
  tbody tr { transition: var(--transition); }
  tbody tr:last-child td { border-bottom: none; }
  tbody tr:hover td { background: var(--bg3); color: var(--text); }

  /* ── Auth page ── */
  .auth-page {
    min-height: 100vh; display: flex; align-items: center; justify-content: center;
    background: var(--bg);
    background-image: radial-gradient(ellipse 60% 50% at 50% -20%, rgba(110,231,183,0.08), transparent);
  }
  .auth-box {
    width: 100%; max-width: 420px;
    background: var(--bg2); border: 1px solid var(--border);
    border-radius: 16px; padding: 40px;
    box-shadow: var(--shadow);
  }
  .auth-logo {
    font-family: var(--font-head); font-size: 26px; font-weight: 800;
    background: linear-gradient(135deg, var(--accent), var(--blue));
    -webkit-background-clip: text; -webkit-text-fill-color: transparent;
    margin-bottom: 6px;
  }
  .auth-tagline { color: var(--text3); font-size: 13px; margin-bottom: 32px; }
  .auth-title { font-family: var(--font-head); font-size: 20px; font-weight: 700; margin-bottom: 24px; }
  .auth-switch { text-align: center; margin-top: 20px; font-size: 13px; color: var(--text3); }
  .auth-switch button { background: none; border: none; color: var(--accent); cursor: pointer; font-size: 13px; font-weight: 500; }

  /* ── Skill & roadmap items ── */
  .skill-item {
    background: var(--bg2); border: 1px solid var(--border);
    border-radius: var(--radius); padding: 18px 20px;
    transition: var(--transition);
  }
  .skill-item:hover { border-color: var(--border2); }
  .skill-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; }
  .skill-name { font-weight: 500; font-size: 14px; color: var(--text); }
  .skill-meta { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
  .skill-stats { display: flex; gap: 16px; margin-top: 10px; }
  .skill-stat { font-size: 12px; color: var(--text3); }
  .skill-stat span { color: var(--text); font-weight: 500; }

  .roadmap-step {
    background: var(--bg2); border: 1px solid var(--border);
    border-radius: var(--radius); padding: 20px;
    display: flex; gap: 16px; transition: var(--transition);
    margin-bottom: 12px;
  }
  .roadmap-step.completed { opacity: 0.6; }
  .roadmap-step:hover { border-color: var(--border2); }
  .step-num {
    width: 32px; height: 32px; border-radius: 50%; flex-shrink: 0;
    background: var(--bg3); border: 2px solid var(--border2);
    display: flex; align-items: center; justify-content: center;
    font-family: var(--font-head); font-weight: 700; font-size: 12px; color: var(--text2);
  }
  .step-num.done { background: rgba(52,211,153,0.15); border-color: var(--accent); color: var(--accent); }
  .step-body { flex: 1; }
  .step-title { font-weight: 600; font-size: 14px; color: var(--text); margin-bottom: 4px; }
  .step-desc { font-size: 13px; color: var(--text2); margin-bottom: 10px; }
  .step-footer { display: flex; align-items: center; gap: 12px; }

  .rec-item {
    background: var(--bg2); border: 1px solid var(--border);
    border-radius: var(--radius); padding: 18px 20px;
    display: flex; align-items: flex-start; gap: 14px;
    transition: var(--transition); margin-bottom: 10px;
  }
  .rec-item:hover { border-color: var(--border2); }
  .rec-priority { width: 4px; border-radius: 99px; flex-shrink: 0; align-self: stretch; }
  .rec-priority.HIGH   { background: var(--red); }
  .rec-priority.MEDIUM { background: var(--amber); }
  .rec-priority.LOW    { background: var(--accent); }
  .rec-body { flex: 1; }
  .rec-skill { font-weight: 600; font-size: 14px; color: var(--text); margin-bottom: 4px; }
  .rec-reason { font-size: 13px; color: var(--text2); margin-bottom: 10px; }
  .rec-meta { display: flex; gap: 12px; align-items: center; }
  .rec-progress-mini { flex: 1; }

  /* ── Modal ── */
  .modal-overlay {
    position: fixed; inset: 0; background: rgba(0,0,0,0.7);
    backdrop-filter: blur(4px); z-index: 1000;
    display: flex; align-items: center; justify-content: center;
    padding: 20px; animation: fadeIn 0.2s ease;
  }
  .modal-box {
    background: var(--bg2); border: 1px solid var(--border2);
    border-radius: 16px; padding: 32px;
    width: 100%; max-width: 480px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.6);
    animation: slideUp 0.25s cubic-bezier(0.4,0,0.2,1);
  }
  .modal-title { font-family: var(--font-head); font-size: 20px; font-weight: 700; margin-bottom: 24px; }
  .modal-footer { display: flex; gap: 12px; justify-content: flex-end; margin-top: 24px; }

  /* ── Toast ── */
  .toast-container { position: fixed; bottom: 24px; right: 24px; z-index: 2000; display: flex; flex-direction: column; gap: 10px; }
  .toast {
    background: var(--bg2); border: 1px solid var(--border2);
    border-radius: var(--radius2); padding: 14px 18px;
    font-size: 13px; font-weight: 500; color: var(--text);
    box-shadow: var(--shadow); min-width: 260px;
    animation: slideUp 0.25s ease;
    display: flex; align-items: center; gap: 10px;
  }
  .toast.success { border-left: 3px solid var(--accent); }
  .toast.error   { border-left: 3px solid var(--red); }

  /* ── Empty state ── */
  .empty-state {
    text-align: center; padding: 60px 20px;
    color: var(--text3);
  }
  .empty-icon { font-size: 48px; margin-bottom: 16px; opacity: 0.5; }
  .empty-title { font-family: var(--font-head); font-size: 18px; color: var(--text2); margin-bottom: 8px; }
  .empty-sub { font-size: 13px; }

  /* ── Loading ── */
  .spinner {
    width: 20px; height: 20px; border-radius: 50%;
    border: 2px solid var(--border2);
    border-top-color: var(--accent);
    animation: spin 0.8s linear infinite;
    display: inline-block;
  }
  .loading-page {
    display: flex; align-items: center; justify-content: center;
    min-height: 300px; flex-direction: column; gap: 16px;
    color: var(--text3); font-size: 14px;
  }

  /* ── Activity log form ── */
  .log-form { display: flex; gap: 12px; align-items: flex-end; }
  .log-form .form-group { margin-bottom: 0; flex: 1; }

  /* ── Section title ── */
  .section-title {
    font-family: var(--font-head); font-size: 16px; font-weight: 700;
    color: var(--text); margin-bottom: 16px;
    display: flex; align-items: center; gap: 10px;
  }
  .section-title .line {
    flex: 1; height: 1px; background: var(--border);
  }

  /* ── Difficulty colors ── */
  .diff-BEGINNER     { color: var(--accent); }
  .diff-INTERMEDIATE { color: var(--amber); }
  .diff-ADVANCED     { color: var(--red); }

  /* ── Animations ── */
  @keyframes fadeIn  { from { opacity: 0; }           to { opacity: 1; } }
  @keyframes slideUp { from { transform: translateY(16px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
  @keyframes spin    { to { transform: rotate(360deg); } }

  .fade-in { animation: fadeIn 0.4s ease both; }
  .slide-up { animation: slideUp 0.4s cubic-bezier(0.4,0,0.2,1) both; }
`;

// ─── Toast System ─────────────────────────────────────────────────────────────
let toastId = 0;
let setToastsGlobal = null;

function ToastContainer() {
  const [toasts, setToasts] = useState([]);
  setToastsGlobal = setToasts;

  useEffect(() => {
    if (toasts.length === 0) return;
    const t = setTimeout(() => {
      setToasts(prev => prev.slice(1));
    }, 3500);
    return () => clearTimeout(t);
  }, [toasts]);

  return (
    <div className="toast-container">
      {toasts.map(t => (
        <div key={t.id} className={`toast ${t.type}`}>
          <span>{t.type === "success" ? "✓" : "✕"}</span>
          {t.msg}
        </div>
      ))}
    </div>
  );
}

const toast = {
  success: (msg) => setToastsGlobal?.(p => [...p, { id: ++toastId, msg, type: "success" }]),
  error:   (msg) => setToastsGlobal?.(p => [...p, { id: ++toastId, msg, type: "error" }]),
};

// ─── Helpers ──────────────────────────────────────────────────────────────────
const fmtDate = (s) => {
  if (!s) return "Never";
  try { return new Date(s).toLocaleDateString("en-IN", { day: "2-digit", month: "short", year: "numeric" }); }
  catch { return s; }
};

const priorityBadge = (p) => {
  if (p === "HIGH")   return <span className="badge badge-red">HIGH</span>;
  if (p === "MEDIUM") return <span className="badge badge-amber">MEDIUM</span>;
  return <span className="badge badge-green">LOW</span>;
};

const diffBadge = (d) => (
  <span className={`badge ${d === "BEGINNER" ? "badge-green" : d === "INTERMEDIATE" ? "badge-amber" : "badge-red"}`}>{d}</span>
);

// ─── Auth Page ────────────────────────────────────────────────────────────────
function AuthPage() {
  const { login } = useAuth();
  const [isLogin, setIsLogin] = useState(true);
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const submit = async (e) => {
    e.preventDefault();
    setError(""); setLoading(true);
    try {
      if (isLogin) {
        const res = await api.post("/auth/login", { email: form.email, password: form.password });
        login(res.data);
        toast.success("Welcome back!");
      } else {
        await api.post("/auth/register", form);
        toast.success("Account created! Please login.");
        setIsLogin(true);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-box slide-up">
        <div className="auth-logo">SkillSync</div>
        <div className="auth-tagline">Track your developer journey</div>
        <div className="auth-title">{isLogin ? "Sign in" : "Create account"}</div>
        <form onSubmit={submit}>
          {!isLogin && (
            <div className="form-group">
              <label className="form-label">Full Name</label>
              <input className="form-input" placeholder="Khushwant Suthar" value={form.name} onChange={set("name")} required />
            </div>
          )}
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" placeholder="you@example.com" value={form.email} onChange={set("email")} required />
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input className="form-input" type="password" placeholder="••••••••" value={form.password} onChange={set("password")} required />
          </div>
          {error && <div className="form-error" style={{ marginBottom: 16 }}>{error}</div>}
          <button className="btn btn-primary" style={{ width: "100%" }} disabled={loading}>
            {loading ? <span className="spinner" /> : null}
            {isLogin ? "Sign in" : "Create account"}
          </button>
        </form>
        <div className="auth-switch">
          {isLogin ? "No account?" : "Already registered?"}{" "}
          <button onClick={() => { setIsLogin(!isLogin); setError(""); }}>
            {isLogin ? "Register" : "Sign in"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ─── Dashboard Page ───────────────────────────────────────────────────────────
function DashboardPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get("/dashboard").then(r => { setData(r.data); setLoading(false); }).catch(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading-page"><div className="spinner" /><span>Loading dashboard…</span></div>;
  if (!data)   return <div className="empty-state"><div className="empty-icon">📊</div><div className="empty-title">No dashboard data yet</div><div className="empty-sub">Log some activities to get started</div></div>;

  const stats = [
    { label: "Skills Tracked",    value: data.totalSkillsTracked, color: "blue",   sub: `${data.skillsMastered} mastered` },
    { label: "Average Progress",  value: `${data.averageProgress}%`, color: "green", sub: `${data.skillsInProgress} in progress` },
    { label: "Total Score",       value: Math.round(data.totalScore), color: "purple", sub: "points earned" },
    { label: "Minutes Practiced", value: data.totalMinutesPracticed, color: "amber", sub: `${data.totalActivitiesLogged} sessions` },
    { label: "Top Skill",         value: data.topSkillName, color: "green", sub: `${data.topSkillProgress}% complete`, small: true },
  ];

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Dashboard</div>
        <div className="page-sub">Welcome back, {data.userName} 👋</div>
      </div>

      <div className="stats-grid">
        {stats.map((s, i) => (
          <div key={i} className={`stat-card ${s.color}`}>
            <div className="stat-label">{s.label}</div>
            <div className="stat-value" style={s.small ? { fontSize: 18, paddingTop: 6 } : {}}>{s.value}</div>
            <div className="stat-sub">{s.sub}</div>
          </div>
        ))}
      </div>

      <div className="section-title">Skill Breakdown <div className="line" /></div>
      <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
        {data.skillBreakdown?.map((s, i) => (
          <div key={i} className="skill-item">
            <div className="skill-header">
              <span className="skill-name">{s.skillName}</span>
              <span style={{ fontSize: 13, color: "var(--text2)", fontWeight: 600 }}>{s.progressPercentage}%</span>
            </div>
            <div className="progress-bar-wrap">
              <div className="progress-bar-fill" style={{ width: `${s.progressPercentage}%` }} />
            </div>
            <div className="skill-stats">
              <div className="skill-stat">Score: <span>{s.score}</span></div>
              <div className="skill-stat">Last practiced: <span>{fmtDate(s.lastPracticedAt)}</span></div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

// ─── Progress Page ────────────────────────────────────────────────────────────
function ProgressPage() {
  const [progress, setProgress] = useState([]);
  const [skills,   setSkills]   = useState([]);
  const [loading,  setLoading]  = useState(true);
  const [logForm,  setLogForm]  = useState({ skillId: "", minutes: "" });
  const [logging,  setLogging]  = useState(false);

  const load = useCallback(() => {
    Promise.all([
      api.get("/progress/me"),
      api.get("/skills/getAllSkills?size=100"),
    ]).then(([pRes, sRes]) => {
      setProgress(pRes.data || []);
      setSkills(sRes.data?.content || []);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  useEffect(() => { load(); }, [load]);

  const logActivity = async (e) => {
    e.preventDefault();
    if (!logForm.skillId || !logForm.minutes) return;
    setLogging(true);
    try {
      await api.post("/activities/logBook", {
        skillId: Number(logForm.skillId),
        timeSpentMinutes: Number(logForm.minutes),
      });
      toast.success("Activity logged! Progress updated.");
      setLogForm({ skillId: "", minutes: "" });
      load();
    } catch (err) {
      toast.error(err.message);
    } finally {
      setLogging(false);
    }
  };

  if (loading) return <div className="loading-page"><div className="spinner" /><span>Loading progress…</span></div>;

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Skill Progress</div>
        <div className="page-sub">Track your learning sessions</div>
      </div>

      {/* Log Activity Form */}
      <div className="card" style={{ marginBottom: 28 }}>
        <div className="section-title" style={{ marginBottom: 16, fontSize: 14 }}>Log a Practice Session</div>
        <form className="log-form" onSubmit={logActivity}>
          <div className="form-group" style={{ flex: 2 }}>
            <label className="form-label">Skill</label>
            <select className="form-select" value={logForm.skillId}
              onChange={e => setLogForm(f => ({ ...f, skillId: e.target.value }))} required>
              <option value="">Select skill…</option>
              {skills.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
          <div className="form-group" style={{ width: 140 }}>
            <label className="form-label">Minutes (1–1440)</label>
            <input className="form-input" type="number" min="1" max="1440"
              placeholder="60" value={logForm.minutes}
              onChange={e => setLogForm(f => ({ ...f, minutes: e.target.value }))} required />
          </div>
          <button className="btn btn-primary" type="submit" disabled={logging} style={{ marginBottom: 0 }}>
            {logging ? <span className="spinner" /> : "⚡ Log"}
          </button>
        </form>
      </div>

      {/* Progress List */}
      {progress.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🎯</div>
          <div className="empty-title">No progress recorded yet</div>
          <div className="empty-sub">Log your first activity above to start tracking</div>
        </div>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {progress.map((p, i) => (
            <div key={i} className="skill-item">
              <div className="skill-header">
                <span className="skill-name">{p.skillName}</span>
                <span style={{ fontFamily: "var(--font-head)", fontSize: 20, fontWeight: 800,
                  color: p.progressPercentage >= 70 ? "var(--accent)" : p.progressPercentage >= 30 ? "var(--amber)" : "var(--red)" }}>
                  {p.progressPercentage}%
                </span>
              </div>
              <div className="progress-bar-wrap" style={{ marginBottom: 10 }}>
                <div className={`progress-bar-fill ${p.progressPercentage < 30 ? "red" : p.progressPercentage < 70 ? "amber" : ""}`}
                  style={{ width: `${p.progressPercentage}%` }} />
              </div>
              <div className="skill-stats">
                <div className="skill-stat">Score: <span>{p.score}</span></div>
                <div className="skill-stat">Last practiced: <span>{fmtDate(p.lastPracticedAt)}</span></div>
                {p.progressPercentage >= 100 && <span className="badge badge-green">✓ Mastered</span>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// ─── Recommendations Page ─────────────────────────────────────────────────────
function RecommendationsPage() {
  const [recs, setRecs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get("/recommendations")
      .then(r => { setRecs(r.data || []); setLoading(false); })
      .catch(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading-page"><div className="spinner" /><span>Generating recommendations…</span></div>;

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Recommendations</div>
        <div className="page-sub">Personalised learning suggestions based on your progress</div>
      </div>

      {recs.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">💡</div>
          <div className="empty-title">No recommendations yet</div>
          <div className="empty-sub">Start tracking skills to get personalised suggestions</div>
        </div>
      ) : (
        <div>
          {recs.map((r, i) => (
            <div key={i} className="rec-item">
              <div className={`rec-priority ${r.priority}`} />
              <div className="rec-body">
                <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 4 }}>
                  <div className="rec-skill">{r.skillName}</div>
                  {priorityBadge(r.priority)}
                </div>
                <div className="rec-reason">{r.reason}</div>
                <div className="rec-meta">
                  <div className="progress-bar-wrap rec-progress-mini">
                    <div className="progress-bar-fill" style={{ width: `${r.currentProgress}%` }} />
                  </div>
                  <span style={{ fontSize: 12, color: "var(--text2)", whiteSpace: "nowrap" }}>
                    {r.currentProgress}% · Last: {fmtDate(r.lastPracticedAt)}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// ─── Roadmap Page ─────────────────────────────────────────────────────────────
function RoadmapPage() {
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);
  const [dailyMins, setDailyMins] = useState(60);

  const loadRoadmap = () => {
    setLoading(true);
    api.get("/roadmap")
      .then(r => { setRoadmap(r.data); setLoading(false); })
      .catch(() => setLoading(false));
  };

  useEffect(() => { loadRoadmap(); }, []);

  const generate = async () => {
    setGenerating(true);
    try {
      const r = await api.post("/roadmap/generate", { dailyMinutesAvailable: Number(dailyMins) });
      setRoadmap(r.data);
      toast.success("Roadmap generated!");
    } catch (err) {
      toast.error(err.message);
    } finally {
      setGenerating(false);
    }
  };

  const completeStep = async (stepId) => {
    try {
      await api.patch(`/roadmap/steps/${stepId}/complete`);
      toast.success("Step completed! 🎉");
      loadRoadmap();
    } catch (err) {
      toast.error(err.message);
    }
  };

  if (loading) return <div className="loading-page"><div className="spinner" /><span>Loading roadmap…</span></div>;

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Learning Roadmap</div>
        <div className="page-sub">Your personalised step-by-step learning plan</div>
      </div>

      {/* Generate controls */}
      <div className="card" style={{ marginBottom: 28 }}>
        <div style={{ display: "flex", alignItems: "flex-end", gap: 12, flexWrap: "wrap" }}>
          <div className="form-group" style={{ margin: 0 }}>
            <label className="form-label">Daily available minutes</label>
            <input className="form-input" type="number" min="10" max="480"
              value={dailyMins} onChange={e => setDailyMins(e.target.value)}
              style={{ width: 160 }} />
          </div>
          <button className="btn btn-primary" onClick={generate} disabled={generating}>
            {generating ? <span className="spinner" /> : "🗺️"} Regenerate Roadmap
          </button>
          {roadmap && (
            <div style={{ marginLeft: "auto", textAlign: "right" }}>
              <div style={{ fontSize: 12, color: "var(--text3)", marginBottom: 4 }}>
                {roadmap.completedSteps}/{roadmap.totalSteps} steps completed
              </div>
              <div className="progress-bar-wrap" style={{ width: 180 }}>
                <div className="progress-bar-fill" style={{ width: `${roadmap.overallProgressPercent}%` }} />
              </div>
            </div>
          )}
        </div>
      </div>

      {!roadmap || !roadmap.steps?.length ? (
        <div className="empty-state">
          <div className="empty-icon">🗺️</div>
          <div className="empty-title">No roadmap yet</div>
          <div className="empty-sub">Log some activities first, then generate your roadmap</div>
        </div>
      ) : (
        <div>
          {roadmap.steps.map((step, i) => (
            <div key={step.stepId} className={`roadmap-step ${step.completed ? "completed" : ""}`}>
              <div className={`step-num ${step.completed ? "done" : ""}`}>
                {step.completed ? "✓" : step.stepOrder}
              </div>
              <div className="step-body">
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                  <div className="step-title">{step.title}</div>
                  <span className="badge badge-gray" style={{ marginLeft: 8, whiteSpace: "nowrap" }}>
                    ~{step.estimatedDays}d
                  </span>
                </div>
                <div className="step-desc">{step.description}</div>
                <div className="step-footer">
                  <span className="badge badge-blue">{step.skillName}</span>
                  {!step.completed && (
                    <button className="btn btn-sm btn-secondary" onClick={() => completeStep(step.stepId)}>
                      Mark Complete ✓
                    </button>
                  )}
                  {step.completed && <span className="badge badge-green">Completed ✓</span>}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// ─── Profile Page ─────────────────────────────────────────────────────────────
function ProfilePage() {
  const [profile, setProfile] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [name, setName] = useState("");
  const [pwForm, setPwForm] = useState({ currentPassword: "", newPassword: "", confirmPassword: "" });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [changingPw, setChangingPw] = useState(false);

  useEffect(() => {
    api.get("/user/profile")
      .then(r => { setProfile(r.data); setName(r.data.name); setLoading(false); })
      .catch(() => setLoading(false));
  }, []);

  const saveProfile = async () => {
    setSaving(true);
    try {
      const r = await api.put("/user/profile", { name });
      setProfile(r.data); setEditMode(false);
      toast.success("Profile updated!");
    } catch (err) { toast.error(err.message); }
    finally { setSaving(false); }
  };

  const changePassword = async (e) => {
    e.preventDefault();
    setChangingPw(true);
    try {
      await api.put("/user/change-password", pwForm);
      toast.success("Password changed!");
      setPwForm({ currentPassword: "", newPassword: "", confirmPassword: "" });
    } catch (err) { toast.error(err.message); }
    finally { setChangingPw(false); }
  };

  if (loading) return <div className="loading-page"><div className="spinner" /></div>;

  return (
    <div className="fade-in" style={{ maxWidth: 640 }}>
      <div className="page-header">
        <div className="page-title">My Profile</div>
        <div className="page-sub">Manage your account details</div>
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <div style={{ display: "flex", alignItems: "center", gap: 20, marginBottom: 24 }}>
          <div className="user-avatar" style={{ width: 56, height: 56, fontSize: 22 }}>
            {profile?.name?.[0]?.toUpperCase()}
          </div>
          <div>
            <div style={{ fontFamily: "var(--font-head)", fontSize: 20, fontWeight: 700 }}>{profile?.name}</div>
            <div style={{ fontSize: 13, color: "var(--text3)" }}>{profile?.email}</div>
            <div style={{ marginTop: 6 }}>
              <span className={`badge ${profile?.role === "ADMIN" ? "badge-purple" : "badge-blue"}`}>{profile?.role}</span>
            </div>
          </div>
        </div>

        {editMode ? (
          <div>
            <div className="form-group">
              <label className="form-label">Display Name</label>
              <input className="form-input" value={name} onChange={e => setName(e.target.value)} />
            </div>
            <div style={{ display: "flex", gap: 10 }}>
              <button className="btn btn-primary btn-sm" onClick={saveProfile} disabled={saving}>
                {saving ? <span className="spinner" /> : "Save"}
              </button>
              <button className="btn btn-secondary btn-sm" onClick={() => { setEditMode(false); setName(profile.name); }}>Cancel</button>
            </div>
          </div>
        ) : (
          <button className="btn btn-secondary btn-sm" onClick={() => setEditMode(true)}>✏️ Edit Name</button>
        )}
      </div>

      <div className="card">
        <div className="section-title" style={{ marginBottom: 20, fontSize: 14 }}>Change Password</div>
        <form onSubmit={changePassword}>
          <div className="form-group">
            <label className="form-label">Current Password</label>
            <input className="form-input" type="password" value={pwForm.currentPassword}
              onChange={e => setPwForm(f => ({ ...f, currentPassword: e.target.value }))} required />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">New Password</label>
              <input className="form-input" type="password" value={pwForm.newPassword}
                onChange={e => setPwForm(f => ({ ...f, newPassword: e.target.value }))} required />
            </div>
            <div className="form-group">
              <label className="form-label">Confirm Password</label>
              <input className="form-input" type="password" value={pwForm.confirmPassword}
                onChange={e => setPwForm(f => ({ ...f, confirmPassword: e.target.value }))} required />
            </div>
          </div>
          <button className="btn btn-primary btn-sm" type="submit" disabled={changingPw}>
            {changingPw ? <span className="spinner" /> : "Update Password"}
          </button>
        </form>
      </div>
    </div>
  );
}

// ─── Admin — Skills Page ──────────────────────────────────────────────────────
function AdminSkillsPage() {
  const [skills, setSkills]       = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading]     = useState(true);
  const [search, setSearch]       = useState("");
  const [page, setPage]           = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm]           = useState({ name: "", categoryId: "", difficulty: "BEGINNER", description: "" });
  const [saving, setSaving]       = useState(false);

  const loadSkills = useCallback(() => {
    api.get(`/skills/getAllSkills?keyword=${search}&page=${page}&size=10&sortBy=name`)
      .then(r => {
        setSkills(r.data?.content || []);
        setTotalPages(r.data?.totalPages || 1);
        setLoading(false);
      }).catch(() => setLoading(false));
  }, [search, page]);

  useEffect(() => { loadSkills(); }, [loadSkills]);

  useEffect(() => {
    api.get("/categories/with-skills").then(r => setCategories(r.data || []));
  }, []);

  const createSkill = async (e) => {
    e.preventDefault(); setSaving(true);
    try {
      await api.post("/skills/createSkill", { ...form, categoryId: Number(form.categoryId) });
      toast.success(`Skill '${form.name}' created!`);
      setShowModal(false);
      setForm({ name: "", categoryId: "", difficulty: "BEGINNER", description: "" });
      loadSkills();
    } catch (err) { toast.error(err.message); }
    finally { setSaving(false); }
  };

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Skills Management</div>
        <div className="page-sub">Create and manage platform skills</div>
      </div>

      <div style={{ display: "flex", gap: 12, marginBottom: 20, alignItems: "center" }}>
        <input className="form-input" style={{ maxWidth: 280 }}
          placeholder="Search skills…" value={search}
          onChange={e => { setSearch(e.target.value); setPage(0); }} />
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ New Skill</button>
      </div>

      {loading ? (
        <div className="loading-page"><div className="spinner" /></div>
      ) : (
        <div className="card" style={{ padding: 0 }}>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Skill Name</th>
                  <th>Category</th>
                  <th>Difficulty</th>
                  <th>Description</th>
                </tr>
              </thead>
              <tbody>
                {skills.length === 0 ? (
                  <tr><td colSpan={4} style={{ textAlign: "center", color: "var(--text3)", padding: "40px 0" }}>No skills found</td></tr>
                ) : skills.map((s, i) => (
                  <tr key={i}>
                    <td style={{ fontWeight: 500, color: "var(--text)" }}>{s.name}</td>
                    <td>{s.categoryName}</td>
                    <td>{diffBadge(s.difficulty)}</td>
                    <td style={{ maxWidth: 280, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{s.description || "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {totalPages > 1 && (
            <div style={{ padding: "12px 16px", display: "flex", gap: 8, justifyContent: "flex-end", borderTop: "1px solid var(--border)" }}>
              <button className="btn btn-secondary btn-xs" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Prev</button>
              <span style={{ fontSize: 13, color: "var(--text2)", alignSelf: "center" }}>Page {page + 1} / {totalPages}</span>
              <button className="btn btn-secondary btn-xs" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next →</button>
            </div>
          )}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-box">
            <div className="modal-title">Create New Skill</div>
            <form onSubmit={createSkill}>
              <div className="form-group">
                <label className="form-label">Skill Name</label>
                <input className="form-input" placeholder="e.g. Dynamic Programming"
                  value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} required />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Category</label>
                  <select className="form-select" value={form.categoryId}
                    onChange={e => setForm(f => ({ ...f, categoryId: e.target.value }))} required>
                    <option value="">Select…</option>
                    {categories.map(c => <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Difficulty</label>
                  <select className="form-select" value={form.difficulty}
                    onChange={e => setForm(f => ({ ...f, difficulty: e.target.value }))}>
                    <option>BEGINNER</option>
                    <option>INTERMEDIATE</option>
                    <option>ADVANCED</option>
                  </select>
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Description</label>
                <textarea className="form-textarea" placeholder="Brief description…"
                  value={form.description} onChange={e => setForm(f => ({ ...f, description: e.target.value }))} />
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? <span className="spinner" /> : "Create Skill"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

// ─── Admin — Categories Page ──────────────────────────────────────────────────
function AdminCategoriesPage() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading]       = useState(true);
  const [showModal, setShowModal]   = useState(false);
  const [form, setForm]             = useState({ name: "", description: "" });
  const [saving, setSaving]         = useState(false);

  const load = () => {
    api.get("/categories/with-skills")
      .then(r => { setCategories(r.data || []); setLoading(false); })
      .catch(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const createCategory = async (e) => {
    e.preventDefault(); setSaving(true);
    try {
      await api.post("/categories/createCategory", form);
      toast.success(`Category '${form.name}' created!`);
      setShowModal(false); setForm({ name: "", description: "" }); load();
    } catch (err) { toast.error(err.message); }
    finally { setSaving(false); }
  };

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Categories</div>
        <div className="page-sub">Manage skill categories</div>
      </div>

      <div style={{ marginBottom: 20 }}>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ New Category</button>
      </div>

      {loading ? <div className="loading-page"><div className="spinner" /></div> : (
        <div className="grid-2">
          {categories.map((c, i) => (
            <div key={i} className="card">
              <div style={{ fontFamily: "var(--font-head)", fontSize: 16, fontWeight: 700, marginBottom: 6 }}>{c.categoryName}</div>
              <div style={{ fontSize: 13, color: "var(--text3)", marginBottom: 14 }}>
                {c.skills?.length || 0} skills
              </div>
              <div style={{ display: "flex", flexWrap: "wrap", gap: 6 }}>
                {c.skills?.map((s, j) => (
                  <span key={j} className="badge badge-gray">{s.name}</span>
                ))}
                {(!c.skills || c.skills.length === 0) && (
                  <span style={{ fontSize: 12, color: "var(--text3)" }}>No skills yet</span>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-box">
            <div className="modal-title">Create Category</div>
            <form onSubmit={createCategory}>
              <div className="form-group">
                <label className="form-label">Category Name</label>
                <input className="form-input" placeholder="e.g. Frontend Development"
                  value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} required />
              </div>
              <div className="form-group">
                <label className="form-label">Description</label>
                <textarea className="form-textarea" placeholder="Brief description…"
                  value={form.description} onChange={e => setForm(f => ({ ...f, description: e.target.value }))} />
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  {saving ? <span className="spinner" /> : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

// ─── Admin — Users Page ───────────────────────────────────────────────────────
function AdminUsersPage() {
  const [stats, setStats]   = useState(null);
  const [users, setUsers]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage]     = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const load = useCallback(() => {
    Promise.all([
      api.get("/admin/stats"),
      api.get(`/admin/users?page=${page}&size=10&sortBy=name`),
    ]).then(([sRes, uRes]) => {
      setStats(sRes.data);
      setUsers(uRes.data?.content || []);
      setTotalPages(uRes.data?.totalPages || 1);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, [page]);

  useEffect(() => { load(); }, [load]);

  const promote = async (id) => {
    if (!confirm("Promote this user to ADMIN?")) return;
    try {
      await api.patch(`/admin/users/${id}/promote`);
      toast.success("User promoted to Admin!");
      load();
    } catch (err) { toast.error(err.message); }
  };

  const deleteUser = async (id) => {
    if (!confirm("Delete this user permanently?")) return;
    try {
      await api.delete(`/admin/users/${id}`);
      toast.success("User deleted.");
      load();
    } catch (err) { toast.error(err.message); }
  };

  if (loading) return <div className="loading-page"><div className="spinner" /></div>;

  return (
    <div className="fade-in">
      <div className="page-header">
        <div className="page-title">Admin Panel</div>
        <div className="page-sub">Platform overview and user management</div>
      </div>

      {stats && (
        <div className="stats-grid" style={{ marginBottom: 28 }}>
          {[
            { label: "Total Users",       value: stats.totalUsers,          color: "blue" },
            { label: "Total Skills",      value: stats.totalSkills,         color: "green" },
            { label: "Categories",        value: stats.totalCategories,     color: "purple" },
            { label: "Activities Logged", value: stats.totalActivitiesLogged, color: "amber" },
            { label: "Mins Practiced",    value: stats.totalMinutesPracticed, color: "green" },
            { label: "Avg Progress",      value: `${stats.platformAverageProgress}%`, color: "blue" },
          ].map((s, i) => (
            <div key={i} className={`stat-card ${s.color}`}>
              <div className="stat-label">{s.label}</div>
              <div className="stat-value" style={{ fontSize: 26 }}>{s.value}</div>
            </div>
          ))}
        </div>
      )}

      <div className="section-title">All Users <div className="line" /></div>
      <div className="card" style={{ padding: 0 }}>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Skills</th>
                <th>Avg Progress</th>
                <th>Activities</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u, i) => (
                <tr key={i}>
                  <td style={{ fontWeight: 500, color: "var(--text)" }}>{u.name}</td>
                  <td>{u.email}</td>
                  <td><span className={`badge ${u.role === "ADMIN" ? "badge-purple" : "badge-blue"}`}>{u.role}</span></td>
                  <td>{u.skillsTracked}</td>
                  <td>
                    <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                      <div className="progress-bar-wrap" style={{ width: 60 }}>
                        <div className="progress-bar-fill" style={{ width: `${u.averageProgress}%` }} />
                      </div>
                      <span style={{ fontSize: 12 }}>{u.averageProgress}%</span>
                    </div>
                  </td>
                  <td>{u.totalActivities}</td>
                  <td>
                    <div style={{ display: "flex", gap: 6 }}>
                      {u.role !== "ADMIN" && (
                        <button className="btn btn-xs btn-secondary" onClick={() => promote(u.id)}>↑ Admin</button>
                      )}
                      <button className="btn btn-xs btn-danger" onClick={() => deleteUser(u.id)}>Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {totalPages > 1 && (
          <div style={{ padding: "12px 16px", display: "flex", gap: 8, justifyContent: "flex-end", borderTop: "1px solid var(--border)" }}>
            <button className="btn btn-secondary btn-xs" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Prev</button>
            <span style={{ fontSize: 13, color: "var(--text2)", alignSelf: "center" }}>Page {page + 1} / {totalPages}</span>
            <button className="btn btn-secondary btn-xs" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next →</button>
          </div>
        )}
      </div>
    </div>
  );
}

// ─── Sidebar ──────────────────────────────────────────────────────────────────
function Sidebar({ current, navigate }) {
  const { user, logout } = useAuth();
  const isAdmin = user?.role === "ADMIN";

  const userNav = [
    { id: "dashboard",       icon: "⬛", label: "Dashboard" },
    { id: "progress",        icon: "📈", label: "My Progress" },
    { id: "recommendations", icon: "💡", label: "Recommendations" },
    { id: "roadmap",         icon: "🗺️", label: "Roadmap" },
    { id: "profile",         icon: "👤", label: "Profile" },
  ];

  const adminNav = [
    { id: "admin-users",      icon: "👥", label: "Users" },
    { id: "admin-skills",     icon: "🎯", label: "Skills" },
    { id: "admin-categories", icon: "📂", label: "Categories" },
  ];

  return (
    <nav className="sidebar">
      <div className="sidebar-logo">
        <div className="logo-text">SkillSync</div>
        <div className="logo-sub">Developer Skill Tracker</div>
      </div>

      <div className="sidebar-nav">
        <div className="nav-section-label">Main</div>
        {userNav.map(n => (
          <button key={n.id} className={`nav-item ${current === n.id ? "active" : ""}`}
            onClick={() => navigate(n.id)}>
            <span className="nav-icon">{n.icon}</span>
            {n.label}
          </button>
        ))}

        {isAdmin && (
          <>
            <div className="nav-section-label" style={{ marginTop: 8 }}>Admin</div>
            {adminNav.map(n => (
              <button key={n.id} className={`nav-item ${current === n.id ? "active" : ""}`}
                onClick={() => navigate(n.id)}>
                <span className="nav-icon">{n.icon}</span>
                {n.label}
              </button>
            ))}
          </>
        )}
      </div>

      <div className="sidebar-footer">
        <div className="user-card" onClick={() => navigate("profile")}>
          <div className="user-avatar">{user?.email?.[0]?.toUpperCase()}</div>
          <div className="user-info">
            <div className="user-name">{user?.email}</div>
            <div className="user-role">{user?.role}</div>
          </div>
        </div>
        <button className="btn btn-secondary btn-sm" style={{ width: "100%", marginTop: 8 }} onClick={logout}>
          Sign out
        </button>
      </div>
    </nav>
  );
}

// ─── App Shell ────────────────────────────────────────────────────────────────
function AppShell() {
  const [page, setPage] = useState("dashboard");

  const pages = {
    dashboard:       <DashboardPage />,
    progress:        <ProgressPage />,
    recommendations: <RecommendationsPage />,
    roadmap:         <RoadmapPage />,
    profile:         <ProfilePage />,
    "admin-users":      <AdminUsersPage />,
    "admin-skills":     <AdminSkillsPage />,
    "admin-categories": <AdminCategoriesPage />,
  };

  return (
    <div className="app-shell">
      <Sidebar current={page} navigate={setPage} />
      <main className="main-content">
        {pages[page] || <DashboardPage />}
      </main>
    </div>
  );
}

// ─── Root ─────────────────────────────────────────────────────────────────────
function App() {
  const { user } = useAuth();

  return (
    <>
      <style>{styles}</style>
      <ToastContainer />
      {user ? <AppShell /> : <AuthPage />}
    </>
  );
}

// Wrap with provider at root
const WrappedApp = () => (
  <AuthProvider>
    <App />
  </AuthProvider>
);

export { WrappedApp as default };