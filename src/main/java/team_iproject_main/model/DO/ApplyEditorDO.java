package team_iproject_main.model.DO;

import lombok.Data;

@Data
public class ApplyEditorDO {
    private int apply_no;
    private int recruit_no;
    private String editor_email;
    private String edited_link;
    private String editor_memo;
    private String youtuber_memo;
}
