# 📚 Tài Liệu API Authentication & Authorization

## 📋 Mục lục
1. [Tổng quan](#tổng-quan)
2. [Luồng Authentication](#luồng-authentication)
3. [API Endpoints](#api-endpoints)
4. [Roles & Permissions](#roles--permissions)
5. [Hướng dẫn Test](#hướng-dẫn-test)
6. [Error Codes](#error-codes)

---

## 🎯 Tổng quan

### Cơ chế Authentication
Hệ thống sử dụng **JWT (JSON Web Token)** với 2 loại token:
- **Access Token**: Dùng để gọi API (hết hạn sau 1 giờ)
- **Refresh Token**: Dùng để làm mới Access Token (hết hạn sau 7 ngày)

### Base URL
```
Development: http://localhost:5555
Production: https://your-domain.com
```

---

## 🔄 Luồng Authentication

### 1. Đăng ký (Register)
```mermaid
User → Frontend → Backend → Database → Backend → Frontend → User
                    ↓
              Create User
              Generate Tokens
              Save Refresh Token
```

### 2. Đăng nhập (Login)
```mermaid
User → Frontend → Backend → Validate → Generate Tokens → Frontend → User
```

### 3. Gọi Protected API
```mermaid
Frontend → Backend → Verify JWT → Check Permission → PostgREST → Response
```

### 4. Refresh Token
```mermaid
Access Token Expired → Frontend → Backend → Validate Refresh Token → New Access Token
```

---

## 📡 API Endpoints

### 1. Đăng ký tài khoản

**Endpoint:** `POST /api/auth/register`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "123456",
  "ten": "Nguyen",
  "ho": "Van A",
  "soDienThoai": "0901234567",
  "diaChi": "123 ABC Street"
}
```

**Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "userId": 1,
  "email": "user@example.com",
  "role": "nguoi_dung",
  "ten": "Nguyen",
  "ho": "Van A"
}
```

**Validation Rules:**
- `email`: Bắt buộc, phải là email hợp lệ, unique
- `password`: Bắt buộc, tối thiểu 6 ký tự
- `ten`: Bắt buộc, tối đa 100 ký tự
- `ho`: Bắt buộc, tối đa 100 ký tự
- `soDienThoai`: Optional, tối đa 20 ký tự
- `diaChi`: Optional

**Error Responses:**
```json
// Email đã tồn tại
{
  "message": "Email đã tồn tại"
}

// Validation error
{
  "message": "Email không hợp lệ"
}
```

---

### 2. Đăng nhập

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "userId": 1,
  "email": "user@example.com",
  "role": "nguoi_dung",
  "ten": "Nguyen",
  "ho": "Van A"
}
```

**Error Responses:**
```json
// Sai email hoặc password (401 Unauthorized)
{
  "message": "Email hoặc mật khẩu không đúng"
}

// Tài khoản bị khóa (401 Unauthorized)
{
  "message": "Tài khoản đã bị khóa"
}
```

---

### 3. Làm mới Access Token

**Endpoint:** `POST /api/auth/refresh`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",  // NEW
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",  // SAME
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "userId": 1,
  "email": "user@example.com",
  "role": "nguoi_dung",
  "ten": "Nguyen",
  "ho": "Van A"
}
```

**Error Responses:**
```json
// Refresh token không hợp lệ (401 Unauthorized)
{
  "message": "Refresh token không hợp lệ"
}

// Refresh token đã hết hạn (401 Unauthorized)
{
  "message": "Refresh token đã hết hạn hoặc bị thu hồi"
}
```

---

### 4. Đăng xuất

**Endpoint:** `POST /api/auth/logout`

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "message": "Đăng xuất thành công"
}
```

---

### 5. Gọi Protected API

**Endpoint:** Bất kỳ API nào cần authentication

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Example:**
```bash
GET /api/v1/nguoi_dung
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Error Responses:**
```json
// Không có token (401 Unauthorized)
{
  "error": "Unauthorized"
}

// Token hết hạn (401 Unauthorized)
{
  "error": "Token expired"
}

// Không đủ quyền (403 Forbidden)
{
  "error": "Insufficient permissions",
  "message": "Bạn không có quyền thực hiện hành động này"
}
```

---

## 👥 Roles & Permissions

### Danh sách Roles

| Role | Value | Mô tả |
|------|-------|-------|
| Admin | `quan_tri_vien` | Quản trị viên - Full quyền |
| Operator | `dieu_hanh_vien` | Điều hành viên - Quản lý nội dung |
| User | `nguoi_dung` | Người dùng thông thường |
| Volunteer | `tinh_nguyen_vien` | Tình nguyện viên |

### Public Endpoints (Không cần token)

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/auth/register` | Đăng ký |
| POST | `/api/auth/login` | Đăng nhập |
| POST | `/api/auth/refresh` | Làm mới token |
| POST | `/api/auth/logout` | Đăng xuất |
| GET | `/api/v1/du_an/**` | Xem danh sách dự án |
| GET | `/api/v1/tin_tuc/**` | Xem tin tức |
| GET | `/api/v1/danh_muc_du_an/**` | Xem danh mục dự án |
| GET | `/` | Trang chủ |
| GET | `/docs` | API docs |

### Protected Endpoints (Cần token)

#### 1. Dự Án (`/api/v1/du_an`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/du_an` | ✅ | ✅ | ✅ | ✅ |
| POST | `/du_an` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/du_an/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/du_an/{id}` | ✅ | ❌ | ❌ | ❌ |

#### 2. Quyên Góp (`/api/v1/quyen_gop`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/quyen_gop` | ✅ | ✅ | ✅ | ✅ |
| POST | `/quyen_gop` | ✅ | ✅ | ✅ | ✅ |
| PUT/PATCH | `/quyen_gop/{id}` | ✅ | ❌ | ❌ | ❌ |
| DELETE | `/quyen_gop/{id}` | ✅ | ❌ | ❌ | ❌ |

#### 3. Người Dùng (`/api/v1/nguoi_dung`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/nguoi_dung` | ✅ | ✅ | ✅ | ✅ |
| POST | `/nguoi_dung` | ✅ | ❌ | ❌ | ❌ |
| PUT/PATCH | `/nguoi_dung/{id}` | ✅ | ❌ | ❌ | ❌ |
| DELETE | `/nguoi_dung/{id}` | ✅ | ❌ | ❌ | ❌ |

#### 4. Giải Ngân (`/api/v1/giai_ngan`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/giai_ngan` | ✅ | ✅ | ✅ | ✅ |
| POST | `/giai_ngan` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/giai_ngan/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/giai_ngan/{id}` | ✅ | ❌ | ❌ | ❌ |

#### 5. Tin Tức (`/api/v1/tin_tuc`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/tin_tuc` | ✅ | ✅ | ✅ | ✅ |
| POST | `/tin_tuc` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/tin_tuc/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/tin_tuc/{id}` | ✅ | ✅ | ❌ | ❌ |

#### 6. Sự Kiện (`/api/v1/su_kien`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/su_kien` | ✅ | ✅ | ✅ | ✅ |
| POST | `/su_kien` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/su_kien/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/su_kien/{id}` | ✅ | ✅ | ❌ | ❌ |

#### 7. Đăng Ký Sự Kiện (`/api/v1/dang_ky_su_kien`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/dang_ky_su_kien` | ✅ | ✅ | ✅ | ✅ |
| POST | `/dang_ky_su_kien` | ✅ | ✅ | ✅ | ✅ |
| DELETE | `/dang_ky_su_kien/{id}` | ✅ | ✅ | ✅ | ✅ |

#### 8. Tài Khoản Thanh Toán (`/api/v1/tai_khoan_thanh_toan`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/tai_khoan_thanh_toan` | ✅ | ✅ | ✅ | ✅ |
| POST | `/tai_khoan_thanh_toan` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/tai_khoan_thanh_toan/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/tai_khoan_thanh_toan/{id}` | ✅ | ✅ | ❌ | ❌ |

#### 9. Cập Nhật Dự Án (`/api/v1/cap_nhat_du_an`)

| Method | Endpoint | Admin | Operator | User | Volunteer |
|--------|----------|-------|----------|------|-----------|
| GET | `/cap_nhat_du_an` | ✅ | ✅ | ✅ | ✅ |
| POST | `/cap_nhat_du_an` | ✅ | ✅ | ❌ | ❌ |
| PUT/PATCH | `/cap_nhat_du_an/{id}` | ✅ | ✅ | ❌ | ❌ |
| DELETE | `/cap_nhat_du_an/{id}` | ✅ | ✅ | ❌ | ❌ |

---

## 🧪 Hướng dẫn Test

### 1. Test Flow đầy đủ (Happy Path)

#### Step 1: Đăng ký tài khoản
```bash
POST http://localhost:5555/api/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "123456",
  "ten": "Test",
  "ho": "User",
  "soDienThoai": "0901234567"
}

# Expected: 201 Created
# Lưu lại: accessToken, refreshToken
```

#### Step 2: Gọi protected API với access token
```bash
GET http://localhost:5555/api/v1/nguoi_dung
Authorization: Bearer {accessToken}

# Expected: 200 OK với danh sách users
```

#### Step 3: Test permission denied
```bash
POST http://localhost:5555/api/v1/du_an
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "tieu_de": "Test Project"
}

# Expected: 403 Forbidden (vì user thường không có quyền tạo dự án)
```

#### Step 4: Refresh token
```bash
POST http://localhost:5555/api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "{refreshToken}"
}

# Expected: 200 OK với accessToken mới
```

#### Step 5: Logout
```bash
POST http://localhost:5555/api/auth/logout
Content-Type: application/json

{
  "refreshToken": "{refreshToken}"
}

# Expected: 200 OK
```

#### Step 6: Verify token đã bị revoke
```bash
POST http://localhost:5555/api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "{refreshToken}"
}

# Expected: 401 Unauthorized (token đã bị thu hồi)
```

---

### 2. Test Cases cho Frontend

#### Test Case 1: Đăng ký thành công
```javascript
const response = await fetch('http://localhost:5555/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'newuser@example.com',
    password: '123456',
    ten: 'New',
    ho: 'User'
  })
});

// Assert
expect(response.status).toBe(201);
const data = await response.json();
expect(data).toHaveProperty('accessToken');
expect(data).toHaveProperty('refreshToken');
expect(data.role).toBe('nguoi_dung');
```

#### Test Case 2: Login thành công
```javascript
const response = await fetch('http://localhost:5555/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'test@example.com',
    password: '123456'
  })
});

// Assert
expect(response.status).toBe(200);
const data = await response.json();
expect(data.accessToken).toBeDefined();
```

#### Test Case 3: Gọi API với token
```javascript
const response = await fetch('http://localhost:5555/api/v1/nguoi_dung', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});

// Assert
expect(response.status).toBe(200);
```

#### Test Case 4: Auto refresh token khi hết hạn
```javascript
// Giả sử access token đã hết hạn, gọi API sẽ trả về 401
const apiResponse = await fetch('http://localhost:5555/api/v1/nguoi_dung', {
  headers: { 'Authorization': `Bearer ${expiredToken}` }
});

if (apiResponse.status === 401) {
  // Refresh token
  const refreshResponse = await fetch('http://localhost:5555/api/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });

  const { accessToken: newAccessToken } = await refreshResponse.json();

  // Retry API với token mới
  const retryResponse = await fetch('http://localhost:5555/api/v1/nguoi_dung', {
    headers: { 'Authorization': `Bearer ${newAccessToken}` }
  });

  expect(retryResponse.status).toBe(200);
}
```

---

### 3. Test Cases cho Tester

#### TC-001: Đăng ký với email hợp lệ
- **Pre-condition:** Email chưa tồn tại
- **Steps:** POST /api/auth/register với email hợp lệ
- **Expected:** 201 Created, trả về tokens và user info

#### TC-002: Đăng ký với email đã tồn tại
- **Pre-condition:** Email đã được đăng ký
- **Steps:** POST /api/auth/register với email đã tồn tại
- **Expected:** 400 Bad Request, message "Email đã tồn tại"

#### TC-003: Đăng ký với password ngắn hơn 6 ký tự
- **Pre-condition:** None
- **Steps:** POST /api/auth/register với password "12345"
- **Expected:** 400 Bad Request, validation error

#### TC-004: Login với credentials đúng
- **Pre-condition:** User đã đăng ký
- **Steps:** POST /api/auth/login với email/password đúng
- **Expected:** 200 OK, trả về tokens

#### TC-005: Login với password sai
- **Pre-condition:** User đã đăng ký
- **Steps:** POST /api/auth/login với password sai
- **Expected:** 401 Unauthorized

#### TC-006: Gọi protected API không có token
- **Pre-condition:** None
- **Steps:** GET /api/v1/nguoi_dung không có Authorization header
- **Expected:** 401 Unauthorized

#### TC-007: Gọi protected API với token hợp lệ
- **Pre-condition:** User đã login
- **Steps:** GET /api/v1/nguoi_dung với valid token
- **Expected:** 200 OK với data

#### TC-008: User thường tạo dự án
- **Pre-condition:** Login với role "nguoi_dung"
- **Steps:** POST /api/v1/du_an với valid token
- **Expected:** 403 Forbidden

#### TC-009: Admin tạo dự án
- **Pre-condition:** Login với role "quan_tri_vien"
- **Steps:** POST /api/v1/du_an với valid token
- **Expected:** 201 Created

#### TC-010: Refresh token hợp lệ
- **Pre-condition:** User đã login
- **Steps:** POST /api/auth/refresh với valid refresh token
- **Expected:** 200 OK với access token mới

#### TC-011: Refresh token sau khi logout
- **Pre-condition:** User đã logout
- **Steps:** POST /api/auth/refresh với revoked refresh token
- **Expected:** 401 Unauthorized

---

## ⚠️ Error Codes

| HTTP Code | Error Type | Message | Mô tả |
|-----------|------------|---------|-------|
| 400 | Bad Request | Email đã tồn tại | Email đã được đăng ký |
| 400 | Bad Request | Validation error | Dữ liệu không hợp lệ |
| 401 | Unauthorized | Email hoặc mật khẩu không đúng | Sai thông tin đăng nhập |
| 401 | Unauthorized | Tài khoản đã bị khóa | User bị khóa |
| 401 | Unauthorized | Refresh token không hợp lệ | Token không đúng format |
| 401 | Unauthorized | Refresh token đã hết hạn | Token đã expire hoặc revoked |
| 401 | Unauthorized | Token expired | Access token hết hạn |
| 403 | Forbidden | Insufficient permissions | Không đủ quyền |
| 403 | Forbidden | Bạn không có quyền thực hiện hành động này | Không đủ quyền (tiếng Việt) |

---

## 💡 Best Practices cho Frontend

### 1. Lưu trữ Token
```javascript
// ✅ GOOD: Lưu trong memory hoặc httpOnly cookie
const [accessToken, setAccessToken] = useState('');
const [refreshToken, setRefreshToken] = useState('');

// ❌ BAD: Không lưu trong localStorage (dễ bị XSS)
localStorage.setItem('accessToken', token); // AVOID
```

### 2. Auto Refresh Token
```javascript
// Interceptor tự động refresh token khi hết hạn
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const newAccessToken = await refreshAccessToken();
      axios.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;

      return axios(originalRequest);
    }

    return Promise.reject(error);
  }
);
```

### 3. Handle Logout khi Refresh Token hết hạn
```javascript
async function refreshAccessToken() {
  try {
    const response = await axios.post('/api/auth/refresh', {
      refreshToken: getRefreshToken()
    });

    const { accessToken } = response.data;
    setAccessToken(accessToken);
    return accessToken;

  } catch (error) {
    // Refresh token hết hạn → logout user
    clearTokens();
    redirectToLogin();
    throw error;
  }
}
```

### 4. Kiểm tra Permission ở Frontend
```javascript
// Kiểm tra role trước khi hiển thị UI
function canCreateProject(userRole) {
  return ['quan_tri_vien', 'dieu_hanh_vien'].includes(userRole);
}

// Render conditional
{canCreateProject(user.role) && (
  <button onClick={createProject}>Tạo dự án</button>
)}
```

---

## 📞 Support

Nếu có vấn đề, liên hệ:
- Backend Team: [email/slack]
- Documentation: Xem file này

---

**Last Updated:** 2025-11-14
**Version:** 1.0.0
