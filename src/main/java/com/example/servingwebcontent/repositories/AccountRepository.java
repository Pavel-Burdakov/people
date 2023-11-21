package com.example.servingwebcontent.repositories;
import com.example.servingwebcontent.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
