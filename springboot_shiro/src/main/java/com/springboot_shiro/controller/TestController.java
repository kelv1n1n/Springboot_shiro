package com.springboot_shiro.controller;

import com.springboot_shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello";
    }

    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(Model model){
        model.addAttribute("name","梨花");
        return "hello";
    }

    @RequestMapping("/add")
    public String add(){
        return "/user/user_add";
    }

    @RequestMapping("/update")
    public String update(){
        return "/user/user_update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/unAuth")
    public String unAuth(){
        return "noAuth";
    }

    /**
     * 登录逻辑处理
     */
    @RequestMapping("/login")
    public String login(String name, String password, Model model){
        //使用shiro 编写认证操作
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);
        //执行登录方法
        try {
            subject.login(token);//去到doGetAuthenticationInfo方法
            //无异常即登陆成功 跳转到主页 hello页面
            return "redirect:/testThymeleaf";
        } catch (UnknownAccountException e) {
            //有异常登陆失败
            //用户名错误
            model.addAttribute("msg","用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException i){
            //密码错误
            model.addAttribute("msg","密码不存在");
            return "login";
        }
    }
}
