# React Frontend — Vendor NDA Tracker

React + Vite frontend for the Vendor NDA Tracker. Connects to the Java Spring Boot backend which proxies to the Flask AI microservice.

---

## Stack
- React 18 + Vite
- Vanilla CSS (dark glassmorphism design)
- No external UI libraries (zero dependencies beyond React)

---

## Setup

### 1. Install dependencies
```bash
cd frontend
npm install
```

### 2. Configure environment
```bash
cp .env.example .env
# Edit VITE_API_URL if backend runs on a different port
```

### 3. Start dev server
```bash
npm run dev
```
App runs at: `http://localhost:5173`

---

## Pages

| Page | Route (state) | Description |
|------|---------------|-------------|
| Home | `home` | Landing page with feature overview |
| Describe | `describe` | Plain-language NDA summary |
| Recommend | `recommend` | Action items: Review / Negotiate / Flag |
| Risk Report | `report` | Full structured NDA risk assessment |

---

## API Integration

All calls go to the Java Spring Boot backend at `VITE_API_URL` (default `http://localhost:8080`).

| Frontend Action | Backend Endpoint |
|-----------------|-----------------|
| Describe NDA | `POST /vendor/describe` |
| Get Recommendations | `POST /vendor/recommend` |
| Generate Report | `POST /vendor/generate-report` |
| Health Check | `GET /actuator/health` |

---

## Project Structure

```
frontend/
├── .env.example
├── index.html
├── vite.config.js
└── src/
    ├── main.jsx
    ├── App.jsx
    ├── index.css
    ├── components/
    │   ├── Navbar.jsx
    │   └── Toast.jsx
    ├── pages/
    │   ├── HomePage.jsx
    │   ├── DescribePage.jsx
    │   ├── RecommendPage.jsx
    │   └── ReportPage.jsx
    └── services/
        └── api.js
```

---

## Design
- Dark glassmorphism theme
- Inter + JetBrains Mono fonts
- Electric indigo accent (#6366f1)
- Micro-animations and hover effects
- Fully responsive (mobile-first grid)
