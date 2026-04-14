// ============================================================================
// FILE UPLOAD ROUTES
// ============================================================================
//
// POST /photo — multipart upload for check-in / expense receipt / visit photos
//
// Files are stored on local disk under `uploads/<orgId>/<yyyy-mm>/<uuid>.<ext>`
// (swap `diskStorage` for an S3 adapter in production).
// ============================================================================

import fs from "fs";
import path from "path";
import { Router, Request, Response, NextFunction } from "express";
import multer, { FileFilterCallback } from "multer";
import { v4 as uuidv4 } from "uuid";
import { protectRoute } from "../middleware/rbac.middleware";
import { sendSuccess } from "../../utils/response";
import { AppError } from "../../utils/errors";

const UPLOAD_ROOT =
  process.env.UPLOAD_DIR || path.resolve(process.cwd(), "uploads");
const MAX_BYTES = 10 * 1024 * 1024; // 10 MB

const ALLOWED_MIME = new Set([
  "image/jpeg",
  "image/png",
  "image/webp",
  "image/heic",
]);

const storage = multer.diskStorage({
  destination: (req, _file, cb) => {
    const user = (req as Request).user;
    if (!user) return cb(new Error("Unauthenticated"), "");
    const month = new Date().toISOString().slice(0, 7);
    const dir = path.join(UPLOAD_ROOT, String(user.empcloudOrgId), month);
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    const ext = path.extname(file.originalname).toLowerCase() || ".jpg";
    cb(null, `${uuidv4()}${ext}`);
  },
});

const fileFilter = (_req: any, file: Express.Multer.File, cb: FileFilterCallback) => {
  if (!ALLOWED_MIME.has(file.mimetype)) {
    return cb(new Error(`Unsupported file type: ${file.mimetype}`));
  }
  cb(null, true);
};

const upload = multer({
  storage,
  limits: { fileSize: MAX_BYTES },
  fileFilter,
});

const router = Router();
router.use(protectRoute);

// POST /photo — single photo upload
router.post(
  "/photo",
  upload.single("file"),
  (req: Request, res: Response, next: NextFunction) => {
    try {
      if (!req.file) {
        throw new AppError(400, "NO_FILE", "file field is required (multipart)");
      }
      const rel = path.relative(UPLOAD_ROOT, req.file.path).replace(/\\/g, "/");
      sendSuccess(
        res,
        {
          url: `/uploads/${rel}`,
          path: rel,
          size: req.file.size,
          mime: req.file.mimetype,
          originalName: req.file.originalname,
        },
        201,
      );
    } catch (err) {
      next(err);
    }
  },
);

export { router as uploadRoutes, UPLOAD_ROOT };
