from flask import Blueprint, request, jsonify
from groq import Groq
from service.cache import r, get_cache_key, REDIS_AVAILABLE
import os
import json
from datetime import datetime

report_bp = Blueprint("report", __name__)

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

FALLBACK = {
    "title": "NDA Report Unavailable",
    "summary": "AI service is temporarily unavailable.",
    "overview": "Please review the NDA manually.",
    "key_items": ["Review all clauses carefully"],
    "recommendations": ["Consult a legal expert"]
}

def load_prompt():
    with open("prompts/generate_report.txt", "r") as f:
        return f.read()


@report_bp.route("/generate-report", methods=["POST"])
def generate_report():
    data = request.get_json()

    if not data or "input" not in data:
        return jsonify({"error": "Missing input"}), 400

    user_input = data["input"]

    # ✅ Cache key
    key = get_cache_key(user_input)

    # ✅ Check cache
    if REDIS_AVAILABLE and r:
        cached = r.get(key)
        if cached:
            return jsonify({
                "report": json.loads(cached),
                "is_fallback": False,
                "cached": True
            })

    # Prepare prompt
    prompt = load_prompt().replace("{input}", user_input)

    try:
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            messages=[{"role": "user", "content": prompt}],
            timeout=2.0
        )

        output = response.choices[0].message.content
        output = output.replace("```json", "").replace("```", "").strip()

        try:
            report = json.loads(output)
        except:
            return jsonify({
                "report": FALLBACK,
                "is_fallback": True,
                "generated_at": datetime.utcnow().isoformat()
            })

        #Save to cache
        if REDIS_AVAILABLE and r:
            r.setex(key, 900, json.dumps(report))

        return jsonify({
            "report": report,
            "is_fallback": False,
            "generated_at": datetime.utcnow().isoformat(),
            "cached": False
        })

    except Exception:
        return jsonify({
            "report": FALLBACK,
            "is_fallback": True,
            "generated_at": datetime.utcnow().isoformat()
        })