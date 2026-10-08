package com.example.microboard.repository;

import com.example.microboard.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Hàm hỗ trợ lấy toàn bộ task của một bảng
    List<Task> findByBoardId(Long boardId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Task t WHERE t.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}