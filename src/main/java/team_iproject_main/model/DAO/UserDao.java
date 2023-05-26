package team_iproject_main.model.DAO;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import team_iproject_main.model.DO.UserDO;
import team_iproject_main.model.Mapper.UserRowMapper;
import team_iproject_main.model.Mapper.YoutuberRowMapper;
import team_iproject_main.model.Request.RegisterReqeustChannel;
import team_iproject_main.model.Request.UserSearchRequest;
import team_iproject_main.model.Request.UserUpdateRequest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Log4j2
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void createEditor(UserDO user) throws UnsupportedEncodingException {

        String password = encoder.encode(user.getPassword());
        user.setPassword(password);

        String sql = "INSERT INTO USER_INFO(EMAIL, PASSWORD, NAME, NICKNAME, PHONE_NUMBER, ADDRESS, DETAIL_ADDR, USER_TYPE, GENDER, BIRTH_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getName(), user.getNickname(), user.getPhone_number(),
                user.getAddress(), user.getDetail_addr(), user.getUser_type(), user.getGender(), user.getBirth_date());

        String sqluqid = "INSERT INTO USER_EDITOR(EDITOR_EMAIL, IS_UPLOADED) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqluqid, user.getEmail(), "FALSE");
    }

    // 겸손 추가
    public void createYoutuber(UserDO user) {

        String password = encoder.encode(user.getPassword());
        user.setPassword(password);

        String sql = "INSERT INTO USER_INFO(EMAIL, PASSWORD, NAME, NICKNAME, PHONE_NUMBER, ADDRESS, DETAIL_ADDR, USER_TYPE, GENDER, BIRTH_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getName(), user.getNickname(), user.getPhone_number(),
                user.getAddress(), user.getDetail_addr(), user.getUser_type(), user.getGender(), user.getBirth_date());

        String sql1 = "INSERT INTO USER_YOUTUBER(YOUTUBER_EMAIL, CHANNEL_ID, SUBSCRIBE, VIDEO_COUNT, VIEW_COUNT, CHANNEL_NAME, CHANNEL_PHOTO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql1, user.getEmail(), user.getChannel_id(), user.getSubscribe(), user.getVideo_count(), user.getView_count(), user.getChannel_name(), user.getChannel_photo());
    }

    public UserDO findByEmail(String email) {
        String sql = "SELECT * FROM user_info WHERE email = ?";
        UserDO user = null;

        try{
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            log.info("userDAO"+ user);
        }
        catch (EmptyResultDataAccessException e){
        }
        return user;
    }

    public UserDO findByNickname1(String nickname) {
        String sql = "SELECT * FROM user_info WHERE nickname = ?";
        UserDO user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), nickname);
            log.info("userDAO"+ user);
        }
        catch (EmptyResultDataAccessException e){
        }
        return user;
    }

    public UserDO findByPhoneNumber(String phone_number) {
        String sql = "SELECT * FROM user_info WHERE phone_number = ?";
        UserDO user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), phone_number);
            log.info("userDAO"+ user);
        }
        catch (EmptyResultDataAccessException e){
        }
        return user;
    }


    //희수
    //관리자 회원 검색
    // 준영 페이징 추가
    public List<UserDO> userFindById(UserSearchRequest userSearchRequest, int postsPerPage, int offset) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, u.* FROM USER_INFO u) " +
                "WHERE " + userSearchRequest.getJob() + " like '%" + userSearchRequest.getSearchtext() + "%' " +
                "AND rn BETWEEN ? AND ?";

        List<UserDO> result = jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new UserRowMapper());
        return result;
    }

    // 준영 페이징 추가
    public int getTotalSearch(UserSearchRequest userSearchRequest) {
        return jdbcTemplate.queryForObject("select COUNT(*) from USER_INFO where " + userSearchRequest.getJob() + " like '%" + userSearchRequest.getSearchtext() + "%'", Integer.class);
    }

    //희수
    //유저 전체 조회
    //준영 페이징 추가
    public List<UserDO> findAll(int postsPerPage, int offset) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, u.* FROM USER_INFO u) WHERE rn BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new UserRowMapper());
    }

    public int getTotalResults() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USER_INFO", Integer.class);
    }

    //0506-손주현
    //name이랑 핸드폰 번호로 유저 찾기
    public UserDO findByNameAndPhone(String name, String phone_number){
        String sql = "SELECT * FROM USER_INFO WHERE NAME = ? AND PHONE_NUMBER = ?";
        UserDO user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), name, phone_number);
        }
        catch (EmptyResultDataAccessException e){
        }
        return user;
    }


    public UserDO findByNickname(String nickname){
        String sql = "SELECT * FROM user_info WHERE nickname = ?";
        UserDO user = null;

        try{
            user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), nickname);
        }
        catch (EmptyResultDataAccessException e){
        }
        return user;
    }

    public RegisterReqeustChannel findByChannel(String channel_id) {
        String sql = "SELECT * FROM USER_YOUTUBER WHERE CHANNEL_ID = ?";
        RegisterReqeustChannel uq = null;
        try {
            uq = jdbcTemplate.queryForObject(sql, new YoutuberRowMapper(), channel_id);
        }
        catch (EmptyResultDataAccessException e) {
        }
        return uq;
    }

    public void updatePassword(String email, String newpwd) {
        String password = encoder.encode(newpwd);
        newpwd = password;

        String sql = "update user_info set password = ? where email = ?";
        jdbcTemplate.update(sql, newpwd, email);
    }

    public void updateUserInfo(UserUpdateRequest userUpdateRequest, String email) {
        String sql = "update user_info set nickname=?,name=?,phone_number=?,address=?,detail_addr=?,birth_date=? where email=?";
        jdbcTemplate.update(sql,userUpdateRequest.getNickname(),userUpdateRequest.getName(), userUpdateRequest.getPhone_number(),
                userUpdateRequest.getAddress(),userUpdateRequest.getDetail_addr(), LocalDate.parse(userUpdateRequest.getBirth_date()), email);

    }

    //0511- 손주현
    public void deleteUserInfo(String email){
        String sql = "DELETE FROM USER_INFO WHERE EMAIL = ?";
        jdbcTemplate.update(sql, email);
    }
}
