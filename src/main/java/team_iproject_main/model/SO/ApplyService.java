package team_iproject_main.model.SO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_iproject_main.model.DAO.ApplyEditorDao;
import team_iproject_main.model.DO.ApplierListDO;
import team_iproject_main.model.DO.ApplyEditorDO;
import team_iproject_main.model.DO.UserEditorDO;

import java.util.List;

@Service
public class ApplyService {

    @Autowired
    private ApplyEditorDao applyEditorDao;

    public void applyUploadVideo(int recruitNo, String editor_email, String edited_link, String editor_memo) {
        //https://www.youtube.com/watch?v=sEenDC5fry4
        //https://youtu.be/sEenDC5fry4
        //https://www.youtube.com/live/pY5yHv-ZOi4?feature=share
        //https://www.youtube.com/watch?v=pY5yHv-ZOi4&pp=ygUQ7Lmo7LCp66eoIOybkOuztQ%3D%3D
        //링크가 두 종류임... -> 4종류 됨..........................
        String returnurl = "";
        if(edited_link.contains("https://www.youtube.com/watch?v=")){
            if(edited_link.contains(("&pp="))){
                returnurl = "https://www.youtube.com/embed/" + edited_link.substring(32, 43);
            }else{
                returnurl = "https://www.youtube.com/embed/" + edited_link.substring(32, 43);
            }
        }
        else if(edited_link.contains("https://youtu.be/")){
            returnurl = "https://www.youtube.com/embed/" + edited_link.substring(17, 28);
        }
        else if(edited_link.contains("https://www.youtube.com/live/")){
            returnurl = "https://www.youtube.com/embed/" + edited_link.substring(29, 40);
        }else{
            returnurl = edited_link;
        }
        applyEditorDao.updateApplyEditor(recruitNo, editor_email, returnurl, editor_memo);

    }

    public ApplyEditorDO getLinkAndMemo(int recruitNo, String email){
        return applyEditorDao.findApplyEditorByNumAndEmail(recruitNo, email);
    }

    //0512 주현 수업시간중 추가
    public void deleteApplyEditorByNumEmail(int recruitNo, String email){
        applyEditorDao.deleteApplyEditor(recruitNo, email);
    }

    //주현
    //지원자 넣기
    public void recruitBoardApply(int recruitNo, String email){
        applyEditorDao.addApplyEditor(recruitNo, email);
    }

    public ApplyEditorDO findApplyEditorByNumEmail(int recruitNo, String email){
        return applyEditorDao.findApplyEditorByNumAndEmail(recruitNo, email);
    }

    // 0512 준원
    // 마이페이지 -> 작성한 구인글 -> 지원자 확인
    // 준영 페이징 추가
    public List<ApplierListDO> myApplierList(int recruitNo, int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return applyEditorDao.myRecruitApplierList(recruitNo, postsPerPage, offset);
    }

    public int getTotalApplier(int recruitNo) {
        return applyEditorDao.getTotalApplier(recruitNo);
    }

    public UserEditorDO findEditor(String email){
        return applyEditorDao.findEditor(email);
    }

    // 0514 준원
    // 테스트 영상확인
    public ApplyEditorDO applierTestVideo(String editor_email, int recruit_no) {
        return applyEditorDao.checkApplierVideo(editor_email, recruit_no);
    }

    // 0514 준원
    // 유튜버 메모
    public void insertYoutuberMemo(String youtuber_memo, int apply_no) {
        applyEditorDao.updateYoutuberMemo(youtuber_memo, apply_no);
    }
}
