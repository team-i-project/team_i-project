package team_iproject_main.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecruitSearchDO { //구인글 (유튜버)
    private int recruitNo; //게시글 숫자? fk값?
    private String email; //유튜버 계정
    private String title;   //제목
    private String duty;    // 직무
    private String workLoad;    // 편집 월 몇건
    private String RequireMent; // 군필 or 면제 ? 경력?
    private String salaryDetail;    //
    private String preference;  //우대사항
    private String environment; //근무 형태
    private String welfare;     //복리 후생
    private String recruit_Detail;  // 상세 모집 내용
    private String salary;      // 급여 상세
    private LocalDate deadline; //마감일      LocalDateTime  // LocalDate 둘중 무엇을 쓸지 나중에 수정 해야 합니다
    private String original_link;
    private LocalDateTime postdate;
    private String channel_name;
    private String channel_photo;

    @Override
    public String toString() {
        return recruitNo + title + deadline + channel_name;
    }

}
