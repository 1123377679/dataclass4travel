package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.TravelRouteMapper;
import cn.lanqiao.dataclass4travel.pojo.TCmsTravelRoute;
import cn.lanqiao.dataclass4travel.service.TCmsTravelRouteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TCmsTravelRouteServiceImpl extends ServiceImpl<TravelRouteMapper, TCmsTravelRoute> implements TCmsTravelRouteService {
}
