package team_iproject_main.model.Mapper;


import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.PortfolioDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortfolioFinderRowMapper implements RowMapper<PortfolioDO> {

    @Override
    public PortfolioDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortfolioDO finderfolio = new PortfolioDO();
        finderfolio.setEmail((rs.getString("email")));
        finderfolio.setPortfolio_title(rs.getString("portfolio_title"));
        finderfolio.setNickname(rs.getString("nickname"));
        finderfolio.setPost_date(rs.getDate("post_date").toLocalDate());
        finderfolio.setIspublic(rs.getString("is_public"));

        return finderfolio;
    }
}
