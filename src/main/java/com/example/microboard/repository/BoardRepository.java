package com.example.microboard.repository;

import com.example.microboard.entity.Board;
import com.example.microboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // Đổi findByOwnerId thành findByOwner, nhận tham số là đối tượng User
    List<Board> findByOwner(User owner); 
}