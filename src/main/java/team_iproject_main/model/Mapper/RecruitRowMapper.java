package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.RecruitDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecruitRowMapper implements RowMapper<RecruitDO> {

    @Override
    public RecruitDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecruitDO recruitDO = new RecruitDO();
        recruitDO.setRecruitNo(rs.getInt("RECRUIT_NO"));
        recruitDO.setEmail(rs.getString("YOUTUBER_EMAIL"));
        recruitDO.setTitle(rs.getString("RECRUIT_TITLE"));
        recruitDO.setDuty(rs.getString("DUTY"));
        recruitDO.setWorkLoad(rs.getString("WORKLOAD"));
        recruitDO.setRequireMent(rs.getString("REQUIREMENT"));
        recruitDO.setSalaryDetail(rs.getString("SALARY_DETAIL"));
        recruitDO.setPreference(rs.getString("PREFERENCE"));
        recruitDO.setEnvironment(rs.getString("ENVIRONMENT"));
        recruitDO.setWelfare(rs.getString("WELFARE"));
        recruitDO.setRecruit_Detail(rs.getString("RECRUIT_DETAIL"));
        recruitDO.setSalary(rs.getString("SALARY"));
        recruitDO.setDeadline(rs.getDate("DEADLINE").toLocalDate());
        recruitDO.setOriginal_link(rs.getString("ORIGINAL_LINK"));
        recruitDO.setPostdate(rs.getTimestamp("POST_DATE").toLocalDateTime());

        return recruitDO;
    }
}
