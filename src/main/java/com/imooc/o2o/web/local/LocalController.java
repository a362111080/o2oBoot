package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {
    /**
     * 绑定账号页路由
     */
    @RequestMapping(value = "/accountbind", method = RequestMethod.GET)
    private String accountbind() {
        return "local/accountbind";
    }

    /**
     * 修改密码路由
     */
    @RequestMapping(value = "/changepsw", method = RequestMethod.GET)
    private String changepaw() {
        return "local/changepsw";
    }

    /**
     * 登陆页路由
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login(){
        return "local/login";
    }
}
