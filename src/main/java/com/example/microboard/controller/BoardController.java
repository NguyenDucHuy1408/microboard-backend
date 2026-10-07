package com.example.microboard.controller;

import com.example.microboard.entity.Board;
import com.example.microboard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import com.example.microboard.entity.User;
@RestController
@CrossOrigin(origins = "*") // Mở CORS cho Frontend gọi
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    // API lấy toàn bộ danh sách các Bảng
    @GetMapping
    public ResponseEntity<List<Board>> getBoards(Principal principal) {
        // 1. Từ Token (Principal), lấy ra email của người đang đăng nhập
        String userEmail = principal.getName(); 
        
        // 2. Tìm User trong Database dựa vào email
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // 3. Chỉ lấy các Board có owner_id khớp với ID của user này
        List<Board> userBoards = boardRepository.findByOwnerId(currentUser.getId());
        
        return ResponseEntity.ok(userBoards);
    }

    // API tạo một Bảng mới
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }
}