package com.timerecorder.repositories;

import com.timerecorder.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findFirstByEmail(String email);
}
