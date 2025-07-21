-- User domain SQL DDL script
-- This script creates the users table with all necessary constraints and indexes

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN', 'MODERATOR')),
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED')),
    CONSTRAINT chk_username_length CHECK (LENGTH(username) >= 3),
    CONSTRAINT chk_email_format CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Comments for documentation
ALTER TABLE users COMMENT = 'User domain table storing user account information';
ALTER TABLE users MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key for user identification';
ALTER TABLE users MODIFY COLUMN username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Unique username for login (3-50 characters)';
ALTER TABLE users MODIFY COLUMN email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Unique email address for login and communication';
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NOT NULL COMMENT 'Encrypted password hash';
ALTER TABLE users MODIFY COLUMN full_name VARCHAR(100) COMMENT 'User full name for display';
ALTER TABLE users MODIFY COLUMN phone_number VARCHAR(20) COMMENT 'User phone number for contact';
ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT 'User role: USER, ADMIN, MODERATOR';
ALTER TABLE users MODIFY COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'User status: ACTIVE, INACTIVE, SUSPENDED, DELETED';
ALTER TABLE users MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when user was created';
ALTER TABLE users MODIFY COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Timestamp when user was last updated';