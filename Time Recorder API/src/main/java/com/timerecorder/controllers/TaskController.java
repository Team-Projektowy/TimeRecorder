package com.timerecorder.controllers;

import com.timerecorder.helpers.requestProcessingHelper;
import com.timerecorder.models.Task;
import com.timerecorder.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public Iterable<Task> getAll() {
        return taskRepository.findAll();
    }

    @GetMapping(path = "/{taskId}")
    public Task getById(@PathVariable Integer taskId, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        return task.get();
    }

    @PostMapping
    public Task create(@Valid @RequestBody Task task, HttpServletRequest request, HttpServletResponse response) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        if(taskRepository.findTaskByName(task.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task of that name already exists.");
        }

        Task newTask = taskRepository.save(task);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return newTask;
    }

    @PutMapping(path = "/{taskId}")
    public Task edit(@PathVariable Integer taskId, @Valid @RequestBody Task task, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        Optional<Task> curTask = taskRepository.findById(taskId);
        if (curTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        task.setId(taskId);
        taskRepository.save(task);
        return task;
    }

    @DeleteMapping(path = "/{taskId}")
    public void delete(@PathVariable Integer taskId, HttpServletRequest request) {
        requestProcessingHelper.throwUnauthorizedIfRequesterIsNotAdmin(request);

        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        taskRepository.delete(task.get());
    }
}
