package com.example.microboard.controller;

import com.example.microboard.entity.Board;
import com.example.microboard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Mở CORS cho Frontend gọi
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    // API lấy toàn bộ danh sách các Bảng
    @GetMapping
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // API tạo một Bảng mới
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }
}