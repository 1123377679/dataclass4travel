package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TCmsScenicSpot;
import cn.lanqiao.dataclass4travel.service.TCmsScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TCmsScenicSpotController {
    @Autowired
    private TCmsScenicSpotService tCmsScenicSpotService;
}
