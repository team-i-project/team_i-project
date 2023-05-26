package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.RecruitSearchDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecruitSearchRowMapper implements RowMapper<RecruitSearchDO> {

    @Override
    public RecruitSearchDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecruitSearchDO recruitSearchDO = new RecruitSearchDO();
        recruitSearchDO.setRecruitNo(rs.getInt("RECRUIT_NO"));
        recruitSearchDO.setEmail(rs.getString("YOUTUBER_EMAIL"));
        recruitSearchDO.setTitle(rs.getString("RECRUIT_TITLE"));
        recruitSearchDO.setDuty(rs.getString("DUTY"));
        recruitSearchDO.setWorkLoad(rs.getString("WORKLOAD"));
        recruitSearchDO.setRequireMent(rs.getString("REQUIREMENT"));
        recruitSearchDO.setSalaryDetail(rs.getString("SALARY_DETAIL"));
        recruitSearchDO.setPreference(rs.getString("PREFERENCE"));
        recruitSearchDO.setEnvironment(rs.getString("ENVIRONMENT"));
        recruitSearchDO.setWelfare(rs.getString("WELFARE"));
        recruitSearchDO.setRecruit_Detail(rs.getString("RECRUIT_DETAIL"));
        recruitSearchDO.setSalary(rs.getString("SALARY"));
        recruitSearchDO.setDeadline(rs.getDate("DEADLINE").toLocalDate());
        recruitSearchDO.setOriginal_link(rs.getString("ORIGINAL_LINK"));
        recruitSearchDO.setPostdate(rs.getTimestamp("POST_DATE").toLocalDateTime());
        recruitSearchDO.setChannel_name(rs.getString("CHANNEL_NAME"));
        recruitSearchDO.setChannel_photo(rs.getString("channel_photo"));

        return recruitSearchDO;
    }
}
