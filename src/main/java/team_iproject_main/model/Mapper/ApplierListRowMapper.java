package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.ApplierListDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplierListRowMapper implements RowMapper<ApplierListDO> {

    @Override
    public ApplierListDO mapRow(ResultSet rs, int rowNum) throws SQLException {

        ApplierListDO applierListDO = new ApplierListDO();
        applierListDO.setRecruit_no(rs.getInt("RECRUIT_NO"));
        applierListDO.setPortfolioTitle(rs.getString("PORTFOLIO_TITLE"));
        applierListDO.setNickname(rs.getString("NICKNAME"));
        applierListDO.setEmail(rs.getString("EMAIL"));
        return applierListDO;
    }
}
