package team_iproject_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team_iproject_main.model.DO.PortfolioDO;
import team_iproject_main.model.DO.PortfolioEditDO;
import team_iproject_main.model.DO.PortfolioToolsDO;
import team_iproject_main.model.DO.UserDO;
import team_iproject_main.model.Request.PortfolioEditRequest;
import team_iproject_main.model.SO.PortfolioService;
import team_iproject_main.model.SO.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserService userService;

    //희수
    //구직자 게시판 조회
    //준영 페이징 추가
    @GetMapping("/portfolio_board")
    public String list2(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        List<PortfolioDO> portfolioDO = portfolioService.findPortfolio(page, postsPerPage);
        model.addAttribute("portfolioDO", portfolioDO);

        int totalPosts = portfolioService.getTotalPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "portfolio_board";
    }

    @GetMapping("/myPage/portfolio_result")
    public String portfolioresult(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            redirectAttributes.addFlashAttribute("notAvailable","편집자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        try {
            PortfolioDO portfolioDO = portfolioService.portfoloview(String.valueOf(session.getAttribute("email")));
            model.addAttribute("portfolioDO",portfolioDO);
        }
        catch(EmptyResultDataAccessException e) {
            PortfolioEditDO portfolioEditDO = new PortfolioEditDO();
            UserDO userDO = userService.findUser(String.valueOf(session.getAttribute("email")));

            portfolioEditDO.setName(userDO.getName());
            portfolioEditDO.setGender(userDO.getGender());
            portfolioEditDO.setIs_public("TRUE");
            boolean[] returnTools = new boolean[10];
            String[] videoLinks = new String[13];

            model.addAttribute("portfolioEditDO",portfolioEditDO);
            model.addAttribute("returnTools",returnTools);
            model.addAttribute("videoLink",videoLinks);

            return "portfolio_edit";
        }

        String email = session.getAttribute("email").toString();
        UserDO userDO = userService.findUser(email);

        List<PortfolioToolsDO> editTools = portfolioService.getEditTools(email);
        List<String> videoLink = portfolioService.getVideoLinks(email);
        String returnTools = "";
        for (PortfolioToolsDO tool : editTools) {
            returnTools += tool.getEdittools() + " ";
        }
        UserDO user = userService.findUser(String.valueOf(session.getAttribute("email")));
        String[] videoLinks = new String[13];
        videoLinks = videoLink.toArray(videoLinks);

        model.addAttribute("user",user);
        model.addAttribute("returnTools",returnTools);
        model.addAttribute("videoLink",videoLinks);
        PortfolioDO portfolioDO = portfolioService.portfoloview(email);
        model.addAttribute("portfolioDO", portfolioDO);
        return "portfolio_result";
    }

    //5.11 양서림
    @PostMapping("/portfolio_edit")
    public String portfolioedit(HttpSession session, PortfolioEditRequest edit, RedirectAttributes redirectAttributes, @RequestParam("edit_link") String[] edit_link,
                                @RequestParam("salaryhid") String salaryhid, @RequestParam("worktypehid") String worktypehid, @RequestParam("toyoutuberhid") String toyoutuberhid) {
        String email = String.valueOf(session.getAttribute("email"));

        if(edit.getCkb() == null) {
            edit.setCkb("FALSE");
        }

        System.out.println(salaryhid != null && !salaryhid.equals(""));
        System.out.println(worktypehid != null && !worktypehid.equals(""));
        System.out.println(toyoutuberhid != null && !toyoutuberhid.equals(""));

        if(edit.getSalary() == null || edit.getSalary().equals("")) {
            edit.setSalary(" ");
            if(salaryhid != null && !salaryhid.equals("")) {
                edit.setSalary(salaryhid);
            }
        }
        if(edit.getWorktype() == null || edit.getWorktype().equals("")) {
            edit.setWorktype(" ");
            if(worktypehid != null && !worktypehid.equals("")) {
                edit.setWorktype(worktypehid);
            }
        }
        if(edit.getToyoutuber() == null || edit.getToyoutuber().equals("")) {
            edit.setToyoutuber(" ");
            if(toyoutuberhid != null && !toyoutuberhid.equals("")) {
                edit.setToyoutuber(toyoutuberhid);
            }
        }


        try {
            PortfolioEditDO portfolioEditDO = portfolioService.portfolioedit(email);
        }
        catch(EmptyResultDataAccessException e) {
            portfolioService.portfolioInsert(edit, email);
            portfolioService.portfolioVideoUpdate(edit_link,email); //편집영상 db 저장
            redirectAttributes.addFlashAttribute("msg","포트폴리오가 작성되었습니다");
            return "redirect:/portfolio_edit?email="+email;
        }

        portfolioService.portfolioupdate(edit,email); //편집영상제외 db저장

        portfolioService.portfolioVideoUpdate(edit_link,email); //편집영상 db 저장
        redirectAttributes.addFlashAttribute("msg","포트폴리오가 수정되었습니다");
        return "redirect:/portfolio_edit?email="+email;
    }
    //5.12 양서림 초기조회 추가 (html css도변경함)
    @GetMapping("/portfolio_edit")
    public String portfolioForm(String email, Model model,HttpSession session , RedirectAttributes redirectAttributes) {

        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            redirectAttributes.addFlashAttribute("notAvailable","편집자만 이용 가능합니다.");
            return "redirect:/main";
        }
        else if(email != null && !email.equals(String.valueOf(session.getAttribute("email")))) {
            redirectAttributes.addFlashAttribute("msg","작성자만 수정 할 수 있습니다.");
            return "redirect:/portfolio_result?email=" + email;
        }

        try {
            PortfolioDO portfolioDO = portfolioService.portfoloview(String.valueOf(session.getAttribute("email")));
            model.addAttribute("portfolioDO",portfolioDO);
        }
        catch(EmptyResultDataAccessException e) {
            PortfolioEditDO portfolioEditDO = new PortfolioEditDO();
            UserDO userDO = userService.findUser(String.valueOf(session.getAttribute("email")));

            portfolioEditDO.setName(userDO.getName());
            portfolioEditDO.setGender(userDO.getGender());
            portfolioEditDO.setIs_public("TRUE");
            boolean[] returnTools = new boolean[10];
            String[] videoLinks = new String[13];

            model.addAttribute("portfolioEditDO",portfolioEditDO);
            model.addAttribute("returnTools",returnTools);
            model.addAttribute("videoLink",videoLinks);

            return "portfolio_edit";
        }

        PortfolioEditDO portfolioEditDO = portfolioService.portfolioedit(String.valueOf(session.getAttribute("email"))); // 초기조회
        List<PortfolioToolsDO> editTools = portfolioService.getEditTools(String.valueOf(session.getAttribute("email"))); // tools 체크
        List<String> videoLink = portfolioService.getVideoLinks(String.valueOf(session.getAttribute("email")));

        String[] videoLinks = new String[13];
        videoLinks = videoLink.toArray(videoLinks);

        String[] tools = {"프리미어프로", "애프터이펙트", "베가스", "파이널컷", "파워디렉터", "포토샵", "일러스트레이터", "마야", "블렌더", "기타"};
        boolean[] returnTools = new boolean[10];

        for(int i = 0; i < tools.length; i++){
            for (PortfolioToolsDO tool : editTools) {
                if(tools[i].equals(tool.getEdittools())){
                    returnTools[i] = true;
                }
            }
        }
        model.addAttribute("portfolioEditDO",portfolioEditDO);
        model.addAttribute("returnTools",returnTools);
        model.addAttribute("videoLink",videoLinks);
        return "portfolio_edit";
    }


    //희수
    //구직자 상세 조회
    @GetMapping("/portfolio_result")
    public String portfolio_result(String email, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        List<PortfolioToolsDO> editTools = portfolioService.getEditTools(email);
        List<String> videoLink = portfolioService.getVideoLinks(email);
        String returnTools = "";
        for (PortfolioToolsDO tool : editTools) {
            returnTools += tool.getEdittools() + " ";
        }
        UserDO user = userService.findUser(email);
        String[] videoLinks = new String[13];
        videoLinks = videoLink.toArray(videoLinks);

        model.addAttribute("user",user);
        model.addAttribute("returnTools",returnTools);
        model.addAttribute("videoLink",videoLinks);
        PortfolioDO portfolioDO = portfolioService.portfoloview(email);
        model.addAttribute("portfolioDO", portfolioDO);
        return "portfolio_result";
    }
    //희수
    //포트폴리오 삭제
    @GetMapping("/portfolio_delete")
    public String portfolio_delete(String email1, HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!email1.equals(String.valueOf(session.getAttribute("email")))){
            redirectAttributes.addFlashAttribute("msg","작성자만 삭제 할 수 있습니다.");
            return "redirect:/portfolio_result?email=" + email1;
        }
        portfolioService.deletePortfole(email1);
        redirectAttributes.addFlashAttribute("deleteMsg", "포트폴리오가 삭제되었습니다.");
        return "redirect:/portfolio_board";
    }

    // 겸손
    @PostMapping("/portfolio_board/search")
    public String portfolio_board_search_POST(
            @RequestParam("folio_search_text") String folio_search_text,
            @RequestParam("location") String location,
            @RequestParam(value = "edit_tools_folio", required = false) String[] edit_tools_folio,
            HttpSession session) {

        if(edit_tools_folio == null) {
            edit_tools_folio = new String[]{};
        }

        session.setAttribute("folio_search_text", folio_search_text);
        session.setAttribute("location", location);
        session.setAttribute("edit_tools_folio", edit_tools_folio);

        return "redirect:/portfolio_board/search";
    }

    @GetMapping("/portfolio_board/search")
    public String portfolio_board_search(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session, Model model) {

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        String folio_search_text = String.valueOf(session.getAttribute("folio_search_text"));
        String location = String.valueOf(session.getAttribute("location"));
        String[] edit_tools_folio = (String[]) session.getAttribute("edit_tools_folio");

        if((edit_tools_folio == null || edit_tools_folio.length == 0)&& folio_search_text.equals("") && location.equals("")) {
            return list2(1, model);
        }

        if(edit_tools_folio == null) {
            edit_tools_folio = new String[]{};
        }

        List<PortfolioDO> portfolioDO = portfolioService.PoforFinder(folio_search_text, location, edit_tools_folio, page, postsPerPage);
        model.addAttribute("portfolioDO", portfolioDO);

        int totalPosts = portfolioService.getSearchTotalPosts(folio_search_text, location, edit_tools_folio);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        session.setAttribute("folio_search_text", folio_search_text);
        session.setAttribute("location", location);
        session.setAttribute("edit_tools_folio", edit_tools_folio);

        return "portfolio_finder";

    }
}
