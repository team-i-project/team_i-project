package team_iproject_main.model.Mapper;

import org.springframework.jdbc.core.RowMapper;
import team_iproject_main.model.DO.QnADO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QnARowMapper implements RowMapper<QnADO> {

    @Override
    public QnADO mapRow(ResultSet rs, int rowNum) throws SQLException {
        QnADO qnaDO = new QnADO();
        qnaDO.setQnano(rs.getInt("QNA_NO"));
        qnaDO.setEmail(rs.getString("EMAIL"));
        qnaDO.setNickname(rs.getString("NICKNAME"));
        qnaDO.setQuestion(rs.getString("QUESTION"));
        qnaDO.setAnswer(rs.getString("ANSWER"));
        qnaDO.setPostdate(rs.getDate("POST_DATE").toLocalDate());
        return qnaDO;
    }
}
