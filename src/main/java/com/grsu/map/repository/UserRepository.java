package com.grsu.map.repository;

import com.grsu.map.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String namePage);
    User findUserById(Long id);
}
