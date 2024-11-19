package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.Dataclass4travelApplication;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: 李某人
 * @Date: 2024/11/18/19:10
 * @Description: 通用的文件上传类
 */
@Controller
public class UploadFileController {
    @RequestMapping("/uploadImg/{address}")
    @ResponseBody
    public CommonResult upload(@PathVariable("address") String address, @RequestParam("file") MultipartFile multipartFile) throws FileNotFoundException {
        if (!multipartFile.isEmpty() && multipartFile.getSize()>0) {
            //保证是有参数的
            //获取上传的原始文件名
            String filename = multipartFile.getOriginalFilename();
            //获取文件的拓展名
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            //文件真实上传的路径
            String realPath = ResourceUtils.getURL("classpath:").getPath();
            // System.out.println("1"+realPath); // D:/IT/LanqiaoJavaProject/dataclass4travel/target/classes/
            String filePath = "/"+address+"/"+new Date().getTime()+"."+suffix;
            // System.out.println("3"+filePath); // car/1731929342276.jpg
            realPath = realPath.substring(1,realPath.length())+"static"+filePath;
            // System.out.println("2"+realPath); // D:/IT/LanqiaoJavaProject/dataclass4travel/target/classes/static/car/1731929342276.jpg
            File newFile = new File(realPath);
            try {
                //生成路径
                multipartFile.transferTo(newFile);
                System.out.println("文件上传的路径:"+realPath);
                return new CommonResult(200,"文件上传成功",filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new CommonResult(500,"文件上传失败");
            }
        }else {
            return new CommonResult(404,"文件上传为空");
        }
    }
}
