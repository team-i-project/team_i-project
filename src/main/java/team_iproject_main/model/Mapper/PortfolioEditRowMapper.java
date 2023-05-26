package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.PortfolioEditDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioEditRowMapper implements RowMapper<PortfolioEditDO> {

    @Override
    public PortfolioEditDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortfolioEditDO portfolioEditDO = new PortfolioEditDO();
        portfolioEditDO.setGender(rs.getString("GENDER"));
        portfolioEditDO.setName(rs.getString("NAME"));
        portfolioEditDO.setYOUTUBE_CAREER(rs.getString("YOUTUBE_CAREER"));
        portfolioEditDO.setEdit_email(rs.getString("EDITOR_EMAIL"));
        portfolioEditDO.setLocation(rs.getString("WORKABLE_LOCATION"));
        portfolioEditDO.setCareer(rs.getString("OTHER_CAREER"));
        portfolioEditDO.setMessage(rs.getString("MESSAGE"));
        portfolioEditDO.setTitle(rs.getString("PORTFOLIO_TITLE"));
        portfolioEditDO.setSalary(rs.getString("DESIRED_SALARY"));
        portfolioEditDO.setWorktype(rs.getString("DESIRED_WORK_TYPE"));
        portfolioEditDO.setToyoutuber(rs.getString("MESSAGE_TO_YOUTUBER"));
        portfolioEditDO.setIs_public(rs.getString("IS_PUBLIC"));
        return portfolioEditDO;
    }
}
