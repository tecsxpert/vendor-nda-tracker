import requests
import json

BASE_URL = "http://127.0.0.1:5000"

# 30 demo records
demo_inputs = [
    "NDA with 2 year confidentiality period and unlimited liability clause",
    "Vendor agreement restricting sharing of trade secrets for 3 years",
    "Mutual NDA between two tech companies for software development",
    "One-sided NDA protecting only the client's information",
    "NDA with no exit clause and automatic renewal every year",
    "Vendor NDA with data protection and GDPR compliance requirements",
    "NDA including non-compete clause for 5 years post termination",
    "Short term NDA for a 6 month project with a freelancer",
    "NDA with broad scope covering all business communications",
    "Vendor agreement with penalty clause of $50,000 for breach",
    "NDA governed by UK law with London jurisdiction",
    "Technology sharing NDA between startup and enterprise company",
    "NDA with unclear termination conditions and vague scope",
    "Vendor NDA requiring monthly confidentiality reports",
    "NDA protecting customer data and payment information",
    "Agreement with strict IP ownership clauses favoring vendor",
    "NDA with force majeure clause excluding natural disasters",
    "Vendor agreement with unlimited indemnification obligations",
    "NDA covering both digital and physical confidential materials",
    "Agreement restricting reverse engineering of software products",
    "NDA with automatic breach notification within 24 hours",
    "Vendor NDA with third party disclosure restrictions",
    "Agreement protecting marketing strategies for 2 years",
    "NDA with dispute resolution through arbitration only",
    "Vendor agreement with strict audit rights every quarter",
    "NDA covering employee information and HR data",
    "Agreement with survival clause lasting 10 years post expiry",
    "NDA protecting financial projections and business plans",
    "Vendor NDA with cross border data transfer restrictions",
    "Agreement with mutual non-solicitation of employees clause"
]

results = []

print("Running 30 demo records...\n")

for i, input_text in enumerate(demo_inputs):
    try:
        response = requests.post(
            f"{BASE_URL}/recommend",
            json={"input": input_text},
            timeout=10
        )
        data = response.json()
        results.append({
            "record": i + 1,
            "input": input_text,
            "status": "✅ OK" if not data.get("is_fallback") else "⚠️ Fallback",
            "output": data
        })
        print(f"Record {i+1}: ✅ Done")
    except Exception as e:
        results.append({
            "record": i + 1,
            "input": input_text,
            "status": "❌ Failed",
            "error": str(e)
        })
        print(f"Record {i+1}: ❌ Failed — {e}")

# Save results
with open("demo_results.json", "w") as f:
    json.dump(results, f, indent=2)

print(f"\nAll 30 records done! Results saved to demo_results.json")