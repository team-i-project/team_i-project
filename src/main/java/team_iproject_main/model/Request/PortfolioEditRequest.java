package team_iproject_main.model.Request;

import lombok.Data;

@Data
public class PortfolioEditRequest {

    private String location; //근무가능지역
    private String[] edit_tools;
    private String YOUTUBE_CAREER;
    private String career; //다른기업경력
    private String message; //하고싶은말
    private String main_link;
    private String ckb;
    //    private String edit_link;
    private String title; //포트폴리오제목
    private String salary; //희망급여
    private String worktype; //희망근무형태
    private String toyoutuber;



}
