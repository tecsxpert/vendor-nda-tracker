from flask import Blueprint, request, jsonify
from groq import Groq
import os
import json
from datetime import datetime

recommend_bp = Blueprint("recommend", __name__)

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

FALLBACK = [
    {"action_type": "Review", "description": "Please review the vendor NDA carefully."},
    {"action_type": "Consult", "description": "Consult a legal expert before signing."}
]

def load_prompt():
    with open("prompts/recommend.txt", "r") as f:
        return f.read()

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    if not data or "input" not in data:
        return jsonify({"error": "Missing input"}), 400

    user_input = data["input"]
    prompt = load_prompt().replace("{input}", user_input)

    try:
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            messages=[{"role": "user", "content": prompt}],
            timeout=2.0  # ← 2 second limit
        )

        output = response.choices[0].message.content

        try:
            recommendations = json.loads(output)
        except:
            return jsonify({
                "recommendations": FALLBACK,
                "is_fallback": True,
                "generated_at": datetime.utcnow().isoformat()
            })

        return jsonify({
            "recommendations": recommendations,
            "is_fallback": False,
            "generated_at": datetime.utcnow().isoformat()
        })

    except Exception:
        return jsonify({
            "recommendations": FALLBACK,
            "is_fallback": True,
            "generated_at": datetime.utcnow().isoformat()
        })