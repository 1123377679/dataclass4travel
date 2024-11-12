package cn.lanqiao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.lanqiao.dataclass4travel.mapper")
public class Dataclass4travelApplication {

    public static void main(String[] args) {
        SpringApplication.run(Dataclass4travelApplication.class, args);
    }

}
