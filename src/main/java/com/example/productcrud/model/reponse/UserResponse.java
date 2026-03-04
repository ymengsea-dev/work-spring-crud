package com.example.productcrud.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String userName;
    private List<String> roles;
}
