package com.example.microboard.entity;
import java.time.LocalDate;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_tasks_board_id", columnList = "board_id")
})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Đảm bảo tên biến là 'title', không phải 'content'
    @Column(nullable = false, length = 500)
    private String title; 

    @Column(nullable = false)
    private String status = "TODO"; 

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(columnDefinition = "int default 0")
    private Integer position = 0;

    // THÊM THUỘC TÍNH NÀY VÀO DƯỚI CÙNG
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date")
    private LocalDate dueDate;

    // THÊM 2 HÀM NÀY VÀO KHU VỰC GETTER/SETTER
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // --- CÁC HÀM GETTER VÀ SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getAssignee() { return assignee; }
    public void setAssignee(User assignee) { this.assignee = assignee; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}