package com.lifekhmer.authentication.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{

    private boolean success;

    private String message;

    private T data;
}
