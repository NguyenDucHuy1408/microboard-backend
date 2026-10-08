package com.example.microboard.controller;

import com.example.microboard.entity.Board;
import com.example.microboard.entity.User;
import com.example.microboard.repository.BoardRepository;
import com.example.microboard.repository.TaskRepository; // 1. Bổ sung import TaskRepository
import com.example.microboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // 2. Bổ sung import Transactional
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

    @Autowired
    private TaskRepository taskRepository; // 3. Inject TaskRepository vào Controller

    @GetMapping
    public ResponseEntity<List<Board>> getBoards(Principal principal) {
        String userEmail = principal.getName(); 
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        //List<Board> userBoards = boardRepository.findByOwner(currentUser);
        List<Board> userBoards = boardRepository.findAll();
        return ResponseEntity.ok(userBoards);
    }

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody Board board, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Từ chối truy cập: Không tìm thấy Token xác thực.");
        }
        
        String userEmail = principal.getName(); 
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        board.setOwner(currentUser);
        Board savedBoard = boardRepository.save(board);
        return ResponseEntity.ok(savedBoard); 
    }

    // 4. CẬP NHẬT API XÓA: Dọn sạch Task con trước rồi mới xóa Board
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        // Bước 1: Xóa toàn bộ task nằm trong bảng này
        taskRepository.deleteByBoardId(id);
        
        // Bước 2: Xóa chính cái bảng đó
        boardRepository.deleteById(id);
        
        return ResponseEntity.ok().build();
    }
}