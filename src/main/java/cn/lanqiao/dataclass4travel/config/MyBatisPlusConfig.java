package cn.lanqiao.dataclass4travel.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 李某人
 * @Date: 2024/11/13/00:18
 * @Description:
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }
}
