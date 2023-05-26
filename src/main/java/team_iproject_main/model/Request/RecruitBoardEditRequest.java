package team_iproject_main.model.Request;

import lombok.Data;

@Data
public class RecruitBoardEditRequest {
    private String recruit_title;
    private String deadline;
    private String duty;
    private String workload;
    private String requirement;
    private long salary;
    private String salary_detail;
    private String preference;
    private String[] edit_tools;
    private String environment;
    private String welfare;
    private String recruit_detail;
    private String original_link;
    private String[] channel_categories;

}
