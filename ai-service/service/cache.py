import redis
import hashlib

# Redis connection
r = redis.Redis(host='localhost', port=6379, db=0)

# Generate cache key
def get_cache_key(input_text):
    return hashlib.sha256(input_text.encode()).hexdigest()

import redis
import hashlib

try:
    r = redis.Redis(host='localhost', port=6379, db=0)
    r.ping()
    REDIS_AVAILABLE = True
except:
    r = None
    REDIS_AVAILABLE = False

def get_cache_key(text):
    return "report:" + hashlib.sha256(text.encode()).hexdigest()