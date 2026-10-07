package com.example.microboard.controller;

import com.example.microboard.entity.Board;
import com.example.microboard.entity.User;
import com.example.microboard.repository.BoardRepository;
import com.example.microboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository; 

    @GetMapping
    public ResponseEntity<List<Board>> getBoards(Principal principal) {
        String userEmail = principal.getName(); 
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // Truyền thẳng đối tượng currentUser vào để tìm kiếm
        List<Board> userBoards = boardRepository.findByOwner(currentUser);
        
        return ResponseEntity.ok(userBoards);
    }

    // API tạo một Bảng mới (Đã vá lỗi NullPointerException)
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody Board board, Principal principal) {
        // 1. Chặn đứng lỗi nếu không có danh tính (mất Token hoặc Token hết hạn)
        if (principal == null) {
            return ResponseEntity.status(401).body("Từ chối truy cập: Không tìm thấy Token xác thực hoặc phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
        }
        
        // 2. Logic xử lý bình thường nếu Token hợp lệ
        String userEmail = principal.getName(); 
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        board.setOwner(currentUser);
        Board savedBoard = boardRepository.save(board);
        
        // 3. Trả về mã 200 OK kèm dữ liệu bảng vừa tạo
        return ResponseEntity.ok(savedBoard); 
    }

    // API Xóa một bảng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}