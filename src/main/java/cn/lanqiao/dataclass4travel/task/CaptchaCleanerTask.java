package cn.lanqiao.dataclass4travel.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
public class CaptchaCleanerTask {
    private final ResourceLoader resourceLoader;

    @Value("${image.path}")
    private String imagePath;

    public CaptchaCleanerTask(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            // 获取 target/classes 目录的绝对路径
            File classesDir = new File(getClass().getResource("/").getFile());
            File imageDir = new File(classesDir, "CaptchaImage");

            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void cleanExpiredCaptchas() {
//        System.out.println("开始清理过期验证码图片...");
        try {
            Resource resource = resourceLoader.getResource(imagePath);
            File imageDir = resource.getFile();
            if (imageDir.exists() && imageDir.isDirectory()) {
                for (File file : imageDir.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".png")) {
                        long lastModified = file.lastModified();
                        long currentTime = System.currentTimeMillis();
                        long diffInMinutes = (currentTime - lastModified) / (1000 * 60);

                        if (diffInMinutes > 3) {
                            FileUtil.del(file);
//                            System.out.println("删除过期验证码图片: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
