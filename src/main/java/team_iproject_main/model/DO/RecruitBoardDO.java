package team_iproject_main.model.DO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class RecruitBoardDO {
    private long recruit_no;
    private String youtuber_email;
    private String recruit_title;
    private String duty;
    private String workload;
    private String requirement;
    private String salary_detail;
    private String preference;
    private String[] edit_tools;
    private String environment;
    private String welfare;
    private String recruit_detail;
    private long salary;
    private LocalDate deadline;
    private String original_link;
    private LocalDateTime post_date;
    private String[] channel_categories;


    public RecruitBoardDO(String recruit_title, String duty, String workload, String requirement, String salary_detail, String preference,
                          String[] edit_tools, String environment, String welfare, String recruit_detail, Long salary, LocalDate deadline, String original_link, String[] channel_categories){
        this.recruit_title = recruit_title;
        this.duty = duty;
        this.workload = workload;
        this.requirement = requirement;
        this.salary_detail = salary_detail;
        this.preference = preference;
        this.edit_tools = edit_tools;
        this.environment = environment;
        this.welfare = welfare;
        this.recruit_detail = recruit_detail;
        this.salary = salary;
        this.deadline = deadline;
        this.original_link = original_link;
        this.channel_categories = channel_categories;
    }
}
