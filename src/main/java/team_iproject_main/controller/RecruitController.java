package team_iproject_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team_iproject_main.model.DO.*;
import team_iproject_main.model.Request.RecruitBoardEditRequest;
import team_iproject_main.model.Request.RequestKeyword;
import team_iproject_main.model.SO.RecruitService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    // 준영 페이징 추가
    @GetMapping("/recruit_board")
    public String list1(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        List<RecruitSearchDO> recruitDO = recruitService.findRecruit(page, postsPerPage);
        model.addAttribute("recruitDO", recruitDO);

        // Calculate pagination variables
        int totalPosts = recruitService.getTotalPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "recruit_board";
    }

    //주현 0512:수업시간중에 수정
    @GetMapping("/recruit_result")
    public String recruitresult(int recruitNo, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        YoutuberDO infoDO = recruitService.getYoutuberPhotoName(recruitNo);

        RecruitDO recruitDO = recruitService.boardview(recruitNo);
        List<ChannelCategoryDO> chCategories = recruitService.getChannelCategory(recruitNo);
        List<EditToolsRecruitDO> editTools = recruitService.getEditTools(recruitNo);
        String categories = " ";
        String tools = " ";
        //타임리프에서 th:each 사용시 자동으로 줄바꿈 되게 되어 있어서 이렇게 했습니다. 쩔수 없임...
        for (ChannelCategoryDO category : chCategories) {
            categories += category.getCategory() + " ";
        }
        for (EditToolsRecruitDO tool : editTools) {
            tools += tool.getEdit_tool() + " ";
        }

        if (recruitDO.getDeadline().compareTo(LocalDate.now()) < 0) {
            recruitDO.setOverdate(false);
        } else {
            recruitDO.setOverdate(true);
        }

        model.addAttribute("infoDO", infoDO);
        model.addAttribute("recruitDO", recruitDO);
        model.addAttribute("categories", categories);
        model.addAttribute("tools", tools);
        return "recruit_result";
    }

    @GetMapping("/recruit_board_edit")
    public String recruit_board_editForm(HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("유튜버")) {
            redirectAttributes.addFlashAttribute("notAvailable","유튜버만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        return "recruit_board_edit";
    }

    @PostMapping("/recruit_board_edit")
    public String recruit_board_edit(RecruitBoardEditRequest req, HttpSession session, Model model) {
        String email = session.getAttribute("email").toString();

        recruitService.recruit_boardWrite(email, req);

        return "redirect:/recruit_board";
    }

    //희수
    //구인글 삭제
    @GetMapping("/recruit_delete")
    public String recruit_delete(int recruitNo, RedirectAttributes redirectAttributes, HttpSession session) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("email")).equals(recruitService.boardview(recruitNo).getEmail()) &&
                !String.valueOf(session.getAttribute("type")).equals("관리자")) {
            redirectAttributes.addFlashAttribute("notAvailable", "작성자만 삭제할 수 있습니다.");
            return "redirect:/main";
        }

        recruitService.deleteRecruit(recruitNo);
        redirectAttributes.addFlashAttribute("msg","정상적으로 삭제 완료 되었습니다.");

        return "redirect:/recruit_board";
    }

    //주현 0513
    @GetMapping("/recruit_delete_myPage")
    public String recruit_delete_myPage(int recruitNo, RedirectAttributes redirectAttributes, HttpSession session) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("email")).equals(recruitService.boardview(recruitNo).getEmail())) {
            redirectAttributes.addFlashAttribute("notAvailable", "작성자만 삭제할 수 있습니다.");
            return "redirect:/main";
        }

        redirectAttributes.addAttribute("msg","정상적으로 삭제 완료 되었습니다.");
        recruitService.deleteRecruit(recruitNo);
        return "redirect:/my_recruit";
    }


    @GetMapping("/recruit_board_modify")
    public String recruit_modifyForm(int recruitNo, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("email")).equals(recruitService.boardview(recruitNo).getEmail())) {
            redirectAttributes.addFlashAttribute("notAvailable", "작성자만 수정할 수 있습니다.");
            return "redirect:/main";
        }

        RecruitDO recruitDO = recruitService.boardview(recruitNo);
        List<ChannelCategoryDO> chCategories = recruitService.getChannelCategory(recruitNo);
        List<EditToolsRecruitDO> editTools = recruitService.getEditTools(recruitNo);

        String[] tools = {"프리미어프로", "애프터이펙트", "베가스", "파이널컷", "파워디렉터", "포토샵", "일러스트레이터", "마야", "블렌더", "기타"};
        String[] categories ={"먹방","게임","일상","요리","음악","영화","스포츠","학습","소통","자동차","IT","이슈","동물","기타"};

        boolean[] returnTools = new boolean[10];
        boolean[] returnCategory = new boolean[14];


        for(int i = 0; i < tools.length; i++){
            for (EditToolsRecruitDO tool : editTools) {
                if(tools[i].equals(tool.getEdit_tool())){
                    returnTools[i] = true;
                }
            }
        }
        for(int i = 0; i < categories.length; i++){
            for (ChannelCategoryDO catego : chCategories) {
                if(categories[i].equals(catego.getCategory())){
                    returnCategory[i] = true;
                }
            }
        }
        model.addAttribute("recruitDO", recruitDO);
        model.addAttribute("returnCategory", returnCategory);
        model.addAttribute("returnTools", returnTools);
        return "recruit_board_modify";
    }

    @PostMapping("/recruit_board_modify")
    public String recruit_board_modify(@RequestParam(value = "recruitNo") int recruitNo, RecruitBoardEditRequest req, HttpSession session, RedirectAttributes redirectAttributes){
        String email = session.getAttribute("email").toString();

        recruitService.recruit_boardModify(req, recruitNo, email);
        redirectAttributes.addFlashAttribute("msg", "정상적으로 수정되었습니다!");
        return "redirect:/recruit_board";

    }

    //준영 페이징 추가
    @PostMapping("/recruit_board/search")
    public String board_Search_find_Post(@ModelAttribute("searchForm") RequestKeyword keyword, HttpSession session) {
        session.setAttribute("keyword", keyword);
        return "redirect:/recruit_board/search";
    }

    //준영 페이징 추가
    @GetMapping("/recruit_board/search")
    public String board_Search_find(@RequestParam(value = "page", defaultValue = "1") int page
            , Model model, HttpSession session) {

        RequestKeyword keyword = (RequestKeyword)session.getAttribute("keyword");

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        if(keyword.getSearch_text() == null) {
            keyword.setSearch_text("");
        }
        if(keyword.getSubscribe_no() == null) {
            keyword.setSubscribe_no(0L);
        }
        if(keyword.getSalary_no() == null) {
            keyword.setSalary_no(0L);
        }
        if(keyword.getEnvironment_no() == null) {
            keyword.setEnvironment_no("");
        }
        if(keyword.getEdit_tools_no() == null || keyword.getEdit_tools_no().length == 0) {
            keyword.setEdit_tools_no(new String[]{});
        }
        List<RecruitSearchDO> recruitDO = recruitService.Recruit_finder(keyword, page, postsPerPage);
        model.addAttribute("recruitDO", recruitDO);

        // Calculate pagination variables
        int totalPosts = recruitService.getSearchTotalPosts(keyword);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        session.setAttribute("keyword", keyword);

        return "finder";
    }

    // 0511 준원
    // 마이페이지 -> 내 구인글 확인 -> 구인글 리스트
    // 준영 페이징 추가
    @GetMapping("/my_recruit")
    public String myRecruit(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("유튜버")) {
            redirectAttributes.addFlashAttribute("notAvailable","유튜버만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        String youtuber_email = String.valueOf(session.getAttribute("email"));
        List<MyRecruitDO> myRecruitList = recruitService.myRecruitList(youtuber_email, page, postsPerPage);
        if (myRecruitList.size() == 0) {
            model.addAttribute("NotRecruit", "작성하신 구인글이 없습니다.");
        } else {
            model.addAttribute("myRecruitList", myRecruitList);
        }

        int totalPosts = recruitService.getTotalRecruits(youtuber_email);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        return "/my_recruit";
    }

    @PostMapping("/recruit_board/main")
    public String main_Search_find_Post(@ModelAttribute("searchForm") RequestKeyword keyword, HttpSession session) {
        session.setAttribute("keyword", keyword);
        return "redirect:/recruit_board/search";
    }
}
