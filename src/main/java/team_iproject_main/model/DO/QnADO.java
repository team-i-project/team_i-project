package team_iproject_main.model.DO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QnADO {
    private int qnano;
    private String email;
    private String nickname;
    private String question;
    private String answer;
    private LocalDate postdate;
}
