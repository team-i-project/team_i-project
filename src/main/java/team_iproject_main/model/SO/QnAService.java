package team_iproject_main.model.SO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_iproject_main.model.DAO.QnADao;
import team_iproject_main.model.DO.QnADO;

import java.util.List;

@Service
public class QnAService {

    @Autowired
    private QnADao qnADao;

    public List<QnADO> findQna(int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;

        return qnADao.findAll(postsPerPage, offset);
    }

    public int getTotalQnA() {
        return qnADao.getTotalQna();
    }

    public void qnaUpdate(String email,String question,String answer) {
        qnADao.qnaUpdate(email, question, answer);
    }

    public void qnaAnswer(int qnano, String answer) {
        qnADao.qnaAnswer(qnano, answer);
    }
}
