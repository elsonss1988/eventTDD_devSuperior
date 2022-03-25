package com.devsuperior.bds04.dto;


import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.services.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO{
    private String password;

    public UserUpdateDTO(){
        super();
    }

    public UserUpdateDTO(User entity) {super();}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

