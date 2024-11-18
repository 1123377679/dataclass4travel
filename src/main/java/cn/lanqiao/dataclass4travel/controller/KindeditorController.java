package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.utils.KindEditorResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;


@Controller
public class KindeditorController {

    /**
     * 通用的Kindeditor后台处理方法
     * @param address
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadimg_kindeditor/{address}")
    public KindEditorResult upload(@PathVariable("address") String address, @RequestParam("file") MultipartFile multipartFile) throws Exception{

        if (!StringUtils.isEmpty(multipartFile) && multipartFile.getSize()>0){
            //获取原始的文件名
            String filename = multipartFile.getOriginalFilename();
            //获取文件的扩展名
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            //文件上传的真实路径
            String realPath = ResourceUtils.getURL("classpath:").getPath();
            String filePath="/"+address+"/"+new Date().getTime()+"."+suffix;
            realPath=realPath.substring(1,realPath.length())+"static"+filePath;
            File newfile = new File(realPath);
            try {
                multipartFile.transferTo(newfile);
                System.out.println("文件上传路径是:"+realPath);
                return new KindEditorResult(0,filePath,null);
            } catch (IOException e) {
                e.printStackTrace();
                return new KindEditorResult(1,null,"文件上传异常");
            }
        }else {
            return new KindEditorResult(1,null,"文件上传异常");
        }
    }
}
