package team_iproject_main.model.Request;


import lombok.Data;

@Data
public class RegisterReqeustChannel {
    private String channel_id;
    private String youtuber_email;
    private Long subscribe;
    private Long video_count;
    private Long view_count;
    private String channel_name;
    private String channel_photo;
}
