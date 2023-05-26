package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.ApplyEditorDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplyEditorRowMapper implements RowMapper<ApplyEditorDO> {

    @Override
    public ApplyEditorDO mapRow(ResultSet rs, int rowNum) throws SQLException{
        ApplyEditorDO applyEditorDO = new ApplyEditorDO();
        applyEditorDO.setApply_no(rs.getInt("APPLY_NO"));
        applyEditorDO.setRecruit_no(rs.getInt("RECRUIT_NO"));
        applyEditorDO.setEditor_email(rs.getString("EDITOR_EMAIL"));
        applyEditorDO.setEditor_memo(rs.getString("EDITOR_MEMO"));
        applyEditorDO.setEdited_link(rs.getString("EDITED_LINK"));
        applyEditorDO.setYoutuber_memo(rs.getString("YOUTUBER_MEMO"));
        return applyEditorDO;

    }
}
