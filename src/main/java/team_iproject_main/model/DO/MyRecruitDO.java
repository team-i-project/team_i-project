package team_iproject_main.model.DO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class MyRecruitDO {
    // 마이페이지 -> 작성한 구인글 보기에서 List 보여주는 클래스
    private Long          recruit_no;
    private String        recruit_title;
    private LocalDate     deadline;
    private LocalDateTime post_date;
}