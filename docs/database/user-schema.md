# User Domain Database Schema

## Overview
This document describes the database schema for the User domain in the AIRO LocalBuzz backend application.

## Tables

### users
The main table for storing user account information.

| Column | Type | Constraints | Description |
|--------|------|------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each user |
| username | VARCHAR(50) | NOT NULL, UNIQUE | User's login username (3-50 characters) |
| email | VARCHAR(100) | NOT NULL, UNIQUE | User's email address for login and communication |
| password | VARCHAR(255) | NOT NULL | Encrypted password hash |
| full_name | VARCHAR(100) | NULLABLE | User's full display name |
| phone_number | VARCHAR(20) | NULLABLE | User's contact phone number |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'USER' | User role (USER, ADMIN, MODERATOR) |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | User status (ACTIVE, INACTIVE, SUSPENDED, DELETED) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Timestamp when user was created |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Timestamp when user was last updated |

## Constraints

### Check Constraints
- `chk_role`: Ensures role is one of 'USER', 'ADMIN', 'MODERATOR'
- `chk_status`: Ensures status is one of 'ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED'
- `chk_username_length`: Ensures username is at least 3 characters long
- `chk_email_format`: Validates email format using regex

### Unique Constraints
- `username`: Prevents duplicate usernames
- `email`: Prevents duplicate email addresses

## Indexes
- `idx_users_email`: Index on email for faster login queries
- `idx_users_username`: Index on username for faster login queries  
- `idx_users_status`: Index on status for filtering active users
- `idx_users_role`: Index on role for role-based queries
- `idx_users_created_at`: Index on created_at for chronological queries

## Entity Relationships
The User domain follows the existing project patterns:
- Extends `BaseEntity` for audit fields (`created_at`, `updated_at`)
- Maps to `UserEntity` JPA entity class
- Includes domain enums for `UserRole` and `UserStatus`

## Usage Notes
1. Passwords should always be encrypted before storage
2. Email addresses are used as an alternative login method
3. Usernames must be unique and at least 3 characters long
4. Default role is 'USER' and default status is 'ACTIVE'
5. Audit timestamps are automatically managed by JPA auditing