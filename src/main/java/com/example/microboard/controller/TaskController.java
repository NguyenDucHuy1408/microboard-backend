package com.example.microboard.controller;

import com.example.microboard.entity.Task;
import com.example.microboard.repository.TaskRepository;
import com.example.microboard.repository.UserRepository;
import com.example.microboard.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Lấy danh sách thẻ công việc thuộc một Bảng cụ thể
    @GetMapping("/board/{boardId}")
    public List<Task> getTasksByBoard(@PathVariable Long boardId) {
        return taskRepository.findByBoardId(boardId);
    }

    // 2. Tạo thẻ công việc mới
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // 3. Cập nhật thẻ công việc (Ví dụ: Đổi trạng thái khi kéo thả)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy task"));
        
        // 1. Cập nhật các thông tin cơ bản
        task.setTitle(taskDetails.getTitle());
        task.setStatus(taskDetails.getStatus());
        task.setPosition(taskDetails.getPosition());
        task.setDueDate(taskDetails.getDueDate());
        // 2. CẬP NHẬT NGƯỜI ĐƯỢC GIAO VIỆC (ASSIGNEE)
        if (taskDetails.getAssignee() != null && taskDetails.getAssignee().getId() != null) {
            // Lấy toàn bộ thông tin User từ DB lên (để trả về React có sẵn email, tránh lỗi)
            User user = userRepository.findById(taskDetails.getAssignee().getId()).orElse(null);
            task.setAssignee(user);
        } else {
            // Nếu Frontend gửi null (Người dùng bấm dấu X gỡ người giao việc)
            task.setAssignee(null);
        }

        // 3. Lưu xuống Database
        return taskRepository.save(task);
    }

    // 4. Xóa thẻ công việc
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "Đã xóa công việc thành công!";
    }
}