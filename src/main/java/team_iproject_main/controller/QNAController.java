package team_iproject_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team_iproject_main.model.DO.QnADO;
import team_iproject_main.model.SO.QnAService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QNAController {

    @Autowired
    private QnAService qnAService;

    @GetMapping("/Qna_question")
    public String Qna_question () {
        return "Qna_question";
    }

    //희수
    @GetMapping("/QnAboard")
    public String QnA_board(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        List<QnADO> qnado = qnAService.findQna(page, postsPerPage);
        model.addAttribute("qnado",qnado);

        int totalPosts = qnAService.getTotalQnA();
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        return "QnAboard";
    }

    //희수
    @PostMapping("/QnAboard")
    public String QA_boardForm(HttpSession session, String question, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        String email = String.valueOf(session.getAttribute("email"));

        String answer = "아직 답변되지 않은 질문입니다.";

        qnAService.qnaUpdate(email,question,answer);
        return "redirect:/QnAboard";
    }

    @PostMapping("/QnAboard/answer")
    public String QA_board_answerForm(int qnano, String answer) {
        qnAService.qnaAnswer(qnano, answer);

        return "redirect:/QnAboard";
    }

}
