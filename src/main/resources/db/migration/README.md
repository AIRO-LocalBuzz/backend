# Database Migration Scripts

This directory contains SQL DDL scripts for database schema management.

## User Domain

### Files
- `V001__create_users_table.sql`: Complete DDL script with full constraints and indexes
- `V001_simple__create_users_table.sql`: Simplified DDL script for basic use cases

### Usage
These scripts can be used with database migration tools like Flyway or executed directly against MySQL database.

For manual execution:
```bash
mysql -u username -p database_name < V001__create_users_table.sql
```

### Features
The user table includes:
- Primary key with auto-increment
- Unique constraints on username and email
- Role-based access control (USER, ADMIN, MODERATOR)
- User status management (ACTIVE, INACTIVE, SUSPENDED, DELETED)  
- Audit timestamps (created_at, updated_at)
- Optimized indexes for common queries
- Data validation constraints

### Dependencies
- MySQL 8.0+ (for JSON validation and advanced features)
- Compatible with MySQL 5.7+ (basic functionality)