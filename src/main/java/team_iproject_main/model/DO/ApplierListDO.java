package team_iproject_main.model.DO;

import lombok.Data;

@Data
public class ApplierListDO {
    // 지원자 목록 List 보여주는 클래스
    private int apply_no;
    private int recruit_no;
    private String portfolioTitle;
    private String nickname;
    private String email;
}
