-- Add blockchain tracking fields to quyen_gop table
ALTER TABLE quyen_gop
ADD COLUMN IF NOT EXISTS blockchain_tx_hash VARCHAR(66),
ADD COLUMN IF NOT EXISTS blockchain_block_number BIGINT,
ADD COLUMN IF NOT EXISTS blockchain_timestamp TIMESTAMPTZ,
ADD COLUMN IF NOT EXISTS blockchain_status VARCHAR(50);

-- Add indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_quyen_gop_blockchain_tx_hash ON quyen_gop(blockchain_tx_hash);
CREATE INDEX IF NOT EXISTS idx_quyen_gop_blockchain_status ON quyen_gop(blockchain_status);

-- Add blockchain tracking fields to giai_ngan table
ALTER TABLE giai_ngan
ADD COLUMN IF NOT EXISTS blockchain_tx_hash VARCHAR(66),
ADD COLUMN IF NOT EXISTS blockchain_block_number BIGINT,
ADD COLUMN IF NOT EXISTS blockchain_timestamp TIMESTAMPTZ,
ADD COLUMN IF NOT EXISTS blockchain_status VARCHAR(50);

-- Add indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_giai_ngan_blockchain_tx_hash ON giai_ngan(blockchain_tx_hash);
CREATE INDEX IF NOT EXISTS idx_giai_ngan_blockchain_status ON giai_ngan(blockchain_status);

-- Add comments
COMMENT ON COLUMN quyen_gop.blockchain_tx_hash IS 'Blockchain transaction hash (0x...)';
COMMENT ON COLUMN quyen_gop.blockchain_block_number IS 'Block number where transaction was included';
COMMENT ON COLUMN quyen_gop.blockchain_timestamp IS 'Timestamp from blockchain';
COMMENT ON COLUMN quyen_gop.blockchain_status IS 'Status: PENDING, CONFIRMED, FAILED';

COMMENT ON COLUMN giai_ngan.blockchain_tx_hash IS 'Blockchain transaction hash (0x...)';
COMMENT ON COLUMN giai_ngan.blockchain_block_number IS 'Block number where transaction was included';
COMMENT ON COLUMN giai_ngan.blockchain_timestamp IS 'Timestamp from blockchain';
COMMENT ON COLUMN giai_ngan.blockchain_status IS 'Status: PENDING, CONFIRMED, FAILED';
