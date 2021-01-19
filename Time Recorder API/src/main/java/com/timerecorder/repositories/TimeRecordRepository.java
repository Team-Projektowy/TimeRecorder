package com.timerecorder.repositories;

import com.timerecorder.models.TimeRecord;
import org.springframework.data.repository.CrudRepository;

public interface TimeRecordRepository extends CrudRepository<TimeRecord, Integer> {
}
