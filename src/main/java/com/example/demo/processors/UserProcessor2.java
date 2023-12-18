package com.example.demo.processors;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.model.User;

public class UserProcessor2 implements ItemProcessor<User, User>{

    @Override
    public User process(User user) throws Exception {
        if(user.age >= 21 && user.age <= 45) {
          //  System.out.println("user details:" + user);
        
        return user ;
    }

return null;
    }
}
