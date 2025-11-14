-- ============================================
-- MIGRATION: Add refresh_token table
-- Run this SQL to add refresh token support
-- ============================================

CREATE TABLE IF NOT EXISTS "refresh_token" (
    "id" BIGSERIAL PRIMARY KEY,
    "token" VARCHAR(500) UNIQUE NOT NULL,
    "user_id" BIGINT NOT NULL,
    "expiry_date" TIMESTAMPTZ NOT NULL,
    "revoked" BOOLEAN DEFAULT false,
    "created_at" TIMESTAMPTZ DEFAULT (CURRENT_TIMESTAMP),

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY ("user_id")
        REFERENCES "nguoi_dung" ("id")
        ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id ON "refresh_token"("user_id");
CREATE INDEX IF NOT EXISTS idx_refresh_token_token ON "refresh_token"("token");
CREATE INDEX IF NOT EXISTS idx_refresh_token_expiry ON "refresh_token"("expiry_date");

-- Add comment
COMMENT ON TABLE "refresh_token" IS 'Bảng lưu trữ refresh tokens cho JWT authentication';
COMMENT ON COLUMN "refresh_token"."token" IS 'JWT refresh token string';
COMMENT ON COLUMN "refresh_token"."user_id" IS 'ID người dùng sở hữu token';
COMMENT ON COLUMN "refresh_token"."expiry_date" IS 'Ngày hết hạn của token';
COMMENT ON COLUMN "refresh_token"."revoked" IS 'Token đã bị thu hồi hay chưa (logout)';
