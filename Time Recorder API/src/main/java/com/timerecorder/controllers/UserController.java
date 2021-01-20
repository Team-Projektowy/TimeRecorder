package com.timerecorder.controllers;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;
import com.timerecorder.repositories.TaskRepository;
import com.timerecorder.repositories.TimeRecordRepository;
import com.timerecorder.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TimeRecordRepository timeRecordRepository;

    public UserController(UserRepository userRepository, TaskRepository taskRepository, TimeRecordRepository timeRecordRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.timeRecordRepository = timeRecordRepository;
    }

    @GetMapping("/{userId}/time-records")
    public Iterable<TimeRecord> getTimeRecords(
            @PathVariable Integer userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer taskId,
            HttpServletRequest httpServletRequest) {

        Claims claims = (Claims) httpServletRequest.getAttribute("claims");
        if (!claims.get("userId").equals(userId) && !(boolean) claims.get("isAdmin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont't have access to this resources");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (taskId == null && date == null) {
            return timeRecordRepository.findAllByUser(user);
        }

        if (taskId != null) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            if (date == null) {
                return timeRecordRepository.findAllByUserAndTask(user, task);
            }
            return timeRecordRepository.findAllByUserAndTaskAndStartingDate(user, task, date);
        }

        return timeRecordRepository.findAllByUserAndStartingDate(user, date);
    }

    @GetMapping("/me/time-records")
    public Iterable<TimeRecord> getCurUserTimeRecords(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer taskId,
            HttpServletRequest httpServletRequest) {

        Claims claims = (Claims) httpServletRequest.getAttribute("claims");
        Integer userId = (Integer) claims.get("userId");

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont't have access to this resources");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (taskId == null && date == null) {
            return timeRecordRepository.findAllByUser(user);
        }

        if (taskId != null) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            if (date == null) {
                return timeRecordRepository.findAllByUserAndTask(user, task);
            }
            return timeRecordRepository.findAllByUserAndTaskAndStartingDate(user, task, date);
        }

        return timeRecordRepository.findAllByUserAndStartingDate(user, date);
    }
}
