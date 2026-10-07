package com.example.microboard.controller;

import com.example.microboard.entity.User;
import com.example.microboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Mới thêm
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*") // Cho phép truy cập từ React
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Gọi công cụ băm mật khẩu

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Mã hóa mật khẩu người dùng nhập vào thành chuỗi ký tự loằng ngoằng
        String hashedPass = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPass); // Ghi đè mật khẩu gốc bằng chuỗi đã mã hóa
        
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Gọi máy in thẻ JWT vào
    @Autowired
    private com.example.microboard.config.JwtUtil jwtUtil;

    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<?> login(@RequestBody User loginRequest) {
        // 1. Tìm người dùng trong CSDL qua email
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        
        // 2. Dùng công cụ Bcrypt để đối chiếu mật khẩu người dùng nhập vào với mật khẩu đã băm trong CSDL
        if (user == null || !passwordEncoder.matches(loginRequest.getPasswordHash(), user.getPasswordHash())) {
            // Trả về lỗi 401 nếu sai thông tin
            return org.springframework.http.ResponseEntity.status(401).body("Sai email hoặc mật khẩu!");
        }
        
        // 3. Nếu đúng, in ra thẻ JWT
        String token = jwtUtil.generateToken(user.getEmail());
        
        // Đóng gói thẻ vào một định dạng dễ đọc và gửi về
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("token", token);
        return org.springframework.http.ResponseEntity.ok(response);
    }
}