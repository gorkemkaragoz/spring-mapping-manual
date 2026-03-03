package com.gorkem.manuelmap.repo;

import com.gorkem.manuelmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
