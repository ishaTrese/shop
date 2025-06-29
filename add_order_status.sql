ALTER TABLE orders ADD COLUMN status VARCHAR(50) DEFAULT 'Processing' NOT NULL;

CREATE INDEX idx_orders_status ON orders(status);

UPDATE orders SET status = 'Processing' WHERE status IS NULL; 