from dotenv import load_dotenv
load_dotenv()

from flask import Flask
from werkzeug.serving import WSGIRequestHandler

WSGIRequestHandler.server_version = ""
WSGIRequestHandler.sys_version = ""

import routes.describe
import routes.recommend
import routes.generate_report
import routes.health

app = Flask(__name__)

@app.after_request
def add_security_headers(response):
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["X-Frame-Options"] = "DENY"
    response.headers["X-XSS-Protection"] = "1; mode=block"
    response.headers["Strict-Transport-Security"] = "max-age=31536000; includeSubDomains"
    response.headers["Content-Security-Policy"] = (
        "default-src 'self'; "
        "script-src 'self'; "
        "style-src 'self'; "
        "img-src 'self' data:; "
        "connect-src 'self'; "
        "font-src 'self'; "
        "object-src 'none'; "
        "base-uri 'self'; "
        "form-action 'self'; "
        "frame-src 'none'; "
        "frame-ancestors 'none';"
    )
    response.headers["Referrer-Policy"] = "no-referrer"
    response.headers["Permissions-Policy"] = "geolocation=()"
    response.headers["Server"] = "undisclosed"
    return response

app.register_blueprint(routes.describe.describe_bp)
app.register_blueprint(routes.recommend.recommend_bp)
app.register_blueprint(routes.generate_report.report_bp)
app.register_blueprint(routes.health.health_bp)

@app.route("/")
def home():
    return "AI Service Running"

@app.route("/sitemap.xml")
def sitemap():
    return "", 200

@app.route("/robots.txt")
def robots():
    return "User-agent: *\nDisallow: /", 200, {"Content-Type": "text/plain"}

if __name__ == "__main__":
    app.run(debug=True)