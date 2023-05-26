package team_iproject_main.model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import team_iproject_main.model.DO.ApplierListDO;
import team_iproject_main.model.DO.ApplyEditorDO;
import team_iproject_main.model.DO.UserEditorDO;
import team_iproject_main.model.Mapper.ApplierListRowMapper;
import team_iproject_main.model.Mapper.ApplyEditorRowMapper;
import team_iproject_main.model.Mapper.EditorRowMapper;

import java.util.List;

@Repository
public class ApplyEditorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //주현 0511
    //구인글에 지원한 지원자 넣기
    public void addApplyEditor(int recruitNO, String email){
        String sql = "INSERT INTO APPLY_EDITOR(APPLY_NO, RECRUIT_NO, EDITOR_EMAIL) VALUES(APPLY_EDITOR_SEQ.NEXTVAL,?,?)";
        jdbcTemplate.update(sql,recruitNO,email);
    }

    //주현 0511
    //구인글에 지원한 지원자를 넣을 때 중복을 방지하기 위해서 만든 것
    public ApplyEditorDO findApplyEditorByNumAndEmail(int recruitNO, String email){
        String sql = "SELECT * FROM APPLY_EDITOR WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        ApplyEditorDO applyEditorDO = null;
        try{
            applyEditorDO = jdbcTemplate.queryForObject(sql, new ApplyEditorRowMapper(), recruitNO, email);
            return  applyEditorDO;
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }

    }
    //주현 0511
    //지원한 구인글에 링크와 하고싶은말 업데이트하기
    public void updateApplyEditor(int recruitNo, String editor_email, String edited_link, String editor_memo){
        String sql = "UPDATE APPLY_EDITOR SET EDITED_LINK = ?, EDITOR_MEMO = ? WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        jdbcTemplate.update(sql, edited_link, editor_memo, recruitNo, editor_email);
    }

    //주현 0512
    public void deleteApplyEditor(int recruitNo, String email){
        String sql = "DELETE FROM APPLY_EDITOR WHERE RECRUIT_NO = ? AND EDITOR_EMAIL = ?";
        jdbcTemplate.update(sql, recruitNo, email);
    }

    //준원 0511
    // 유튜버 작성한 구인글 지원자 확인
    public List<ApplierListDO> myRecruitApplierList(int recruitNo, int postsPerPage, int offset) {

        String sql = "SELECT * FROM (SELECT ROWNUM AS rn, a.* FROM (SELECT E.RECRUIT_NO, P.PORTFOLIO_TITLE, U.NICKNAME, U.EMAIL " +
                "FROM APPLY_EDITOR E JOIN USER_INFO U ON E.EDITOR_EMAIL = U.EMAIL JOIN PORTFOLIO P ON U.EMAIL = P.EDITOR_EMAIL " +
                "WHERE E.RECRUIT_NO = ? ORDER BY E.RECRUIT_NO DESC) a) WHERE rn BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{recruitNo, offset + 1, offset + postsPerPage}, new ApplierListRowMapper());
    }

    public int getTotalApplier(int recruitNo) {
        String sql = "SELECT COUNT(*) FROM (SELECT E.RECRUIT_NO, P.PORTFOLIO_TITLE, U.NICKNAME, U.EMAIL " +
                "FROM (APPLY_EDITOR E) JOIN (USER_INFO U) ON (E.EDITOR_EMAIL LIKE U.EMAIL) JOIN PORTFOLIO P ON (U.EMAIL LIKE P.EDITOR_EMAIL) " +
                "WHERE E.RECRUIT_NO = ? )";
        return jdbcTemplate.queryForObject(sql, Integer.class, recruitNo);
    }

    public UserEditorDO findEditor(String email){
        String sql = "SELECT * FROM USER_EDITOR WHERE EDITOR_EMAIL = ?";
        return jdbcTemplate.queryForObject(sql, new EditorRowMapper(), email);
    }

    // 준원 0513
    // 테스트 영상확인
    public ApplyEditorDO checkApplierVideo(String editor_email, int recruit_no) {
        String sql = "SELECT * FROM APPLY_EDITOR WHERE EDITOR_EMAIL = ? AND RECRUIT_NO = ?";
        return jdbcTemplate.queryForObject(sql, new ApplyEditorRowMapper(), editor_email, recruit_no);
    }

    // 준원 0514
    // 유튜버 메모
    public void updateYoutuberMemo(String youtuber_memo, int apply_no){
        String sql = "UPDATE APPLY_EDITOR SET YOUTUBER_MEMO = ? WHERE APPLY_NO = ?";
        jdbcTemplate.update(sql, youtuber_memo, apply_no);
    }
}
