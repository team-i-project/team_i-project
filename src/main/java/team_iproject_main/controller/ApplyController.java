package team_iproject_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team_iproject_main.model.DO.*;
import team_iproject_main.model.SO.ApplyService;
import team_iproject_main.model.SO.RecruitService;
import team_iproject_main.model.SO.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ApplyController {

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private UserService userService;


    @GetMapping("/apply")
    public String applyAndGoMyPage(HttpSession session, int recruitNo, RedirectAttributes re, Model model){
        if(session.getAttribute("email") == null) {
            re.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            re.addFlashAttribute("notAvailable","편집자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        String email = session.getAttribute("email").toString();
        ApplyEditorDO applyEditorDO = applyService.findApplyEditorByNumEmail(recruitNo, email);
        UserEditorDO userEditorDO = applyService.findEditor(email);

        if(applyEditorDO != null) {
            re.addAttribute("msg", "이미 지원한 구인글입니다.");
        }else if(userEditorDO.getIS_UPLOADED().equals("FALSE")){
            re.addFlashAttribute("msg", "포트폴리오가 업로드 되지 않았습니다.\n마이페이지에서 포트폴리오를 올린 후 지원해주세요");
            return "redirect:/myPage";
        }
        else{
            applyService.recruitBoardApply(recruitNo, email);
            re.addAttribute("msg", "지원 완료 되었습니다\n마이페이지 지원현황에서 확인하세요!");

        }
        return "redirect:/applynow";
    }

    //0512주현 수업시간 중 수정
    @GetMapping("/applydelete")
    public String applyDelete(HttpSession session, int recruitNo, RedirectAttributes re){
        if(session.getAttribute("email") == null) {
            re.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            re.addFlashAttribute("notAvailable","편집자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        String email = session.getAttribute("email").toString();
        applyService.deleteApplyEditorByNumEmail(recruitNo, email);
        re.addAttribute("msg","지원 취소되었습니다.");
        return "redirect:/applynow";
    }

    @GetMapping("applynow")
    public String applynowForm(@RequestParam(value = "page", defaultValue = "1") int page, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            redirectAttributes.addFlashAttribute("notAvailable","편집자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        String email = session.getAttribute("email").toString();
        List<RecruitDO> recruitDO = recruitService.findRecruit(email, page, postsPerPage);

        for(RecruitDO redo : recruitDO){
            if(redo.getDeadline().compareTo(LocalDate.now()) < 0){
                redo.setOverdate(false);
            }else{
                redo.setOverdate(true);
            }
        }
        model.addAttribute("recruitDO", recruitDO);
        if (recruitDO.size() == 0){
            model.addAttribute("notRecruit", "지원한 구인글이 없습니다.");
        }

        int totalPosts = recruitService.getTotalApply(email);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "applynow";
    }

    //주현 0512
    //주현 0512 수업시간 중에 수정
    @GetMapping("/applynow_upload")
    public String applynow_uploadForm(int recruitNo, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("편집자")) {
            redirectAttributes.addFlashAttribute("notAvailable","편집자만 이용할 수 있습니다.");
            return "redirect:/main";
        }
        YoutuberDO infoDO = recruitService.getYoutuberPhotoName(recruitNo);

        RecruitDO recruitDO = recruitService.boardview(recruitNo);
        String email = session.getAttribute("email").toString();
        List<ChannelCategoryDO> chCategories = recruitService.getChannelCategory(recruitNo);
        List<EditToolsRecruitDO> editTools = recruitService.getEditTools(recruitNo);
        String categories = " ";
        String tools = " ";
        //타임리프에서 th:each 사용시 자동으로 줄바꿈 되게 되어 있어서 이렇게 했습니다. 쩔수 없임...
        for(ChannelCategoryDO category : chCategories){
            categories += category.getCategory() + " ";
        }
        for(EditToolsRecruitDO tool : editTools){
            tools += tool.getEdit_tool() + " ";
        }

        ApplyEditorDO applyEditorDO = applyService.getLinkAndMemo(recruitNo, email);
        if(applyEditorDO.getEdited_link().equals(" ")) {
            applyEditorDO.setEdited_link("");
        }
        if(applyEditorDO.getEditor_memo().equals(" ")) {
            applyEditorDO.setEditor_memo("");
        }

        model.addAttribute("infoDO", infoDO);
        model.addAttribute("recruitDO",recruitDO);
        model.addAttribute("categories", categories);
        model.addAttribute("tools", tools);
        model.addAttribute("applyEditorDO", applyEditorDO);
        return "applynow_upload";
    }

    @PostMapping("/applynow_upload")
    public String applynow_upload(@RequestParam(value = "recruitNo") int recruitNo, @RequestParam(value = "edited_link") String edited_link,
                                  @RequestParam(value = "editor_memo") String editor_memo, HttpSession session, RedirectAttributes re) {
        String email = session.getAttribute("email").toString();

        applyService.applyUploadVideo(recruitNo, email, edited_link, editor_memo);
        re.addAttribute("msg", "저장 완료 되었습니다");
        return "redirect:/applynow";
    }

    // 0512 준원
    // 유튜버 -> 작성한 구인글 -> 지원자 확인
    @GetMapping("/applier_check")
    public String bringMyRecruitApplier(@RequestParam(value = "page", defaultValue = "1") int page, int recruitNo, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
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

        List<ApplierListDO> applierListDO = applyService.myApplierList(recruitNo, page, postsPerPage);

        if (applierListDO.size() == 0) {
            model.addAttribute("NotApply", "해당 구인글에 지원자가 없습니다.");
        } else {
            model.addAttribute("applierListDO", applierListDO);
        }

        int totalPosts = applyService.getTotalApplier(recruitNo);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("recruitNo", recruitNo);
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "applier_check";
    }

    // 0513 준원
    // 테스트 영상확인
    @GetMapping("/check_video_result")
    public String myApplierTestVideo(String editor_email, int recruit_no, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("유튜버")) {
            redirectAttributes.addFlashAttribute("notAvailable","유튜버만 이용할 수 있습니다.");
            return "redirect:/main";
        }
        else if(!String.valueOf(session.getAttribute("email")).equals(recruitService.boardview(recruit_no).getEmail())) {
            redirectAttributes.addFlashAttribute("notAvailable", "작성자만 조회할 수 있습니다.");
            return "redirect:/main";
        }

        UserDO userDO = userService.findUser(editor_email);
        model.addAttribute("ApplierNickName", userDO.getNickname());

        ApplyEditorDO applyEditorDO = applyService.applierTestVideo(editor_email, recruit_no);
        model.addAttribute("applyEditorDO", applyEditorDO);

        return "check_video_result";
    }

    // 0514
    // 유튜버 메모
    @PostMapping("/check_video_result")
    public String youtuberMemo(@RequestParam(value = "youtuber_memo") String youtuber_memo, @RequestParam(value = "editor_email") String editor_email,
                               @RequestParam(value = "apply_no") int apply_no, @RequestParam(value = "recruit_no") int recruit_no,  RedirectAttributes re, Model model) {

        if(youtuber_memo == null || youtuber_memo.equals("")) {
            re.addAttribute("msg", "메모를 입력해주세요.");
            return "redirect:/check_video_result?editor_email=" + editor_email + "&recruit_no=" + recruit_no;
        }
        applyService.insertYoutuberMemo(youtuber_memo, apply_no);
        re.addFlashAttribute("msg", "메모를 등록하였습니다.");

        return "redirect:/check_video_result?editor_email=" + editor_email + "&recruit_no=" + recruit_no;
    }
}
