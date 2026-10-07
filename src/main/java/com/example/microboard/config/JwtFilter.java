package com.example.microboard.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Nhúng công cụ xử lý Token của bạn vào đây

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy Token từ Header của Request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;

        // Kiểm tra xem Header có chứa chuỗi "Bearer " chuẩn không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Cắt bỏ 7 ký tự "Bearer " để lấy đoạn mã JWT
            try {
                // Trích xuất email từ Token (Đổi .extractEmail thành .extractUsername nếu file JwtUtil của bạn dùng tên đó)
                userEmail = jwtUtil.extractEmail(token); 
            } catch (Exception e) {
                System.out.println("Lỗi giải mã Token: " + e.getMessage());
            }
        }

        // 2. Nếu lấy được email và hệ thống chưa xác thực thì tiến hành cấp quyền
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Xác minh Token hợp lệ (Đổi .validateToken thành tên hàm tương ứng trong JwtUtil của bạn)
            if (jwtUtil.validateToken(token, userEmail)) { 
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, new ArrayList<>());
                
                // Ghi nhận danh tính người dùng vào hệ thống (Đây chính là đối tượng Principal)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 3. Cho phép Request đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}