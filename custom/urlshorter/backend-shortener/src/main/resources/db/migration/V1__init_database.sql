CREATE TABLE IF NOT EXISTS "urls" (
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    "original_url" VARCHAR(2048) NOT NULL,
    "short_url" VARCHAR(255) NOT NULL UNIQUE,
    
    "access_count" INT NOT NULL DEFAULT 0,
    "last_access_time" TIMESTAMP WITH TIME ZONE,
    
    "expiration_time" TIMESTAMP WITH TIME ZONE,
    "date_created" TIMESTAMP WITH TIME ZONE NOT NULL,

    "version" BIGINT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_short_shorturl ON "urls"("short_url");