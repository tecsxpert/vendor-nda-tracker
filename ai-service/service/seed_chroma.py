import chromadb

# Setup ChromaDB
client = chromadb.PersistentClient(path="./chroma_db")
collection = client.get_or_create_collection(name="nda_knowledge")

# 10 NDA domain knowledge documents
documents = [
    "An NDA (Non-Disclosure Agreement) is a legal contract that protects confidential information shared between parties.",
    "NDA confidentiality clauses prevent parties from sharing trade secrets, business strategies, or proprietary information.",
    "NDA duration typically ranges from 1 to 5 years. Unlimited duration NDAs may not be enforceable in some jurisdictions.",
    "Breach of NDA can result in financial penalties, injunctions, and legal action against the violating party.",
    "Mutual NDAs protect both parties equally, while one-way NDAs only protect one party's confidential information.",
    "NDA scope defines what information is considered confidential. Overly broad scope may make the NDA unenforceable.",
    "Exclusions in NDAs typically include publicly available information, independently developed information, and legally obtained information.",
    "NDA jurisdiction clause determines which country or state laws govern the agreement in case of disputes.",
    "Vendor NDAs should be reviewed for liability caps, indemnification clauses, and data protection obligations.",
    "NDA termination clauses specify conditions under which the agreement can be ended by either party."
]

ids = [f"doc_{i+1}" for i in range(len(documents))]

# Add to ChromaDB
collection.add(
    documents=documents,
    ids=ids
)

print(f"Seeded {len(documents)} documents to ChromaDB successfully!")