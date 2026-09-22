-- Run this file with psql while connected to the default "postgres" database.
SELECT format('CREATE DATABASE %I', 'dihuan_online_judge_system')
WHERE NOT EXISTS (
    SELECT 1
    FROM pg_database
    WHERE datname = 'dihuan_online_judge_system'
)\gexec

\connect dihuan_online_judge_system

CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    content TEXT,
    metadata JSON,
    embedding vector(1024)
);

CREATE INDEX IF NOT EXISTS vector_store_embedding_idx
    ON vector_store USING HNSW (embedding vector_cosine_ops);

CREATE INDEX IF NOT EXISTS vector_store_content_fts_idx
    ON vector_store USING GIN (to_tsvector('simple', content));