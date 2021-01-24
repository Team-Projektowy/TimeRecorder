package com.timerecorder.controllers;

import com.timerecorder.helpers.requestProcessingHelper;
import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;
import com.timerecorder.models.UserAPI;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

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

    @GetMapping
    public Iterable<User> getAll(HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable Integer userId, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }

    @PutMapping("/{userId}")
    public User edit(@PathVariable Integer userId, @RequestBody UserAPI newUser, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPosition(newUser.getPosition());
        user.setHoursAWeek(newUser.getHoursAWeek());
        user.setAdmin(newUser.isAdmin());

        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);
        User user = userRepository.findById(userId).orElse(null);
        Iterable<TimeRecord> timeRecords = timeRecordRepository.findAllByUser(user);
        timeRecordRepository.deleteAll(timeRecords);
        userRepository.deleteById(userId);
    }

    @GetMapping("/{userId}/time-records")
    public Iterable<TimeRecord> getTimeRecords(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate,
            @RequestParam(required = false) Integer taskId,
            HttpServletRequest request) {

        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        LocalDateTime startingDateTime = startingDate.atStartOfDay();
        LocalDateTime endingDateTime = endingDate.atTime(LocalTime.MAX);

        if (taskId != null) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            return timeRecordRepository.findAllByUserAndTaskAndStartingTimeBetweenOrderByStartingTime(user, task, startingDateTime, endingDateTime);
        }

        return timeRecordRepository.findAllByUserAndStartingTimeBetweenOrderByStartingTime(user, startingDateTime, endingDateTime);
    }

    @GetMapping("/me/time-records")
    public Iterable<TimeRecord> getCurUserTimeRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate,
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

        LocalDateTime startingDateTime = startingDate.atStartOfDay();
        LocalDateTime endingDateTime = endingDate.atTime(LocalTime.MAX);

        if (taskId != null) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            return timeRecordRepository.findAllByUserAndTaskAndStartingTimeBetweenOrderByStartingTime(user, task, startingDateTime, endingDateTime);
        }

        return timeRecordRepository.findAllByUserAndStartingTimeBetweenOrderByStartingTime(user, startingDateTime, endingDateTime);
    }
}
