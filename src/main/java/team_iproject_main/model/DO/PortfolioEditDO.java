package team_iproject_main.model.DO;

import lombok.Data;


//5.12 양서림 수정
@Data
public class PortfolioEditDO {
    private String gender;
    private String name;
    private String edit_email;
    private String location; //근무가능지역
    private String[] edit_tools;
    private String YOUTUBE_CAREER;
    private String career; //다른기업경력
    private String message; //하고싶은말
    private String main_link;
    private String edit_link;
    private String is_public;
    private String title; //포트폴리오제목
    private String salary; //희망급여
    private String worktype; //희망근무형태
    private String toyoutuber;

    public PortfolioEditDO(String edit_email) {
        this.edit_email = edit_email;
    }

    public PortfolioEditDO(String location, String[] edit_tools, String YOUTUBE_CAREER, String career, String message, String main_link, String title, String salary, String worktype, String toyoutuber) {
        this.location = location;
        this.edit_tools = edit_tools;
        this.YOUTUBE_CAREER = YOUTUBE_CAREER;
        this.career = career;
        this.message = message;
        this.main_link = main_link;
        this.title = title;
        this.salary = salary;
        this.worktype = worktype;
        this.toyoutuber = toyoutuber;
    }

    public PortfolioEditDO() {

    }
}
