print("DESCRIBE LOADED")
from flask import Blueprint, request, jsonify
from groq import Groq
import os
from datetime import datetime

describe_bp = Blueprint("describe", __name__)

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

FALLBACK = "Unable to generate description at this time. Please review the NDA manually."

def load_prompt():
    with open("prompts/describe.txt", "r") as f:
        return f.read()

@describe_bp.route("/describe", methods=["POST"])
def describe():
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

        return jsonify({
            "result": output,
            "is_fallback": False,
            "generated_at": datetime.utcnow().isoformat()
        })

    except Exception:
        return jsonify({
            "result": FALLBACK,
            "is_fallback": True,
            "generated_at": datetime.utcnow().isoformat()
        })