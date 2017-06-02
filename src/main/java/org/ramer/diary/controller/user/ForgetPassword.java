package org.ramer.diary.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ramer.diary.constant.MessageConstant;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.LinkInvalidException;
import org.ramer.diary.exception.PasswordNotMatchException;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.MailUtils;
import org.ramer.diary.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

/**
 * 忘记密码，用于重置密码.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class ForgetPassword{
    //全局成功页面
    final String SUCCESS = PageConstant.SUCCESS;

    //  密码修改成功信息
    final String SUCCESS_CHANGEPASS = MessageConstant.SUCCESS_MESSAGE;

    @Autowired
    UserService userService;

    /**
     * 重定向到忘记密码页面.
     * @param email 用户绑定的邮箱
     * @param map the map
     * @return 引导到忘记密码页面
     */
    @GetMapping("/user/forwardForgetPassword")
    public String forwardForgetPassword(
            @RequestParam(value = "email", required = false, defaultValue = "") String email, Map<String, Object> map) {
        log.debug("引导到忘记用户密码页面");
        if (!email.equals("")) {
            map.put("email", "email");
        }
        return "forget_pass";
    }

    /**
     * 发送邮件.
     *
     * @param email 未加密的邮箱地址
     * @param session the session
     * @param response JSP内置对象
     * @throws IOException 写入信息失败抛出IO异常
     */
    @PostMapping("/user/forgetPass/sendMail")
    public void sendMailToResetPass(@RequestParam("email") String email, HttpSession session,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        if (email.trim() == null || email.trim().equals("")) {
            response.getWriter().write("臣妾还不知道发到哪儿呐");
            return;
        }
        if (!MailUtils.isEmail(email)) {
            response.getWriter().write("您输入的不是邮箱哒 ^o^||");
            return;
        }
        String encodedEmail = Encrypt.execEncrypt(email, true);
        User user = userService.getByEmail(encodedEmail);
        //    发送邮件之前判断是否存在,防止用户而已发送邮件
        if (user == null) {
            response.getWriter().write("您输入的邮箱未注册哟");
            return;
        }
        log.debug("邮箱认证通过");
        String servletName = session.getServletContext().getServletContextName();
        String content = "<h3>请点击下面的链接继续重置密码,五分钟内有效</h3><br>" + "<a href='http://localhost:8080/" + servletName
                + "/user/forwardForgetPassword?email=" + encodedEmail + "'>http://localhost:8080/" + servletName
                + "/user/forgetPassword/" + email + "</a>";
        String top = "来自旅行日记的重置密码邮件";
        MailUtils.sendMail(email, top, content);
        Calendar calendar = Calendar.getInstance();
        //		时间是五分钟之后
        calendar.add(Calendar.MINUTE, 5);
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(calendar.getTime()).toString();
        user.setExpireTime(expireTime);
        userService.newOrUpdate(user);
        response.getWriter().write("嗖.......... 到家啦 ^v^,查收邮件后再继续操作哦");
    }

    /**
     * 忘记密码.
     *
     * @param email 已加密邮箱
     * @param password 新密码
     * @param repassword 密码重复
     * @param session the session
     * @return 密码修改成功: 返回个人主页,失败: 返回密码修改页面
     */
    @PutMapping("/user/forgetPassword")
    public String forgetPassword(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("repassword") String repassword, HttpSession session) {

        log.debug("忘记密码,重置密码");
        User user = userService.getByEmail(email);
        //    获取当前时间,并格式化
        String expireTime = new SimpleDateFormat("yyMMddhhmmss").format(Calendar.getInstance().getTime());
        if (expireTime.compareTo(user.getExpireTime()) > 0) {
            log.debug("链接已失效");
            throw new LinkInvalidException();
        }
        if (!password.equals(repassword)) {
            throw new PasswordNotMatchException();
        }
        user.setPassword(Encrypt.execEncrypt(password, false));
        if (userService.newOrUpdate(user) == null) {
            throw new SystemWrongException();
        }
        UserUtils.execSuccess(session, SUCCESS_CHANGEPASS);
        return SUCCESS;
    }

}