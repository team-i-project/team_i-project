package team_iproject_main.model.Request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String nickname;
    private String phone_number;
    private String address;
    private String detail_addr;
    private String gender;
    private String birth_date;
    private String channel_id;
    private Long subscribe;
    private Long video_count;
    private Long view_count;
    private String channel_name;
    private String channel_photo;
}
