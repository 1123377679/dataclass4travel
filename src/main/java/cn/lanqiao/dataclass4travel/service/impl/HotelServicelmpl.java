package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.HotelMapper;
import cn.lanqiao.dataclass4travel.pojo.Hotel;
import cn.lanqiao.dataclass4travel.service.HotelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HotelServicelmpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {
}
