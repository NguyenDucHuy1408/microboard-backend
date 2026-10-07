package com.example.microboard; // Thay bằng package đúng trên máy bạn nếu khác

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/")
    public String testConnection() {
        return "Backend MicroBoard đã sẵn sàng kết nối với React!";
    }
}