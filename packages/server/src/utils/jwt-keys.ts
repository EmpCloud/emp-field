// ============================================================================
// JWT KEY LOADER
// ============================================================================
//
// Resolves the RS256 public key used to verify OAuth2 tokens issued by the
// EmpCloud core. Accepts either a PEM string (with escaped \n) or a filesystem
// path. Returns null when RS256 is not configured — callers should then fall
// back to HS256.
// ============================================================================

import fs from "fs";
import path from "path";
import { config } from "../config";
import { logger } from "./logger";

let cached: string | null | undefined;

export function loadPublicKey(): string | null {
  if (cached !== undefined) return cached;

  const raw = config.jwt.publicKey?.trim();
  if (!raw) {
    cached = null;
    return cached;
  }

  // Inline PEM (env var). Accept \n literal escape.
  if (raw.startsWith("-----BEGIN")) {
    cached = raw.replace(/\\n/g, "\n");
    return cached;
  }

  // Filesystem path
  try {
    const resolved = path.isAbsolute(raw) ? raw : path.resolve(process.cwd(), raw);
    cached = fs.readFileSync(resolved, "utf8");
    return cached;
  } catch (err) {
    logger.warn(`Failed to read JWT_PUBLIC_KEY at ${raw}: ${(err as Error).message}`);
    cached = null;
    return cached;
  }
}
