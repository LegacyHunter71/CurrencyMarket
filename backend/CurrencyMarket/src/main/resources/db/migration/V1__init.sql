CREATE TABLE IF NOT EXISTS currency_transactions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    currency_code VARCHAR(3) NOT NULL,
    currency_name VARCHAR(30) NOT NULL,
    currency_rate DOUBLE PRECISION NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    amount DECIMAL NOT NULL,
    action_type VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_date TIMESTAMP NOT NULL
    );