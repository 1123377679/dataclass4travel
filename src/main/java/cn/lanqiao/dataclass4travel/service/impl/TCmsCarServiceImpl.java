package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.TCmsCarMapper;
import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import cn.lanqiao.dataclass4travel.service.TCmsCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class TCmsCarServiceImpl extends ServiceImpl<TCmsCarMapper, TCmsCar> implements TCmsCarService {
}
