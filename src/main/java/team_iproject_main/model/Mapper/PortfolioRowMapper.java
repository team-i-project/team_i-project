package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.PortfolioDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioRowMapper implements RowMapper<PortfolioDO> {

    @Override
    public PortfolioDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortfolioDO portfolioDO = new PortfolioDO();
        portfolioDO.setEmail(rs.getString("EDITOR_EMAIL"));
        portfolioDO.setNickname(rs.getString("NICKNAME"));
        portfolioDO.setIspublic(rs.getString("IS_PUBLIC"));
        portfolioDO.setWorkable_location(rs.getString("WORKABLE_LOCATION"));
        portfolioDO.setOthercareer(rs.getString("OTHER_CAREER"));
        portfolioDO.setYOUTUBE_CAREER(rs.getString("YOUTUBE_CAREER"));
        portfolioDO.setMessage(rs.getString("MESSAGE"));
        portfolioDO.setPortfolio_title(rs.getString("PORTFOLIO_TITLE"));
        portfolioDO.setPost_date(rs.getDate("POST_DATE").toLocalDate());
        portfolioDO.setDesiredsalary(rs.getString("DESIRED_SALARY"));
        portfolioDO.setDesiredworktype(rs.getString("DESIRED_WORK_TYPE"));
        portfolioDO.setMessagetoyoutuber(rs.getString("MESSAGE_TO_YOUTUBER"));
        return portfolioDO;
    }
}