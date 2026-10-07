package com.example.microboard.repository;

import com.example.microboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Hàm hỗ trợ tìm user bằng email để phục vụ việc Đăng nhập sau này
    Optional<User> findByEmail(String email);
}