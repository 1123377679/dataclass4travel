package cn.lanqiao.dataclass4travel.service;

import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TCmsCarService extends IService<TCmsCar> {

    void updateById(String id);
}
