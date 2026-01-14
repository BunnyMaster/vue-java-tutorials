package cn.bunny.model.order.bean;

import lombok.Data;

@Data
public class LoginDto {
    String username;
    String password;
    String type;
}