"# ChatAppBackend" 

```bash
# 1. 🔓 Authentication API (Public)

> ⚠️ Không yêu cầu Token

---

## 📨 1.1 Gửi OTP đăng ký

URL: POST /api/v1/users/send-otp?email=email_dang_ky_de_nhan_otp_xac_thuc
✅ Response (200 OK)
{
  "message": "OTP đã được gửi về email"
}
📝 1.2 Đăng ký tài khoản
URL: POST /api/v1/users/register
Body
{
  "fullName": "Nguyen Van A",
  "email": "example@gmail.com",
  "password": "password123",
  "confirmPassword": "password123",
  "otp": "123456"
}
✅ Response (201 Created)
{
  "message": "Đăng ký thành công"
}
❌ Error
{
  "error": "OTP không hợp lệ"
}
🔑 1.3 Đăng nhập
URL: POST /auth-management/api/v1/auth/log-in
Body
{
  "identifier": "example@gmail.com",
  "password": "password123"
}
✅ Response (200 OK)
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

⚠️ Lưu ý:
Dùng accessToken cho các API Private

2. 🔐 User API (Private)

🔒 Yêu cầu Header:

Authorization: Bearer <accessToken>
👤 2.1 Xem thông tin cá nhân
URL: GET /api/v1/users/me
✅ Response
{
  "id": 1,
  "fullName": "Nguyen Van A",
  "email": "example@gmail.com"
}
✏️ 2.2 Cập nhật Profile
URL: PUT /api/v1/users/me
Body
{
  "firstName": "Vinh",
  "lastName": "Nguyen",
  "phoneNumber": "0987654321",
  "address": "TP.HCM",
  "bio": "Học viên lớp DHKTPM18",
  "gender": "MALE",
  "dateOfBirth": "2000-01-01"
}
✅ Response
{
  "message": "Cập nhật thành công"
}
🖼️ 2.3 Upload Avatar
URL: POST /api/v1/users/me/avatar
Content-Type: multipart/form-data
Key	Type	Value
file	File	Chọn ảnh
✅ Response
{
  "avatarUrl": "http://localhost:8080/avatars/avatar_123.png"
}
🔒 2.4 Đổi mật khẩu
URL: PUT /api/v1/users/password
Body
{
  "oldPassword": "password123",
  "newPassword": "newpassword456",
  "confirmNewPassword": "newpassword456"
}
✅ Response
{
  "message": "Đổi mật khẩu thành công"
}
3. 📂 Cấu trúc thư mục
src/main/java/
│
├── entities        # Entity (mapping DB MySQL)
├── repositories    # JPA Repository
├── services        # Business logic
│   ├── AuthService
│   ├── UserService
│   └── EmailService
├── controllers     # REST API
├── configs         # Security, JWT, Web config
4. 📸 Upload Avatar
Ảnh lưu tại:
uploads/avatars/
Truy cập trực tiếp:
http://localhost:8080/avatars/<file_name>
5. 📝 Ghi chú
OTP gồm 6 chữ số
Token sử dụng chuẩn JWT
Password nên được mã hóa bằng BCrypt
API tuân theo chuẩn RESTful
🧪 Test API (Gợi ý)

Bạn có thể test bằng:

Postman