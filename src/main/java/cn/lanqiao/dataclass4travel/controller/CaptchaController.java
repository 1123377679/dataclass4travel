package cn.lanqiao.dataclass4travel.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.pojo.dto.UserLoginDTO;
import cn.lanqiao.dataclass4travel.service.ITPzUserService;
import cn.lanqiao.dataclass4travel.service.impl.TPzUserServiceImpl;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class CaptchaController {
    @Value("${image.path}")
    private String imagePath;
    private final ResourceLoader resourceLoader;
    @Autowired
    private TPzUserServiceImpl userService;
    public CaptchaController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping("/getcaptcha")
    public CommonResult getCaptcha1(HttpSession session) {
        // 1.生成验证码到本地
        // 定义图形验证码的长和宽 (这个验证码的大小需要和自己前端的验证码的大小匹配)
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 40);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ".png";

        try {
            // 获取类路径下的文件资源
            Resource resource = resourceLoader.getResource(imagePath);
            File file = resource.getFile();
            // 将验证码图片写入到文件
            File captchaFile = new File(file, fileName);
            FileOutputStream fos = new FileOutputStream(captchaFile);
            lineCaptcha.write(fos);
            fos.close();
            // 将验证码信息存储到Session中
            session.setAttribute("captchaCode", lineCaptcha.getCode());
            System.out.println("验证码：" + lineCaptcha.getCode());
            HashMap<String, String> result = new HashMap<>();
            result.put("codeurl", "/image/" + uuid + ".png");
            result.put("codekey", uuid);
            return new CommonResult(200, "获取验证码成功",result);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult(500, "生成验证码失败");
        }
    }
//    验证验证码

}
