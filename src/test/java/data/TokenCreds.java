package data;

import lombok.Data;


@Data
public class TokenCreds {
    private String username = System.getProperty("username");
    private String password = System.getProperty("password");

}
