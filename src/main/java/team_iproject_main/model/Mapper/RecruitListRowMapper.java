package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.RecruitSearchDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecruitListRowMapper implements RowMapper<RecruitSearchDO> {


    @Override
    public RecruitSearchDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecruitSearchDO recruitSearchDO = new RecruitSearchDO();
        recruitSearchDO.setRecruitNo(rs.getInt("RECRUIT_NO"));
        recruitSearchDO.setTitle(rs.getString("RECRUIT_TITLE"));
        recruitSearchDO.setDeadline(rs.getDate("DEADLINE").toLocalDate());
        recruitSearchDO.setChannel_name(rs.getString("channel_name"));
        recruitSearchDO.setChannel_photo(rs.getString("channel_photo"));

        return recruitSearchDO;
    }
}
