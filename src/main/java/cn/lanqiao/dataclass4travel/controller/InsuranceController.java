package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.service.InsuranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class InsuranceController {
    @Autowired
    private InsuranceService insuranceService;



}
