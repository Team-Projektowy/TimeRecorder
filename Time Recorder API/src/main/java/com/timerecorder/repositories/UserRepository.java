package com.timerecorder.repositories;

import com.timerecorder.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findFirstByEmail(String email);
}
