# Family Health API - Authentication Testing

## 1. Login API (Đăng nhập bằng số điện thoại)

### Endpoint
```
POST http://localhost:8080/familyhealth/api/v1/auth/login
```

### Admin Account (Default)
```json
{
  "phone": "0123456789",
  "password": "admin123"
}
```

### Response Success
```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "phone": "0123456789",
    "role": "ADMIN",
    "profileId": null
  }
}
```

---

## 2. Register User

### Endpoint
```
POST http://localhost:8080/familyhealth/api/v1/users/register
```

### Request Body
```json
{
  "phone": "0987654321",
  "password": "password123",
  "roleId": 2,
  "isActive": true,
  "memberInfo": {
    "fullName": "Nguyen Van A",
    "address": "Ha Noi",
    "gender": "MALE",
    "dateOfBirth": "1990-01-01",
    "bhyt": "SV123456789",
    "cccd": "001234567890"
  }
}
```

---

## 3. Authenticated Requests

### Header Format
```
Authorization: Bearer YOUR_ACCESS_TOKEN_HERE
```

### Example: Get All Users (Admin Only)
```
GET http://localhost:8080/familyhealth/api/v1/users/getAll
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 4. Role-Based Access Control (@PreAuthorize)

### Roles
- **ADMIN**: Full access
- **DOCTOR**: Can update own profile
- **PATIENT**: Can create appointments
- **PATIENT_HOUSEHOLD**: Can manage family members

### Protected Endpoints Examples

#### Admin Only
- `GET /api/v1/users/getAll` - Get all users
- `DELETE /api/v1/users/delete/{id}` - Delete user
- `POST /api/v1/doctors/create` - Create doctor
- `DELETE /api/v1/doctors/delete/{id}` - Delete doctor
- All `/api/v1/roles/**` endpoints
- `PUT /api/v1/medicalresults/update/{id}` - Update medical result
- `DELETE /api/v1/medicalresults/delete/{id}` - Delete medical result
- `DELETE /api/v1/households/delete/{id}` - Delete household

#### Admin + Doctor
- `PUT /api/v1/doctors/update/{id}` - Update doctor info

#### Admin + Patient_Household
- `POST /api/v1/members/create` - Create family member
- `POST /api/v1/households/create` - Create household
- `PUT /api/v1/households/update/{id}` - Update household

#### Admin + Patient + Patient_Household
- `POST /api/v1/appointments/create` - Create appointment

---

## 5. Testing with cURL

### Login
```bash
curl -X POST http://localhost:8080/familyhealth/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "0123456789",
    "password": "admin123"
  }'
```

### Get All Users (Admin)
```bash
curl -X GET http://localhost:8080/familyhealth/api/v1/users/getAll \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 6. Testing with Postman

1. **Login**
   - Method: POST
   - URL: `http://localhost:8080/familyhealth/api/v1/auth/login`
   - Body (JSON):
     ```json
     {
       "phone": "0123456789",
       "password": "admin123"
     }
     ```
   - Copy `accessToken` from response

2. **Use Token**
   - Go to Authorization tab
   - Type: Bearer Token
   - Token: Paste your accessToken

3. **Test Protected Endpoint**
   - Method: GET
   - URL: `http://localhost:8080/familyhealth/api/v1/users/getAll`

---

## 7. Swagger UI

Access API documentation at:
```
http://localhost:8080/familyhealth/swagger-ui/index.html
```

1. Click "Authorize" button
2. Enter: `Bearer YOUR_TOKEN_HERE`
3. Test endpoints directly from Swagger

---

## Notes

- **Token expiration**: 30 days (2592000 seconds)
- **Login field**: Phone number (10 digits)
- **Default admin**: `0123456789` / `admin123`
- **JWT includes**: userId, phone, role, scope
