package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.UserDO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDO> {

    @Override
    public UserDO mapRow(ResultSet rs, int rowNum) throws SQLException{
        UserDO user = new UserDO();
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setNickname(rs.getString("nickname"));
        user.setPhone_number(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setDetail_addr(rs.getString("detail_addr"));
        user.setUser_type(rs.getString("user_type"));
        user.setGender(rs.getString("gender"));
        user.setBirth_date(rs.getDate("birth_date").toLocalDate());
        user.setRegister_date(rs.getTimestamp("register_date").toLocalDateTime());
        user.setProfile_photo(rs.getString("profile_photo"));

        return user;
    }

}
