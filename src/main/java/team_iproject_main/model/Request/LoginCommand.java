package team_iproject_main.model.Request;

import lombok.Data;

@Data
public class LoginCommand {
    private String email;
    private String password;
}
