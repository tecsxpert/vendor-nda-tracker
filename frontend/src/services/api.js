/**
 * api.js — Centralized API service for Vendor NDA Tracker frontend
 *
 * Calls the Java Spring Boot backend (port 8080) which proxies to Flask AI service.
 * All three AI endpoints are covered: describe, recommend, generate-report.
 */

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

/**
 * Generic POST helper with error handling.
 * @param {string} endpoint - e.g. '/vendor/describe'
 * @param {string} inputText - raw NDA text
 * @returns {Promise<object>} - parsed JSON response
 * @throws {Error} - with user-friendly message
 */
async function postToBackend(endpoint, inputText) {
  const response = await fetch(`${BASE_URL}${endpoint}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify({ input: inputText }),
  });

  if (!response.ok) {
    const text = await response.text().catch(() => '');
    throw new Error(`Backend error (${response.status}): ${text || response.statusText}`);
  }

  return response.json();
}

/**
 * POST /vendor/describe
 * Returns a plain-language description of the vendor NDA.
 *
 * @param {string} ndaText
 * @returns {Promise<{ result: string, is_fallback: boolean, generated_at: string }>}
 */
export async function describeNda(ndaText) {
  return postToBackend('/vendor/describe', ndaText);
}

/**
 * POST /vendor/recommend
 * Returns actionable recommendations for the vendor NDA.
 *
 * @param {string} ndaText
 * @returns {Promise<{ recommendations: Array<{action_type: string, description: string}>, is_fallback: boolean, generated_at: string }>}
 */
export async function recommendNda(ndaText) {
  return postToBackend('/vendor/recommend', ndaText);
}

/**
 * POST /vendor/generate-report
 * Returns a full structured NDA risk report.
 *
 * @param {string} ndaText
 * @returns {Promise<{ report: object, is_fallback: boolean, cached: boolean, generated_at: string }>}
 */
export async function generateReport(ndaText) {
  return postToBackend('/vendor/generate-report', ndaText);
}

/**
 * POST /vendor/create (async fire-and-forget)
 * Saves vendor and triggers background AI processing.
 *
 * @param {string} ndaText
 * @returns {Promise<string>} - confirmation message
 */
export async function createVendor(ndaText) {
  const response = await fetch(`${BASE_URL}/vendor/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ input: ndaText }),
  });
  if (!response.ok) throw new Error(`Error ${response.status}`);
  return response.text();
}

/**
 * GET /actuator/health
 * Checks if the Spring Boot backend is reachable.
 *
 * @returns {Promise<boolean>}
 */
export async function checkHealth() {
  try {
    const res = await fetch(`${BASE_URL}/actuator/health`, { method: 'GET' });
    return res.ok;
  } catch {
    return false;
  }
}
