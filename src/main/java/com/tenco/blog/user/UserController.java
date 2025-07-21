package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor // DI 처리
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * 회원 정보 수정 화면 요청
     */
    @GetMapping("/user/update-form")
    public String updateForm(Model model, HttpSession session) {

        User sessionUser = (User)session.getAttribute("sessionUser");
        User user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    /**
     * 회원 수정 기능 요청
     */
    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO reqDTO,
                         HttpSession session) {
        // 1. 인증검사
        // 2. 유효성 검사
        // 3. 서비스 계층 -> 회원 수정 기능 위임
        // 4. 세션 동기화 처리
        // 5. 리다이렉트 -> 회원 정보 화면 요청(새로운 request)
        reqDTO.validate();
        User user = (User)session.getAttribute("sessionUser");
        User updateUser = userService.updateById(user.getId(), reqDTO);
        session.setAttribute("sessionUser", updateUser);
        return "redirect:/user/update-form";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        log.info("회원 가입 요청 폼");
        return "user/join-form";
    }

    /**
     *
     * 회원 가입 기능 요청
     */
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {
        joinDTO.validate();
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    /**
     * 로그인 화면 요청
     */
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    /**
     * 로그인 요청
     */
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO, HttpSession session) {
        loginDTO.validate();
        User user =  userService.login(loginDTO);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("user/upload-profile-image")
    public String uploadProfileImage(@RequestParam(name = "profileImage")MultipartFile multipartFile,
                                     HttpSession httpSession) {
        // 업로드 로직 구현 시작

        return "redirect:/user/update-form";
    }

    @PostMapping("user/delete-profile-image")
    public String deleteProfileImage(HttpSession httpSession) {

        // 파일 삭제 로직 시작

        return "redirect:/user/update-form";
    }
}
