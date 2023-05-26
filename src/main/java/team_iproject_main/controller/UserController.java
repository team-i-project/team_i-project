package team_iproject_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team_iproject_main.exception.*;
import team_iproject_main.model.DO.EmailDto;
import team_iproject_main.model.DO.UserDO;
import team_iproject_main.model.Request.*;
import team_iproject_main.model.SO.UserService;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class UserController {

    @Value("${admin.adminEmail}")
    private String adminEmail;

    @Value("${admin.adminPassword}")
    private String adminPassword;

    @Autowired
    private UserService userService;

    @GetMapping("/main")
    public String home() {
        return "main";
    }

    @GetMapping("/")
    public String home2() {
        return "redirect:/main";
    }

    //0508손주현
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("email");
        session.invalidate();
        return "redirect:/main";
    }

    @GetMapping("/signup_options")
    public String signup_optionForm() {
        return "signup_options";
    }

    @GetMapping("/signup_editor")
    public String signup_editorForm() {
        return "signup_editor";
    }

    ////0508 손주현-편집자 회원가입 수정
    @PostMapping("/signup_editor")
    public String signup_editor(RegisterRequest req, Model model) {
        String view = "";
        try {
            userService.editor_signUp(req);
            model.addAttribute("msg", "회원가입 되었습니다.");
            view = "/main";

        }
        catch (DuplicateEmailException e) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            view = "/signup_editor";
        }
        catch (DuplicateNickNameException e) {
            model.addAttribute("msg", "이미 존재하는 닉네임입니다.");
            view = "/signup_editor";
        }
        catch (DuplicatePhone_numberException e) {
            model.addAttribute("msg", "이미 존재하는 번호입니다.");
            view = "/signup_editor";
        }
        catch (UnsupportedEncodingException e) {
        }
        return view;

    }

    @GetMapping("/signup_youtuber")
    public String signup_youtuberForm(Model model, HttpSession session) {
        model.addAttribute("channel_certificate_button", false);
        model.addAttribute("channel_photo_subscribe",true);
        model.addAttribute("channel_errorMsg_hidden", true);

        return "signup_youtuber";
    }

    //0508-유튜버 회원가입 수정
    @PostMapping("/signup_youtuber")
    public String signup_youtuber(RegisterRequest req, Model model) {
        String view = "";
        if(req.getChannel_id() == null) {
            model.addAttribute("channel_errorMsg", "채널 인증이 되지 않아 가입을 진행할 수 없습니다.");
            model.addAttribute("channel_certificate_button", false);
            model.addAttribute("channel_photo_subscribe",true);
            model.addAttribute("channel_errorMsg_hidden", false);
            return "signup_youtuber";
        }
        try {
            userService.youtuber_signUp(req);
            model.addAttribute("msg", "회원가입 되었습니다.");
            view = "/main";

        } catch (DuplicateEmailException e) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            model.addAttribute("channel_certificate_button", false);
            model.addAttribute("channel_photo_subscribe",true);
            model.addAttribute("channel_errorMsg_hidden", false);
            model.addAttribute("channel_errorMsg", "채널 인증이 되지 않아 가입을 진행할 수 없습니다.");
            view = "/signup_youtuber";
        }
        catch (DuplicateNickNameException e) {
            model.addAttribute("msg", "이미 존재하는 닉네임입니다.");
            model.addAttribute("channel_certificate_button", false);
            model.addAttribute("channel_photo_subscribe",true);
            model.addAttribute("channel_errorMsg_hidden", false);
            model.addAttribute("channel_errorMsg", "채널 인증이 되지 않아 가입을 진행할 수 없습니다.");
            view = "/signup_youtuber";
        }
        catch (DuplicateChannelException e) {
            model.addAttribute("msg", "채널아이디가 중복입니다.");
            model.addAttribute("channel_certificate_button", false);
            model.addAttribute("channel_photo_subscribe",true);
            model.addAttribute("channel_errorMsg_hidden", false);
            model.addAttribute("channel_errorMsg", "채널 인증이 되지 않아 가입을 진행할 수 없습니다.");
            view = "/signup_youtuber";
        }
        catch (DuplicatePhone_numberException e) {
            model.addAttribute("msg", "이미 존재하는 번호입니다.");
            model.addAttribute("channel_certificate_button", false);
            model.addAttribute("channel_photo_subscribe",true);
            model.addAttribute("channel_errorMsg_hidden", false);
            model.addAttribute("channel_errorMsg", "채널 인증이 되지 않아 가입을 진행할 수 없습니다.");
            view = "/signup_youtuber";
        }
        return view;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session){
        session.invalidate();
        return "login";
    }

    //0508-유튜버 로그인 수정
    @PostMapping("/login")
    public String login(LoginCommand login, HttpSession session, Model model){
        String view = "";

        if(login.getEmail().equals(adminEmail) && login.getPassword().equals(adminPassword)) {
            session.setAttribute("email", login.getEmail());
            session.setAttribute("type", "관리자");
            session.setAttribute("nickname", "관리자");
            return "redirect:/main";
        }

        try{
            if(userService.checkLoginAuth(login)){

                UserDO users = userService.findUser(login.getEmail());
                login.setEmail(users.getEmail());
                login.setPassword("");
                session.setAttribute("email", users.getEmail());
                session.setAttribute("type" , users.getUser_type());
                session.setAttribute("nickname", users.getNickname());
                view = "redirect:/main";

            }
        }catch(UserNotFoundException e){
            model.addAttribute("error", "등록되지 않은 아이디입니다.");
            view = "/login";
        }

        catch(WrongPasswordException e){
            model.addAttribute("error", "비밀번호를 잘못 입력했습니다. \n입력하신 내용을 다시 확인해주세요.");
            view = "/login";
        }
        return view;
    }


    //0506-손주현
    //find_id.html 불러오기
    @GetMapping("/find_id")
    public String find_idForm() {
        return "find_id";
    }

    //0506-손주현
    //Post find_id_result
    @PostMapping("/find_id")
    public String find_id(FindIdRequest req, Model model) {
        String view = "";
        try{
            if(userService.checkFindId(req)){
                UserDO users = userService.findUser(req.getName(), req.getPhone_number());
                req.setName(req.getName());
                model.addAttribute("email", users.getEmail());
                model.addAttribute("name", users.getName());
                view = "/find_id";
            }
        }
        catch(UserNotFoundException e){
            model.addAttribute("error", "입력하신 정보에 해당하는 아이디를 찾지 못했습니다.\n입력하신 내용을 다시 확인해주세요.");
            view = "/find_id";
        }

        return view;
    }

    @GetMapping("/find_password")
    public String find_passwordForm(Model model) {
        return "find_password";
    }

    // 비밀번호 찾기 메서드,  230507 장준원
    @PostMapping("/find_password")
    public String find_passwordFrom(@ModelAttribute("formData") FindPasswordRequest pwReq, Model model) {

        String email = pwReq.getEmail();
        String name  = pwReq.getName();
        String phNum = pwReq.getPhone_number();

        UserDO userDO = userService.findUser(email);

        if(userDO == null) {
            model.addAttribute("failMsg", "<script> alert('존재하지 않는 이메일입니다.'); </script>");
            return "find_password";
        }
        else if(!(userDO.getName().equals(name) && userDO.getPhone_number().equals(phNum))) {
            model.addAttribute("failMsg","<script> alert('일치하는 회원정보가 없습니다.'); </script>");
            return "find_password";
        }
        else {
            model.addAttribute("email", email);
            return "find_change_password";
        }
    }
    // 비밀번호 변경 메서드(찾기에서 변경),  230509 장준원
    @PostMapping("/find_change_password")
    public String changePassword(@RequestParam(value = "email") String email, @RequestParam(value = "newpwd") String newpwd, Model model) {

        String setEmail = email;
        userService.changePwd(setEmail, newpwd);

        return "main";
    }

    //5.8 양서림
    @GetMapping("/myPage")
    public String myPageForm(HttpSession session,Model model, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        String email = String.valueOf(session.getAttribute("email"));
        UserDO user = userService.findById(email); // 기존설정한 아이디와 일치하는 자료 db에서 가져옴
        model.addAttribute("user", user); //모델에 저장후 html에서 표시
        model.addAttribute("nickname", user.getNickname());
        return "myPage";
    }

    //5.9 양서림
    @PostMapping("/myPage")
    public String myPageEdit(@ModelAttribute("member") UserUpdateRequest userUpdateRequest, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        String email = String.valueOf(session.getAttribute("email"));
        UserDO check = userService.findNickname(userUpdateRequest.getNickname());
        UserDO numCheck = userService.findByPhoneNumber(userUpdateRequest.getPhone_number());

        if(check != null && !userUpdateRequest.getNickname().equals(String.valueOf(session.getAttribute("nickname")))){
            redirectAttributes.addFlashAttribute("duplicateNickname","이미 존재하는 닉네임 입니다.");
            return "redirect:/myPage";
        }

        if(numCheck != null && !userUpdateRequest.getPhone_number().equals(userService.findById(String.valueOf(session.getAttribute("email"))).getPhone_number())) {
            redirectAttributes.addFlashAttribute("duplicatePhoneNumber", "이미 존재하는 번호입니다.");
            return "redirect:/myPage";
        }


        userService.mypageupdate(userUpdateRequest, email); // 사용자가 입력한값 데베에 업데이트
        redirectAttributes.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        session.setAttribute("nickname", userUpdateRequest.getNickname());
        return "redirect:/myPage";
    }

    @GetMapping("/changepwd")
    public String ChangepwdForm(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        return "changepwd";
    }

    @PostMapping("/changepwd")
    public String Changepwd(@RequestParam(value = "current") String current, @RequestParam(value = "newpwd") String newpwd , Model model,
                            HttpSession session, RedirectAttributes re) {
        String email = String.valueOf(session.getAttribute("email"));
        UserDO userDO = userService.findUser(email);


        if(!userDO.checkPassword(current)){
            model.addAttribute("failMsg","현재 비밀번호를 잘못입력하셨습니다. 비밀번호를 확인해주세요.");
            return "changepwd";
        }
        else {
            re.addAttribute("msg","비밀번호가 변경되었습니다.");
            userService.changePwd(email, newpwd);
            return "redirect:/myPage";
        }
    }
    //0511- 손주현
    @PostMapping("/disableAccount")
    public String disalbeAccount(@RequestParam(value = "password") String password,
                                 HttpSession session, Model model) {
        String email = String.valueOf(session.getAttribute("email"));
        UserDO userDO = userService.findUser(email);

        if (!userDO.checkPassword(password)) {
            model.addAttribute("errorMsg", "비밀번호를 잘못 입력하셨습니다. 비밀번호를 확인해주세요.");
            return "disableAccount";
        }

        userService.deleteUser(email);
        model.addAttribute("msg", "회원탈퇴 처리 되었습니다.");
        session.removeAttribute("email");
        session.invalidate();
        return "/main";
    }


    //0511- 손주현
    @GetMapping("/disableAccount")
    public String disalbeAccountForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }

        return "disableAccount";
    }

    //희수
    //관리자 (회원 조회)
    @GetMapping("/memberManage")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("관리자")) {
            redirectAttributes.addFlashAttribute("notAvailable","관리자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        List<UserDO> members = userService.findMembers(page, postsPerPage);
        model.addAttribute("members", members);

        int totalPosts = userService.getTotalResults();
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "memberManage";
    }
    //희수
    //id(이메일),닉네임 조회
    @PostMapping("/memberManage/search")
    public String findidPost(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session,
                             @ModelAttribute("userSearchRequest") UserSearchRequest userSearchRequest, Model model, RedirectAttributes redirectAttributes) {
        if(userSearchRequest.getSearchtext().equals("")) {
            return list(1, model, session, redirectAttributes);
        }

        session.setAttribute("userSearchRequest", userSearchRequest);
        return "redirect:/memberManage/search";
    }

    @GetMapping("/memberManage/search")
    public String findid(@RequestParam(value = "page", defaultValue = "1") int page
            , Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("notLogin","로그인 후 이용 가능합니다.");
            return "redirect:/login";
        }
        else if(!String.valueOf(session.getAttribute("type")).equals("관리자")) {
            redirectAttributes.addFlashAttribute("notAvailable","관리자만 이용할 수 있습니다.");
            return "redirect:/main";
        }

        UserSearchRequest userSearchRequest = (UserSearchRequest)session.getAttribute("userSearchRequest");

        int postsPerPage = 10;
        int pageNavigationLinks = 5;

        List<UserDO> members = userService.findMember(userSearchRequest, page, postsPerPage);
        model.addAttribute("members", members);

        int totalPosts = userService.getTotalSearch(userSearchRequest);
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        int startPage = Math.max(1, page - (pageNavigationLinks / 2));
        int endPage = Math.min(startPage + pageNavigationLinks - 1, totalPages);

        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        session.setAttribute("userSearchRequest", userSearchRequest);

        return "memberSearch";
    }
    //희수
    //유저 삭제
    @PostMapping("/deleteMember")
    public String delectId(@RequestParam(value = "delete") String email, Model model) {
        userService.deleteMember(email);
        return "memberManage";
    }


    @ResponseBody
    @RequestMapping(value = "/ConfirmEmail", method = RequestMethod.POST)
    public String compareValues(@RequestBody EmailDto emailDto) {
        String newValue = "true";
        String email = emailDto.getValue();
        System.out.println(email);

        if(userService.ConfirmEmail(email)) {
            return newValue;
        }
        newValue = "false";
        return newValue;
    }

    @ResponseBody
    @RequestMapping(value = "/ConfirmNickname", method = RequestMethod.POST)
    public String ConFirmNickname1(@RequestBody EmailDto emailDto) {
        String newValue = "true";
        String nickname = emailDto.getNickname();

        if(userService.ConfirmNickname(nickname)) {
            return newValue;
        }
        newValue = "false";
        return newValue;
    }

    @ResponseBody
    @RequestMapping(value = "/ConfirmPhoneNumber", method = RequestMethod.POST)
    public String ConFirmPhoneNumber1(@RequestBody EmailDto emailDto) {
        String newValue = "true";
        String phone_number = emailDto.getPhone_number();

        if(userService.ConfirmPhoneNumber(phone_number)) {
            return newValue;
        }
        newValue = "false";
        return newValue;
    }
}
