package com.example.microboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "board_columns")
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Thiết lập quan hệ: Nhiều Cột (Many) thuộc về 1 Bảng (One)
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
}