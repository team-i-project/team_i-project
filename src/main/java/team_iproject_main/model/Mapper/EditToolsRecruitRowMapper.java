package team_iproject_main.model.Mapper;


import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.EditToolsRecruitDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditToolsRecruitRowMapper implements RowMapper<EditToolsRecruitDO> {

    @Override
    public EditToolsRecruitDO mapRow(ResultSet rs, int rowNum) throws SQLException{
        EditToolsRecruitDO editToolsRecruitDO = new EditToolsRecruitDO();
        editToolsRecruitDO.setEdit_tools_no(rs.getInt("EDIT_TOOLS_NO"));
        editToolsRecruitDO.setRecruit_no(rs.getInt("RECRUIT_NO"));
        editToolsRecruitDO.setEdit_tool(rs.getString("EDIT_TOOL"));
        return editToolsRecruitDO;

    }
}
