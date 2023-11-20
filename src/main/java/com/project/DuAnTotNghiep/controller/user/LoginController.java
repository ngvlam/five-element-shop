package com.project.DuAnTotNghiep.controller.user;

import com.project.DuAnTotNghiep.dto.MailInfo;
import com.project.DuAnTotNghiep.entity.Account;
import com.project.DuAnTotNghiep.entity.Role;
import com.project.DuAnTotNghiep.service.AccountService;
import com.project.DuAnTotNghiep.service.CookieService;
import com.project.DuAnTotNghiep.service.MailerService;
import com.project.DuAnTotNghiep.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
@Controller
public class LoginController {

    private final AccountService accountService;

    private final SessionService sessionService;

    private final CookieService cookieService;

    private final MailerService mailerService;

    public LoginController(AccountService accountService, SessionService sessionService, CookieService cookieService, MailerService mailerService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.mailerService = mailerService;
    }

    @GetMapping("/login")
    public String viewLogin(Model model) {

        return "user/login";
    }
//
////
////    @PostMapping("/login")
////    public String PostLogin(@Valid @ModelAttribute Account acc,
////                            @RequestParam(name = "remember",defaultValue = "false") Boolean rm, RedirectAttributes redirectAttributes
////    ){
////
////    }
//
//    @GetMapping("/logout")
//    public String logout(Model model){
//        sessionService.remove("user");
//        sessionService.remove("isLogin");
//        cookieService.remove("email");
//        cookieService.remove("password");
//        return "redirect:/login";
//    }

    @GetMapping("/register")
    public String register(Model model,@ModelAttribute("Account") Account account){
        model.addAttribute("layoutUser", "user/register");
        return "user/layout-user";
    }


    @PostMapping("/register")
    public String saveregister(Model model,@Validated @ModelAttribute("Account") Account account) throws MessagingException {
        Account accountByEmail= accountService.findByEmail(account.getEmail());

        if(accountByEmail !=null ){
            model.addAttribute("Messges","Email da ton tai !");
            model.addAttribute("layoutUser", "user/register");
            return "user/layout-user";
        } else {

            String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // chuỗi các ký tự được phép
            int length = 10; // độ dài của chuỗi ký tự

            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(allowedChars.length());
                char randomChar = allowedChars.charAt(randomIndex);
                sb.append(randomChar);
            }

            String randomString = sb.toString();
            System.out.println(randomString);
            Date date=new Date();
            account.setPassword(randomString);
            account.setCode(randomString);

            account.setNonLocked(true);
            Role role = new Role();
            role.setId(1L);
//            Account account1=new Account(null,account.getCode(),account.getName(),account.getBirthDay(),account.getPhoneNumber(),account.getEmail(),account.getPassword(),account.getCreateDate(),account.getUpdateDate(),account.getStatus(),role);

//            accountService.save(account1);

            String subject="Tạo tài khoản thành công ";
            String body="Thông tin bảo mật, tài khoản và mật khẩu lần lượt là : "+account.getEmail()+" , "+ randomString;

            mailerService.send(new MailInfo(account.getEmail(),subject,body));

            model.addAttribute("Messges","Tao tai khoan thanh con vui long kiem tra mail!");
        }
        model.addAttribute("layoutUser", "user/register");
        return "user/layout-user";
    }

}
