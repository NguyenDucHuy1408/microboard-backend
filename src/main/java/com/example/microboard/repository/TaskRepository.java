package com.example.microboard.repository;

import com.example.microboard.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Hàm hỗ trợ lấy toàn bộ task của một bảng
    List<Task> findByBoardId(Long boardId);
}