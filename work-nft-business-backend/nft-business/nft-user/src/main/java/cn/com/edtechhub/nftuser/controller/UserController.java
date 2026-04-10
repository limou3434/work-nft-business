package cn.com.edtechhub.nftuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /**
     * 测试接口
     *
     * @return String
     */
    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
