package team_iproject_main.model.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDO {
    private String email;
    private String nickname;
    private String ispublic;
    private String workable_location;
    private String YOUTUBE_CAREER;
    private String othercareer; //경력
    private String message; //할말
    private String portfolio_title;   //제목
    private LocalDate post_date;
    private String desiredsalary;
    private String desiredworktype;
    private String messagetoyoutuber;

    public PortfolioDO(String email, String portfolio_title, String nickname, LocalDate post_date) {
        this.email = email;
        this.portfolio_title = portfolio_title;
        this.nickname = nickname;
        this.post_date = post_date;
    }






    //IS_PUBLIC 부분이 true면 보이게
}
