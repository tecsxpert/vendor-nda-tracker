# SECURITY REVIEW – AI SERVICE

## 1. Prompt Injection
Risk: User may try to manipulate AI output by sending malicious prompts.
Fix: Input validation and sanitization will be added to filter unsafe content.

## 2. API Key Exposure
Risk: If API key is exposed, others can misuse it.
Fix: Stored in .env file and excluded using .gitignore.

## 3. Excessive Requests (Rate Limit)
Risk: Too many requests can overload the system or exhaust API quota.
Fix: Will implement rate limiting using flask-limiter.

## 4. Invalid Input Handling
Risk: Empty or incorrect inputs can cause errors.
Fix: Validate input before sending to AI model.

## 5. AI Response Failure
Risk: AI service may fail due to network or API issues.
Fix: Retry logic implemented with exponential backoff and fallback response.