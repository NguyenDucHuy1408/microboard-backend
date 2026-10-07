package com.example.microboard.controller;

import com.example.microboard.entity.Board;
import com.example.microboard.entity.User;
import com.example.microboard.repository.BoardRepository;
import com.example.microboard.repository.UserRepository; // 1. Bổ sung import UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Mở CORS cho Frontend gọi
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    // 2. VÁ LỖI 1: Khai báo (Inject) UserRepository để Spring Boot có thể sử dụng
    @Autowired
    private UserRepository userRepository; 

    // API lấy toàn bộ danh sách các Bảng
    @GetMapping
    public ResponseEntity<List<Board>> getBoards(Principal principal) {
        // 1. Từ Token (Principal), lấy ra email của người đang đăng nhập
        String userEmail = principal.getName(); 
        
        // 2. Tìm User trong Database dựa vào email
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // 3. VÁ LỖI 2: Thêm .intValue() để chuyển ID từ kiểu Long sang Integer
        List<Board> userBoards = boardRepository.findByOwnerId(currentUser.getId().intValue());
        
        return ResponseEntity.ok(userBoards);
    }

    // API tạo một Bảng mới
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }
}