# AI Service — Vendor NDA Tracker
AI microservice built with Flask and Groq API (LLaMA 3.3) for analysing vendor NDAs.
---
## Setup
### 1. Clone the project
```bash
git clone <your-repo-url>
cd ai-service
```
### 2. Install dependencies
```bash
pip install -r requirements.txt
```
### 3. Create `.env` file
```bash
cp .env.example .env
```
---
## Environment Variables
Create a `.env` file in the root folder with:

GROQ_API_KEY=your_groq_api_key_here

Get your Groq API key from: https://console.groq.com
---
## Run Instructions
```bash
python app.py
```
Server runs at: `http://127.0.0.1:5000`
---
## API Reference
### 1. POST `/describe`
Generates a plain-language description of a vendor NDA.
**Request:**
```json
{
  "input": "This NDA restricts the vendor from sharing confidential data for 2 years."
}
```
**Response:**
```json
{
  "result": "This agreement prevents the vendor from disclosing any confidential information...",
  "is_fallback": false,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
**Fallback Response (if Groq fails):**
```json
{
  "result": "Unable to generate description at this time. Please review the NDA manually.",
  "is_fallback": true,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
---
### 2. POST `/recommend`
Generates actionable recommendations based on vendor NDA input.
**Request:**
```json
{
  "input": "Vendor NDA includes unlimited liability clause and no exit terms."
}
```
**Response:**
```json
{
  "recommendations": [
    {
      "action_type": "Review",
      "description": "Carefully review the liability clause before signing."
    },
    {
      "action_type": "Negotiate",
      "description": "Request an exit clause to protect your interests."
    }
  ],
  "is_fallback": false,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
**Fallback Response (if Groq fails):**
```json
{
  "recommendations": [
    {
      "action_type": "Review",
      "description": "Please review the vendor NDA carefully."
    },
    {
      "action_type": "Consult",
      "description": "Consult a legal expert before signing."
    }
  ],
  "is_fallback": true,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
---
### 3. POST `/generate-report`
Generates a full structured NDA risk report.
**Request:**
```json
{
  "input": "Vendor NDA with 3 year confidentiality period, unlimited liability, no exit clause."
}
```
**Response:**
```json
{
  "report": {
    "title": "Vendor NDA Risk Report",
    "summary": "This NDA poses moderate risk due to unlimited liability and lack of exit terms.",
    "overview": "The agreement binds both parties for 3 years with strict confidentiality terms.",
    "key_items": [
      "3 year confidentiality period",
      "Unlimited liability clause",
      "No exit clause"
    ],
    "recommendations": [
      "Negotiate liability cap",
      "Add exit clause",
      "Consult legal expert"
    ]
  },
  "is_fallback": false,
  "cached": false,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
**Fallback Response (if Groq fails):**
```json
{
  "report": {
    "title": "NDA Report Unavailable",
    "summary": "AI service is temporarily unavailable.",
    "overview": "Please review the NDA manually.",
    "key_items": ["Review all clauses carefully"],
    "recommendations": ["Consult a legal expert"]
  },
  "is_fallback": true,
  "generated_at": "2026-04-30T17:38:30.944199"
}
```
---
## Security
- All endpoints protected with security headers
- API keys stored in `.env` — never committed to Git
- Input validation on all endpoints
---
## Tech Stack
- Python 3.13
- Flask
- Groq API (LLaMA 3.3)
- Redis (optional cache)
- python-dotenv all these in readme huh ?
