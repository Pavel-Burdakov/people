package com.example.servingwebcontent.repositories;
import com.example.servingwebcontent.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByName(String userName);
}
