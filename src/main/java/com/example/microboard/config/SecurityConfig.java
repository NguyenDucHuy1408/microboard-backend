package com.example.microboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TODO: Nếu IDE báo đỏ chữ JwtFilter, bạn hãy bấm Alt+Enter để Import file Filter của bạn vào nhé

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Nhúng bộ lọc kiểm tra Token của bạn vào đây 
    // (Lưu ý: Nếu file code xử lý JWT của bạn tên là JwtAuthFilter hay tên khác, hãy đổi chữ JwtFilter bên dưới cho khớp nhé)
    @Autowired
    private JwtFilter jwtFilter;

    // 2. Khai báo công cụ băm mật khẩu Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Cấu hình phân quyền truy cập nghiêm ngặt
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                // BẮT BUỘC: Cho phép các request thăm dò (OPTIONS) của Frontend đi qua để không bị lỗi CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Mở cửa tự do cho 2 API Đăng nhập và Đăng ký
                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                
                // TẤT CẢ các API còn lại (như /api/boards, /api/tasks) đều phải xuất trình Token
                .anyRequest().authenticated() 
            )
            // Đặt bộ lọc quét Token của bạn lên trước trạm kiểm tra mặc định của Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}