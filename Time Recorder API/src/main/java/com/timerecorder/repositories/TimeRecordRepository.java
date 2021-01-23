package com.timerecorder.repositories;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface TimeRecordRepository extends CrudRepository<TimeRecord, Integer> {
    Iterable<TimeRecord> findAllByUser(User user);
    Iterable<TimeRecord> findAllByUserAndTask(User user, Task task);
    Iterable<TimeRecord> findAllByUserAndStartingTimeBetweenOrderByStartingTime(User user, LocalDateTime startingTimeFrom, LocalDateTime startingTimeTo);
    Iterable<TimeRecord> findAllByUserAndTaskAndStartingTimeBetweenOrderByStartingTime(User user, Task task, LocalDateTime startingTimeFrom, LocalDateTime startingTimeTo);
}
