package cn.lanqiao.dataclass4travel;

import cn.lanqiao.pojo.TPzUser;
import cn.lanqiao.service.TPzUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Dataclass4travelApplicationTests {
    @Autowired
    TPzUserService tPzUserService;

    @Test
    void contextLoads() {
    }
    @Test
    void test1(){
        //查询所有
        List<TPzUser> list = tPzUserService.list();
        System.out.println(list);
    }
}
