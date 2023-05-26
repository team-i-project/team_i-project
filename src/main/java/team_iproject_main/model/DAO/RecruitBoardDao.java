package team_iproject_main.model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import team_iproject_main.model.DO.*;
import team_iproject_main.model.Mapper.*;
import team_iproject_main.model.Request.RequestKeyword;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class RecruitBoardDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createRecruitBoard(RecruitBoardDO recruitDO) {

        String sql = "INSERT INTO RECRUIT_BOARD(RECRUIT_NO, YOUTUBER_EMAIL, RECRUIT_TITLE, DUTY, WORKLOAD, REQUIREMENT, " +
                "SALARY_DETAIL, PREFERENCE, ENVIRONMENT, WELFARE, RECRUIT_DETAIL, SALARY, DEADLINE, ORIGINAL_LINK) " +
                "VALUES (RECRUIT_BOARD_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[] { "RECRUIT_NO" });
            ps.setString(1, recruitDO.getYoutuber_email());
            ps.setString(2, recruitDO.getRecruit_title());
            ps.setString(3, recruitDO.getDuty());
            ps.setString(4, recruitDO.getWorkload());
            ps.setString(5, recruitDO.getRequirement());
            ps.setString(6, recruitDO.getSalary_detail());
            ps.setString(7, recruitDO.getPreference());
            ps.setString(8, recruitDO.getEnvironment());
            ps.setString(9, recruitDO.getWelfare());
            ps.setString(10, recruitDO.getRecruit_detail());
            ps.setLong(11, recruitDO.getSalary());
            ps.setDate(12, Date.valueOf(recruitDO.getDeadline()));
            ps.setString(13, recruitDO.getOriginal_link());
            return ps;
        }, keyHolder);

        int recruitNo = keyHolder.getKey().intValue();

        String sqltools = "INSERT INTO EDIT_TOOLS_RECRUIT(EDIT_TOOLS_NO, RECRUIT_NO, EDIT_TOOL) VALUES (EDIT_TOOLS_RECRUIT_SEQ.NEXTVAL, ?, ?)";
        for(String tool : recruitDO.getEdit_tools()){
            jdbcTemplate.update(sqltools, recruitNo, tool);
        }

        String sqlcategories = "INSERT INTO CHANNEL_CATEGORY(CATEGORY_NO, RECRUIT_NO, CATEGORY) VALUES (CHANNEL_CATEGORY_SEQ.NEXTVAL, ?, ?)";
        for(String category : recruitDO.getChannel_categories()){
            jdbcTemplate.update(sqlcategories, recruitNo, category);
        }
    }

    //희수
    //게시글 상세조회
    public RecruitDO selectRecruitPost(int recruitNo) {
        return jdbcTemplate.queryForObject("select * from recruit_board where RECRUIT_NO = ? " , new RecruitRowMapper(), recruitNo);
    }

    //준원
    //구인글 게시판에서 활용할 유튜버 채널사진, 채널명 가져오는 메서드
    public YoutuberDO selectYoutuberInfo(int recruitNo) {
        String sql = "SELECT Y.CHANNEL_PHOTO, Y.CHANNEL_NAME, Y.YOUTUBER_EMAIL, Y.CHANNEL_ID, Y.SUBSCRIBE, Y.VIDEO_COUNT, Y.VIEW_COUNT " +
                "FROM (USER_YOUTUBER Y) JOIN (RECRUIT_BOARD R) ON (Y.YOUTUBER_EMAIL LIKE R.YOUTUBER_EMAIL) " +
                "WHERE RECRUIT_NO = ?";
        return jdbcTemplate.queryForObject(sql, new RecruitYoutuberInfoRowMapper(), recruitNo);
    }

    //주현 0511
    //편집자가 마이페이지 - 지원현황 : 자신만 지원한 구인글 내림차순 추출
    // 준영 페이징 추가
    public List<RecruitDO> findAllApplyByEmail(String email, int postsPerPage, int offset){
        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, rb.* FROM (SELECT * FROM RECRUIT_BOARD WHERE RECRUIT_NO IN" +
                "(SELECT RECRUIT_NO FROM APPLY_EDITOR WHERE EDITOR_EMAIL = ?) ORDER BY DEADLINE, RECRUIT_NO) rb) WHERE rn BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, new Object[]{email, offset + 1, offset + postsPerPage}, new RecruitRowMapper());
    }

    // 준영 페이징 추가
    public int getTotalApply(String email) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM RECRUIT_BOARD WHERE RECRUIT_NO IN (SELECT RECRUIT_NO FROM APPLY_EDITOR WHERE EDITOR_EMAIL = ?)", Integer.class, email);
    }

    public List<ChannelCategoryDO> getCategories(int recruitNo){
        String sql = "SELECT * FROM CHANNEL_CATEGORY WHERE RECRUIT_NO = ?";
        return jdbcTemplate.query(sql, new ChannelCategoryRowMapper(), recruitNo);
    }

    public List<EditToolsRecruitDO> getTools(int recruitNo){
        String sql = "SELECT * FROM EDIT_TOOLS_RECRUIT WHERE RECRUIT_NO = ?";
        return jdbcTemplate.query(sql, new EditToolsRecruitRowMapper(), recruitNo);
    }

    public void modifyRecruitBoard(RecruitBoardDO recruitDO , int recruitNo, String email){
        String sql = "UPDATE RECRUIT_BOARD SET RECRUIT_TITLE = ?, DUTY = ?, WORKLOAD = ?, REQUIREMENT = ?, " +
                "SALARY_DETAIL = ?, PREFERENCE = ?, ENVIRONMENT = ?, WELFARE = ?, RECRUIT_DETAIL = ?, SALARY = ?, DEADLINE = ?, ORIGINAL_LINK = ? " +
                "WHERE RECRUIT_NO = ? AND YOUTUBER_EMAIL = ?";

        jdbcTemplate.update(sql,recruitDO.getRecruit_title(), recruitDO.getDuty(), recruitDO.getWorkload(), recruitDO.getRequirement(),
                recruitDO.getSalary_detail(), recruitDO.getPreference(), recruitDO.getEnvironment(), recruitDO.getWelfare(), recruitDO.getRecruit_detail(),
                recruitDO.getSalary(), recruitDO.getDeadline(), recruitDO.getOriginal_link(), recruitNo, email);

        jdbcTemplate.update("DELETE FROM EDIT_TOOLS_RECRUIT WHERE RECRUIT_NO = ?", recruitNo);
        jdbcTemplate.update("DELETE FROM CHANNEL_CATEGORY WHERE RECRUIT_NO = ?", recruitNo);

        String sqltools = "INSERT INTO EDIT_TOOLS_RECRUIT(EDIT_TOOLS_NO, RECRUIT_NO, EDIT_TOOL) VALUES (EDIT_TOOLS_RECRUIT_SEQ.NEXTVAL, ?, ?)";
        for(String tool : recruitDO.getEdit_tools()){
            jdbcTemplate.update(sqltools, recruitNo, tool);
        }

        String sqlcategories = "INSERT INTO CHANNEL_CATEGORY(CATEGORY_NO, RECRUIT_NO, CATEGORY) VALUES (CHANNEL_CATEGORY_SEQ.NEXTVAL, ?, ?)";
        for(String category : recruitDO.getChannel_categories()){
            jdbcTemplate.update(sqlcategories, recruitNo, category);
        }

    }

    //희수
    //게시글 조회
    // 준영 페이징 추가
    public List<RecruitSearchDO> findRecruit(int postsPerPage, int offset) {
        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, rb.* FROM (select * from recruit_board r join user_youtuber y on r.youtuber_email = y.youtuber_email ORDER BY r.RECRUIT_NO DESC) rb) WHERE rn BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new RecruitSearchRowMapper());
    }

    // 준영 페이징 추가
    public int getTotalPosts() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM RECRUIT_BOARD", Integer.class);
    }

    //희수
    //구인글  삭제
    public void deleteRecruitPost(int recruitNo) {
        jdbcTemplate.update("DELETE FROM recruit_board WHERE RECRUIT_NO = ?", recruitNo);
    }

    // 겸손 구인글 검색(OR 연산)
    public List<RecruitSearchDO> SearchFinder(RequestKeyword keywordDO, int postsPerPage, int offset) {
        String[] tools = keywordDO.getEdit_tools_no();

        String sql = "select * from (select ROWNUM rn, rb.* from(select distinct r.recruit_no, r.recruit_title, r.deadline, y.channel_name, y.channel_photo " +
                "from recruit_board r " +
                "join edit_tools_recruit e on r.recruit_no = e.recruit_no " +
                "join user_youtuber y on r.youtuber_email = y.youtuber_email " +
                "join channel_category c on r.recruit_no = c.recruit_no " +
                "where r.recruit_title LIKE '%" + keywordDO.getSearch_text() + "%' ";

        if(tools.length != 0) {
            sql += "and e.edit_tool IN (";
            for(String tool : tools) {
                sql += "'" + tool + "',";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
        }

        sql += " and y.subscribe >= " + keywordDO.getSubscribe_no() +
                " and r.salary >= " + keywordDO.getSalary_no() +
                " and r.environment LIKE '%" + keywordDO.getEnvironment_no() + "%'" +
                " order by r.recruit_no desc) rb ) where rn between ? and ? ";

        return jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new RecruitListRowMapper());

    }

    public int getSearchTotalPosts(RequestKeyword keywordDO) {
        String[] tools = keywordDO.getEdit_tools_no();

        String sql = "select count(*) from (select ROWNUM rn, rb.* from(select distinct r.recruit_no, r.recruit_title, r.deadline, y.channel_name " +
                "from recruit_board r " +
                "join edit_tools_recruit e on r.recruit_no = e.recruit_no " +
                "join user_youtuber y on r.youtuber_email = y.youtuber_email " +
                "join channel_category c on r.recruit_no = c.recruit_no " +
                "where r.recruit_title LIKE '%" + keywordDO.getSearch_text() + "%' ";

        if(tools.length != 0) {
            sql += "and e.edit_tool IN (";
            for(String tool : tools) {
                sql += "'" + tool + "',";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
        }

        sql += " and y.subscribe >= " + keywordDO.getSubscribe_no() +
                " and r.salary >= " + keywordDO.getSalary_no() +
                " and r.environment LIKE '%" + keywordDO.getEnvironment_no() + "%'" +
                " ) rb ) ";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 준원
    // 마이페이지 -> 작성한 구인글
    // 준영 페이징 추가
    public List<MyRecruitDO> findMyRecruit(String youtuber_email, int postsPerPage, int offset) {
        String sql = "select * from (SELECT ROWNUM AS rn, rb.* from (select * from recruit_board where youtuber_email = '" + youtuber_email + "' order by recruit_no desc) rb) where rn between ? and ?";
        return jdbcTemplate.query(sql, new Object[]{offset + 1, offset + postsPerPage}, new MyRecruitRowMapper());
    }

    // 준영 페이징 추가
    public int getTotalRecruits(String youtuber_email) {
        return jdbcTemplate.queryForObject("select COUNT(*) from recruit_board where youtuber_email = ?", Integer.class, youtuber_email);
    }

    // 겸손 구인글 검색(AND 연산)
//    public List<RecruitDO> SearchFinder(RequestKeyword keywordDO) {
//        String[] tools = keywordDO.getEdit_tools_no();
//
//        String sql = "select distinct r.recruit_no, r.recruit_title, r.deadline, y.channel_name " +
//                "from recruit_board r " +
//                "join user_youtuber y on r.youtuber_email = y.youtuber_email " +
//                "join channel_category c on r.recruit_no = c.recruit_no " +
//                "where r.recruit_title LIKE '%" + keywordDO.getSearch_text() + "%' " +
//                "and y.subscribe >= " + keywordDO.getSubscribe_no() +
//                " and r.salary >= " + keywordDO.getSalary_no() +
//                " and r.environment LIKE '%" + keywordDO.getEnvironment_no() + "%' " +
//                " AND r.recruit_no IN (" +
//                "   SELECT e.recruit_no " +
//                "   FROM edit_tools_recruit e " +
//                "   WHERE e.edit_tool IN (";
//        for(String tool : tools) {
//            sql += "'" + tool + "',";
//        }
//        sql = sql.substring(0, sql.length() - 1);
//        sql +=      ")   GROUP BY e.recruit_no " +
//                "   HAVING COUNT(DISTINCT e.edit_tool) = " + tools.length +
//                ")";
//
//        List<RecruitDO> recruitDO = jdbcTemplate.query(sql, new RecuritListRowMapper());
//
//        return recruitDO;
//    }
}
