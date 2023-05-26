package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.ChannelCategoryDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelCategoryRowMapper implements RowMapper<ChannelCategoryDO> {

    @Override
    public ChannelCategoryDO mapRow(ResultSet rs, int rowNum) throws SQLException{
        ChannelCategoryDO channelCategoryDO = new ChannelCategoryDO();
        channelCategoryDO.setCategory_no(rs.getInt("CATEGORY_NO"));
        channelCategoryDO.setRecruit_no(rs.getInt("RECRUIT_NO"));
        channelCategoryDO.setCategory(rs.getString("CATEGORY"));
        return channelCategoryDO;
    }

}
