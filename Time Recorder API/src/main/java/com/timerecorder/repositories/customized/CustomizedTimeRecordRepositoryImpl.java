package com.timerecorder.repositories.customized;

import com.timerecorder.models.Task;
import com.timerecorder.models.TimeRecord;
import com.timerecorder.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

public class CustomizedTimeRecordRepositoryImpl implements CustomizedTimeRecordRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Iterable<TimeRecord> findAllByUserAndStartingDate(User user, LocalDate startingDate) {
        return entityManager.createNativeQuery(
                "SELECT * FROM time_records WHERE user_id = :userId AND CAST(starting_time AS date) = :startingDate")
                .setParameter("userId", user.getId())
                .setParameter("startingDate", startingDate)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public Iterable<TimeRecord> findAllByUserAndTaskAndStartingDate(User user, Task task, LocalDate startingDate) {
        return entityManager.createNativeQuery(
                "SELECT * FROM time_records WHERE user_id = :userId AND CAST(starting_time AS date) = :startingDate AND task_id = :taskId")
                .setParameter("userId", user.getId())
                .setParameter("startingDate", startingDate)
                .setParameter("taskId", task.getId())
                .getResultList();
    }
}
