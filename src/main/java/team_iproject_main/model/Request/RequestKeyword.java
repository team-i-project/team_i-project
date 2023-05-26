package team_iproject_main.model.Request;


import lombok.Data;

@Data
public class RequestKeyword {

     private String search_text;
     private Long subscribe_no;
     private Long salary_no;
     private String environment_no;
     private String[] edit_tools_no;
}
