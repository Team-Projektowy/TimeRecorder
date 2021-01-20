package com.timerecorder.controllers;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.TimeRecordAPI;
import com.timerecorder.models.User;
import com.timerecorder.repositories.TaskRepository;
import com.timerecorder.repositories.TimeRecordRepository;
import com.timerecorder.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RestController
@RequestMapping("time-records")
public class TimeRecordController {

    private final TimeRecordRepository timeRecordRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TimeRecordController(TimeRecordRepository timeRecordRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.timeRecordRepository = timeRecordRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/{id}")
    public TimeRecord getById(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        TimeRecord timeRecord = timeRecordRepository.findById(id).orElse(null);

        if (timeRecord == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Time record not found");
        }

        Claims claims = (Claims) httpServletRequest.getAttribute("claims");
        if (!timeRecord.getUser().getId().equals(claims.get("userId")) && !(boolean) claims.get("isAdmin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have access to this Time Record");
        }

        return timeRecord;
    }

    @PostMapping
    public TimeRecord create(@RequestBody TimeRecordAPI timeRecord, HttpServletRequest httpServletRequest) {
        Claims claims = (Claims) httpServletRequest.getAttribute("claims");

        Task task = taskRepository.findById(timeRecord.getTaskId()).orElse(null);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task of specified ID doesn't exist");
        }

        User user = userRepository.findById((Integer) claims.get("userId")).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TimeRecord newTimeRecord = new TimeRecord();
        newTimeRecord.setUser(user);
        newTimeRecord.setTask(task);
        newTimeRecord.setStartingTime(timeRecord.getStartingTime());
        newTimeRecord.setEndingTime(timeRecord.getEndingTime());
        newTimeRecord.setDescription(timeRecord.getDescription());

        return timeRecordRepository.save(newTimeRecord);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Integer id) {
        Optional<TimeRecord> timeRecord = timeRecordRepository.findById(id);
        if (timeRecord.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Time record not found.");
        }

        timeRecordRepository.delete(timeRecord.get());
    }

    @PutMapping(path = "/{id}")
    public TimeRecord edit(@PathVariable Integer id, @RequestBody TimeRecordAPI timeRecordAPI, HttpServletRequest httpServletRequest) {
        Claims claims = (Claims) httpServletRequest.getAttribute("claims");

        TimeRecord timeRecord = timeRecordRepository.findById(id).orElse(null);
        if (timeRecord == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Time record not found.");
        }


        if (!timeRecord.getUser().getId().equals(claims.get("userId"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner ot this time record.");
        }

        Task task = taskRepository.findById(timeRecordAPI.getTaskId()).orElse(null);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task of specified ID doesn't exist");
        }

        timeRecord.setTask(task);
        timeRecord.setStartingTime(timeRecordAPI.getStartingTime());
        timeRecord.setEndingTime(timeRecordAPI.getEndingTime());
        timeRecord.setDescription(timeRecordAPI.getDescription());

        return timeRecordRepository.save(timeRecord);
    }
}
