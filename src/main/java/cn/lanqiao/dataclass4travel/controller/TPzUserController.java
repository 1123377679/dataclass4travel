package cn.lanqiao.dataclass4travel.controller;


import cn.hutool.core.lang.UUID;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.service.ITPzUserService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyh
 * @since 2024-11-26
 */
@RequiredArgsConstructor
@RestController
public class TPzUserController {
    private final ITPzUserService tPzUserService;//
        @PostMapping("/user_register")
    public CommonResult register(TPzUser tPzUser) {
            TPzUser user = tPzUserService.lambdaQuery().eq(TPzUser::getUserName, tPzUser.getUserName()).one();
            if (user != null){
                return new CommonResult(400, "用户名已存在");
            }

            //设置唯一id
            tPzUser.setId(UUID.randomUUID().toString().replace("-",""));
//            设置添加时间
            tPzUser.setAddTime(LocalDateTime.now());
            System.out.println("要新增的对象是:" + tPzUser);
            tPzUserService.save(tPzUser);
            return new CommonResult(200, "注册成功");
    }
}
