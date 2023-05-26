package team_iproject_main.model.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSearchRequest {
    private String job;
    private String searchtext;
}
