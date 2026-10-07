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

    @PostMapping
    public Board createBoard(@RequestBody Board board, Principal principal) {
        String userEmail = principal.getName(); 
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // Dùng setOwner và gán trực tiếp đối tượng User
        board.setOwner(currentUser);
        
        return boardRepository.save(board);
    }

    // API Xóa một bảng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}