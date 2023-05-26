package team_iproject_main.model.SO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_iproject_main.model.DAO.RecruitBoardDao;
import team_iproject_main.model.DO.*;
import team_iproject_main.model.Request.RecruitBoardEditRequest;
import team_iproject_main.model.Request.RequestKeyword;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecruitService {

    @Autowired
    private RecruitBoardDao recruitBoardDao;


    public void recruit_boardWrite(String email, RecruitBoardEditRequest req){
        //https://www.youtube.com/watch?v=sEenDC5fry4
        //https://youtu.be/sEenDC5fry4
        //링크가 두 종류임...
        String url = req.getOriginal_link();
        String returnurl = "";
        if(url.contains("https://www.youtube.com/watch?v=")){
            if(url.contains(("&pp="))){
                returnurl = "https://www.youtube.com/embed/" + url.substring(32, 43);
            }else{
                returnurl = "https://www.youtube.com/embed/" + url.substring(32, 43);
            }
        }
        else if(url.contains("https://youtu.be/")){
            returnurl = "https://www.youtube.com/embed/" + url.substring(17, 28);
        }
        else if(url.contains("https://www.youtube.com/live/")){
            returnurl = "https://www.youtube.com/embed/" + url.substring(29, 40);
        }
        else{
            returnurl = url;
        }

        RecruitBoardDO recruitBoardDO = new RecruitBoardDO(req.getRecruit_title(), req.getDuty(), req.getWorkload(), req.getRequirement(),
                req.getSalary_detail(), req.getPreference(), req.getEdit_tools(), req.getEnvironment(), req.getWelfare(), req.getRecruit_detail(),
                req.getSalary(), LocalDate.parse(req.getDeadline()), returnurl, req.getChannel_categories());

        recruitBoardDO.setYoutuber_email(email);
        recruitBoardDao.createRecruitBoard(recruitBoardDO);
    }

    public void recruit_boardModify(RecruitBoardEditRequest req, int recruitNo, String email)  {
        String url = req.getOriginal_link();
        String returnurl = "";
        //https://www.youtube.com/watch?v=sEenDC5fry4
        //https://youtu.be/sEenDC5fry4
        //https://www.youtube.com/live/pY5yHv-ZOi4?feature=share
        //https://www.youtube.com/watch?v=pY5yHv-ZOi4&pp=ygUQ7Lmo7LCp66eoIOybkOuztQ%3D%3D
        //링크가 두 종류임... -> 4종류 됨..........................

        if(url.contains("https://www.youtube.com/watch?v=")){
            if(url.contains(("&pp="))){
                returnurl = "https://www.youtube.com/embed/" + url.substring(32, 43);
            }else{
                returnurl = "https://www.youtube.com/embed/" + url.substring(32, 43);
            }
        }
        else if(url.contains("https://youtu.be/")){
            returnurl = "https://www.youtube.com/embed/" + url.substring(17, 28);
        }
        else if(url.contains("https://www.youtube.com/live/")){
            returnurl = "https://www.youtube.com/embed/" + url.substring(29, 40);
        }
        else{
            returnurl = url;
        }

        RecruitBoardDO recruitBoardDO = new RecruitBoardDO(req.getRecruit_title(), req.getDuty(), req.getWorkload(), req.getRequirement(),
                req.getSalary_detail(), req.getPreference(), req.getEdit_tools(), req.getEnvironment(), req.getWelfare(), req.getRecruit_detail(),
                req.getSalary(), LocalDate.parse(req.getDeadline()), returnurl, req.getChannel_categories());

        recruitBoardDao.modifyRecruitBoard(recruitBoardDO, recruitNo, email);
    }

    //주현
    // 준영 페이징 추가
    public List<RecruitDO> findRecruit(String email, int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return recruitBoardDao.findAllApplyByEmail(email, postsPerPage, offset);
    }

    // 준영 페이징 추가
    public int getTotalApply(String email) {
        return recruitBoardDao.getTotalApply(email);
    }

    //희수
    //구인글 게시글 상세 조회
    public RecruitDO boardview(int recruitNo){
        return recruitBoardDao.selectRecruitPost(recruitNo);
    }

    public List<ChannelCategoryDO> getChannelCategory(int recruitNo){
        return recruitBoardDao.getCategories(recruitNo);
    }

    public List<EditToolsRecruitDO> getEditTools(int recruitNo){
        return recruitBoardDao.getTools(recruitNo);
    }

    //희수
    //구인글 게시글 조회
    // 준영 페이징 추가
    public List<RecruitSearchDO> findRecruit(int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return recruitBoardDao.findRecruit(postsPerPage, offset);
    }


    // 준영 페이징 추가
    public int getTotalPosts() {
        return recruitBoardDao.getTotalPosts();
    }

    public int getSearchTotalPosts(RequestKeyword keyword) {
        return recruitBoardDao.getSearchTotalPosts(keyword);
    }

    public void deleteRecruit(int recruitNo){
        recruitBoardDao.deleteRecruitPost(recruitNo);
    }

    // 겸손 검색
    public List<RecruitSearchDO> Recruit_finder(RequestKeyword keyword, int page, int postsPerPage) {

        int offset = (page - 1) * postsPerPage;
        List<RecruitSearchDO> recruitDO = recruitBoardDao.SearchFinder(keyword, postsPerPage, offset);

        return recruitDO;

    }

    // 0512 준원
    // 유튜버 -> 작성한 구인글 -> 지원자 확인
    // 준영 페이징 추가
    public List<MyRecruitDO> myRecruitList(String youtuber_email, int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return recruitBoardDao.findMyRecruit(youtuber_email, postsPerPage, offset);
    }

    // 준영 페이징 추가
    public int getTotalRecruits(String youtuber_email) {
        return recruitBoardDao.getTotalRecruits(youtuber_email);
    }

    // 준원
    // 유튜버 사진 및 채널명 가져오는 메서드
    public YoutuberDO getYoutuberPhotoName(int recruitNo) {
        return recruitBoardDao.selectYoutuberInfo(recruitNo);
    }
}
