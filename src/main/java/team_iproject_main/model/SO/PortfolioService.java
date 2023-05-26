package team_iproject_main.model.SO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import team_iproject_main.model.DAO.PortfolioDao;
import team_iproject_main.model.DO.PortfolioDO;
import team_iproject_main.model.DO.PortfolioEditDO;
import team_iproject_main.model.DO.PortfolioToolsDO;
import team_iproject_main.model.Request.PortfolioEditRequest;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioDao portfolioDao;

    //희수
    //포트폴리오 게시글 조회
    //준영 페이징 추가
    public List<PortfolioDO> findPortfolio(int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return portfolioDao.PortfolioAll(postsPerPage, offset);
    }

    //준영 페이징 추가
    public int getTotalPosts() { return portfolioDao.getTotalPosts(); }

    //희수
    //포트폴리오 게시글 상세 조회
    public PortfolioDO portfoloview(String email){
        return portfolioDao.selectPortfolioPost(email);
    }

    //서림 5.12 video tool 추가
    //5.15 주현 수정
    public void portfolioupdate(PortfolioEditRequest edit, String email){
        String main = edit.getMain_link();
        String returnurl = "https://www.youtube.com/embed/";
        if(main.contains("https://www.youtube.com/watch?v=")){
            if(main.contains(("&pp="))){
                returnurl += main.substring(32, 43);
            }else{
                returnurl += main.substring(32, 43);
            }
        }
        else if(main.contains("https://youtu.be/")){
            returnurl +=  main.substring(17, 28);
        }
        else if(main.contains("https://www.youtube.com/live/")){
            returnurl += main.substring(29, 40);
        }else{
            returnurl = main;
        }
        edit.setMain_link(returnurl);
        PortfolioEditDO portfolioEditDO = new PortfolioEditDO(edit.getLocation(),edit.getEdit_tools()
                ,edit.getYOUTUBE_CAREER() ,edit.getCareer()
                ,edit.getMessage(),edit.getMain_link(),edit.getTitle(),edit.getSalary(),edit.getWorktype(),edit.getToyoutuber());

        portfolioEditDO.setEdit_email(email);
        portfolioEditDO.setIs_public(edit.getCkb());

        portfolioDao.deletevideo(email);
        portfolioDao.deletetools(email);
        portfolioDao.portfolioupdate(portfolioEditDO);
    }

    public void portfolioInsert(PortfolioEditRequest edit, String email) {
        String main = edit.getMain_link();
        String returnurl = "https://www.youtube.com/embed/";
        if(main.contains("https://www.youtube.com/watch?v=")){
            if(main.contains(("&pp="))){
                returnurl += main.substring(32, 43);
            }else{
                returnurl += main.substring(32, 43);
            }
        }
        else if(main.contains("https://youtu.be/")){
            returnurl +=  main.substring(17,28);
        }
        else if(main.contains("https://www.youtube.com/live/")){
            returnurl += main.substring(29, 40);
        }else{
            returnurl = main;
        }
        edit.setMain_link(returnurl);
        PortfolioEditDO portfolioEditDO = new PortfolioEditDO(edit.getLocation(),edit.getEdit_tools()
                ,edit.getYOUTUBE_CAREER(), edit.getCareer()
                ,edit.getMessage(),edit.getMain_link(),edit.getTitle(),edit.getSalary(),edit.getWorktype(),edit.getToyoutuber());

        portfolioEditDO.setEdit_email(email);
        portfolioEditDO.setIs_public(edit.getCkb());

        portfolioDao.portfolioInsert(portfolioEditDO);
    }

    public PortfolioEditDO portfolioedit(String email) throws EmptyResultDataAccessException {
        return portfolioDao.selectPortfolioEdit(email);
    }


    //5.11 서림 대표영상링크 저장
    //5.15 주현 수정
    public void portfolioVideoUpdate(String[] edit_link, String email){
        int count=1;
        for (String link : edit_link) {
            String returnurl = "https://www.youtube.com/embed/";
            if(link.contains("https://www.youtube.com/watch?v=")){
                if(link.contains(("&pp="))){
                    returnurl += link.substring(32, 43);
                }else{
                    returnurl += link.substring(32, 43);
                }
            }
            else if(link.contains("https://youtu.be/")){
                returnurl +=  link.substring(17, 28);
            }
            else if(link.contains("https://www.youtube.com/live/")){
                returnurl += link.substring(29, 40);
            }else{
                returnurl = link;
            }
            if (!returnurl.isEmpty()) {
                count++;
                portfolioDao.editlinkUpdate(returnurl,email,count);
            }
        }

    }

    //5.11 서림 tool, videolinks (do객체들도 추가)
    public List<PortfolioToolsDO> getEditTools(String email) {
        return portfolioDao.getTools(email);
    }
    //5.11 서림 tool, videolinks (do객체들도 추가)
    public List<String> getVideoLinks(String email) {
        return portfolioDao.getVideoLinks(email);
    }

    //희수
    //포트폴리오 삭제
    public void deletePortfole(String email1){
        portfolioDao.deletePortfolioPost(email1);
    }


    // 겸손
    public List<PortfolioDO> PoforFinder(String folio_search_text, String location, String[] edit_tools_folio, int page, int postsPerPage) {

        int offset = (page - 1) * postsPerPage;
        return portfolioDao.FolioFinder(folio_search_text, location, edit_tools_folio, postsPerPage, offset);
    }

    public int getSearchTotalPosts(String folio_search_text, String location, String[] edit_tools_folio) {
        return portfolioDao.getTotalSearchPosts(folio_search_text, location, edit_tools_folio);
    }

}


