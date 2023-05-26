package team_iproject_main.model.Request;

import lombok.Data;

@Data
public class FindPasswordRequest {
    String email;
    String name;
    String phone_number;
}
