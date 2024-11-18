package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.TravelRouteMapper;
import cn.lanqiao.dataclass4travel.pojo.TCmsTravelRoute;
import cn.lanqiao.dataclass4travel.service.TCmsTravelRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TCmsTravelRouteServiceImpl extends ServiceImpl<TravelRouteMapper, TCmsTravelRoute> implements TCmsTravelRouteService {
}
