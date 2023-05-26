package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.MyRecruitDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyRecruitRowMapper implements RowMapper<MyRecruitDO>  {
    @Override
    public MyRecruitDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MyRecruitDO myRecruitDO = new MyRecruitDO();
        myRecruitDO.setRecruit_no(rs.getLong("recruit_no"));
        myRecruitDO.setRecruit_title(rs.getString("recruit_title"));
        myRecruitDO.setDeadline(rs.getDate("deadline").toLocalDate());
        myRecruitDO.setPost_date(rs.getTimestamp("post_date").toLocalDateTime());
        return myRecruitDO;
    }
}