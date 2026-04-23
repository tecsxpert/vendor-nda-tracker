from flask import Blueprint, request, jsonify
from groq import Groq
import os
import json
from datetime import datetime

recommend_bp = Blueprint("recommend", __name__)

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def load_prompt():
    with open("prompts/recommend.txt", "r") as f:
        return f.read()

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    # Validate input
    if not data or "input" not in data:
        return jsonify({"error": "Missing input"}), 400

    user_input = data["input"]

    # Load prompt
    prompt = load_prompt().replace("{input}", user_input)

    # Call Groq
    response = client.chat.completions.create(
        model="llama-3.3-70b-versatile",
        messages=[{"role": "user", "content": prompt}]
    )

    output = response.choices[0].message.content

    # Convert string → JSON
    try:
        recommendations = json.loads(output)
    except:
        return jsonify({
            "error": "Invalid JSON response from model",
            "raw_output": output
        }), 500

    # Return structured response
    return jsonify({
        "recommendations": recommendations,
        "generated_at": datetime.utcnow().isoformat()
    })