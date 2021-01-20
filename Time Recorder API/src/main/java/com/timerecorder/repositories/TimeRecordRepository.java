package com.timerecorder.repositories;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;
import com.timerecorder.repositories.customized.CustomizedTimeRecordRepository;
import org.springframework.data.repository.CrudRepository;

public interface TimeRecordRepository extends CrudRepository<TimeRecord, Integer>, CustomizedTimeRecordRepository {
    Iterable<TimeRecord> findAllByUser(User user);
    Iterable<TimeRecord> findAllByUserAndTask(User user, Task task);
}
