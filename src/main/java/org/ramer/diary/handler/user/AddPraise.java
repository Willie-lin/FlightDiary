package org.ramer.diary.handler.user;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.UserNotLoginException;
import org.ramer.diary.service.PraiseService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.UserUtils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 点赞和取消点赞.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class AddPraise{

    @Autowired
    private PraiseService praiseService;
    @Autowired
    private UserService userService;

    /**
     * 用户点赞.
     *
     * @param topic_id 分享UID
     * @param map the map
     * @param response the response
     * @param request the request
     * @param session the session
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping("/user/topic/praise/{topic_id}")
    public void praise(@PathVariable("topic_id") Integer topic_id, Map<String, Object> map,
            HttpServletResponse response, HttpSession session) throws IOException {
        response.setCharacterEncoding("utf-8");
        User user = (User) map.get("user");
        if (!UserUtils.checkLogin(session)) {
            //            if (!UserUtils.multiLogin(session, u)) {
            //                response.getWriter().write("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            //                return;
            //            }
            response.getWriter().write("麻麻说没登录不能点赞哒 !");
            return;
        }
        System.out.println("-----用户点赞-----");
        Topic topic = new Topic();
        topic.setId(topic_id);

        if (!praiseService.praise(user, topic)) {
            System.out.println("-----重复点赞-----");
            response.getWriter().write("你已经赞过了哒 !");
            return;
        }
        response.getWriter().write("恭喜你骚年,她已经悄悄收下你的赞 !");
    }

    /**
     * 取消点赞.
     *
     * @param topic_id 分享ID
     * @param user 登录用户
     * @param map the map
     * @param response JSP内置对象
     * @param request JSP内置对象
     * @param session JSP内置对象
     * @throws IOException 写入信息失败抛出IO异常
     */
    @RequestMapping("/user/topic/notPraise/{topic_id}")
    public void notPraise(@PathVariable("topic_id") Integer topic_id, User user, HttpServletResponse response,
            HttpSession session) throws IOException {
        System.out.println("取消点赞");
        response.setCharacterEncoding("utf-8");
        if (!UserUtils.checkLogin(session)) {
            User u = userService.getById(((User) session.getAttribute("user")).getId());
            if (!UserUtils.multiLogin(session, u)) {
                throw new UserNotLoginException("账号异地登陆！ 当前登陆失效，如果不是本人操作，请及时修改密码 !");
            }
            throw new UserNotLoginException("没登录的哦");
        }
        Topic topic = new Topic();
        topic.setId(topic_id);
        if (!praiseService.notPraise(topic, user)) {
            System.out.println("重复取消点赞");
            response.getWriter().write("重复取消点赞 !");
        }
        System.out.println("取消点赞成功");
        response.getWriter().write("取消点赞成功 !");
        return;
    }

}