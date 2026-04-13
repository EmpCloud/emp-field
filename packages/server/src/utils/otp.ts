// Lightweight OTP generator (no external dep) used by legacy admin/profile services.
const DIGITS = "0123456789";
const ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

function generate(length: number, opts?: { digits?: boolean; alpha?: boolean }): string {
  const useDigits = opts?.digits ?? true;
  const useAlpha = opts?.alpha ?? false;
  const charset = (useDigits ? DIGITS : "") + (useAlpha ? ALPHA : "");
  if (!charset) return "";
  let out = "";
  for (let i = 0; i < length; i++) {
    out += charset[Math.floor(Math.random() * charset.length)];
  }
  return out;
}

export default { generate };
