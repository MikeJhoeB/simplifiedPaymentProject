package com.paymentProject.dtos;

import com.paymentProject.enums.UserType;

public record UserDTO(String firstName,
                      String lastName,
                      String document,
                      String email,
                      String password,
                      UserType userType) {
}
