# Social Login API Documentation

This document describes the Firebase-based Google and KakaoTalk social login integration endpoints.

## Base URL
```
http://localhost:9001/api
```

## Authentication Endpoints

### 1. Generic Social Login
```http
POST /auth/social-login
Content-Type: application/json

{
    "token": "firebase-id-token-or-kakao-access-token",
    "provider": "google" // or "kakao"
}
```

**Success Response (200):**
```json
{
    "accessToken": "jwt-access-token",
    "tokenType": "Bearer",
    "user": {
        "id": 1,
        "email": "user@example.com",
        "name": "User Name",
        "profileImageUrl": "https://example.com/profile.jpg",
        "provider": "google"
    }
}
```

**Error Response (401):**
```json
HTTP 401 Unauthorized
```

### 2. Google Login
```http
POST /auth/google?token=firebase-id-token
```

**Parameters:**
- `token`: Firebase ID token obtained from Google authentication

**Success Response:** Same as generic social login

### 3. KakaoTalk Login
```http
POST /auth/kakao?token=kakao-access-token
```

**Parameters:**
- `token`: KakaoTalk access token obtained from Kakao OAuth2

**Success Response:** Same as generic social login

## Environment Configuration

### Required Environment Variables

```yaml
# Database Configuration
DB_URL: "jdbc:mysql://localhost:3306/airo"
DB_USERNAME: "username"
DB_PASSWORD: "password"

# Firebase Configuration
FIREBASE_SERVICE_ACCOUNT_KEY: "firebase-service-account-json-string"
FIREBASE_PROJECT_ID: "your-firebase-project-id"

# KakaoTalk Configuration
KAKAO_CLIENT_ID: "your-kakao-app-client-id"
KAKAO_REDIRECT_URI: "your-kakao-redirect-uri"

# JWT Configuration
JWT_SECRET: "your-256-bit-secret-key"
JWT_EXPIRATION: 86400000  # 24 hours in milliseconds
```

## Authentication Flow

### Google Login Flow
1. Client authenticates with Firebase using Google credentials
2. Client receives Firebase ID token
3. Client sends ID token to `/auth/google` endpoint
4. Server validates token with Firebase
5. Server creates/updates user in database
6. Server returns JWT token for API access

### KakaoTalk Login Flow
1. Client authenticates with KakaoTalk OAuth2
2. Client receives KakaoTalk access token
3. Client sends access token to `/auth/kakao` endpoint
4. Server validates token with KakaoTalk API
5. Server creates/updates user in database
6. Server returns JWT token for API access

## Using JWT Token

Include the JWT token in the Authorization header for authenticated requests:

```http
Authorization: Bearer your-jwt-token
```

## Error Handling

- **401 Unauthorized**: Invalid token or authentication failed
- **400 Bad Request**: Invalid provider specified
- **500 Internal Server Error**: Server configuration or external service issues

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(500),
    provider_type VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    UNIQUE KEY unique_provider_user (provider_type, provider_id)
);
```