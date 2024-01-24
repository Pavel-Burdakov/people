package com.example.servingwebcontent.services;
import com.example.servingwebcontent.models.MyUser;
import com.example.servingwebcontent.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public void addUser(MyUser myUser) {
        userRepository.save(myUser);
    }
}
