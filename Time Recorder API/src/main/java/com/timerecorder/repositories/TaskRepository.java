package com.timerecorder.repositories;

import com.timerecorder.models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    Optional<Task> findTaskByName(String name);
}
