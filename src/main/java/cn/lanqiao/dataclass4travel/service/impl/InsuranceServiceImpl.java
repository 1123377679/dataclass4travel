package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.InsuranceMapper;
import cn.lanqiao.dataclass4travel.pojo.TCmsInsurance;
import cn.lanqiao.dataclass4travel.service.InsuranceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class InsuranceServiceImpl extends ServiceImpl<InsuranceMapper, TCmsInsurance> implements InsuranceService {
    @Override
    public Iterator<TCmsInsurance> iterator() {
        return null;
    }
}
