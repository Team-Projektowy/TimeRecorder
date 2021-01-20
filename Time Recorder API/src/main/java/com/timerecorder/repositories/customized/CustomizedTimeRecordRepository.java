package com.timerecorder.repositories.customized;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;

import java.time.LocalDate;

public interface CustomizedTimeRecordRepository {
    Iterable<TimeRecord> findAllByUserAndStartingDate(User user, LocalDate startingDate);
    Iterable<TimeRecord> findAllByUserAndTaskAndStartingDate(User user, Task task, LocalDate startingDate);
}
