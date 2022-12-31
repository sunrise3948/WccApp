package com.chasingdns.controller;


import com.chasingdns.entity.User;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/reset/password")
    public String showResetPassword(Model model){
        return "resetPassword";
    }

    @PostMapping("/reset/initiate")
    public String showResetInitiate(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = getSiteURL(request) + "/reset/complete?token=" + token;
            sendEmail(email, resetPasswordLink);

            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (NotFoundException e) {
            model.addAttribute("error", "User not found");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "resetPassword";
    }

    @GetMapping("/reset/complete")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
        model.addAttribute("message", "Invalid Token");
        return "message";
        }
        return "reset_password_form";
    }

    @PostMapping("/reset/complete")
    public String submitResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.getByResetPasswordToken(token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }
        return "login";
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    private void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("support@wcc.com", "Reset Password Request");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
        + "<p>You have requested to reset your password.</p>"
        + "<p>Click the link below to change your password:</p>"
        + "<p><a href=\"" + link + "\">Change my password</a></p>"
        + "<br>"
        + "<p>Ignore this email if you do remember your password, "
        + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
