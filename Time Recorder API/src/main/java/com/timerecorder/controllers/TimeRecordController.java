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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;


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
}
