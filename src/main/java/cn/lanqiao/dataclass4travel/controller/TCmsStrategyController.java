package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.TCmsStrategyMapper;


import cn.lanqiao.dataclass4travel.pojo.*;

import cn.lanqiao.dataclass4travel.service.TCmsStrategyService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
public class TCmsStrategyController {

    @Autowired
    private TCmsStrategyService tCmsStrategyService;

    @Autowired
    private TCmsStrategyMapper tCmsStrategyMapper;

    @RequestMapping("/strategy_list")
    public String list(@RequestParam(defaultValue = "1") long pageNumber,
                       @RequestParam(defaultValue = "7") long pageSize,
                       Model model) {

        QueryWrapper<TCmsStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsStrategyService.page(new Page<TCmsStrategy>(pageNumber, pageSize), queryWrapper);

        PageHelper<TCmsStrategy> strategyPageHelper = new PageHelper<TCmsStrategy>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", strategyPageHelper);

        return "strategy/strategyList";
    }

    @RequestMapping("/strategy_toAdd")
    public String toAdd() {
        return "strategy/strategyAdd";
    }

    @PostMapping("/strategy_Add")
    @ResponseBody
    public CommonResult add(TCmsStrategy tCmsStrategy, HttpSession session) {
        try {

            String nowTime = DateUtils.getNowTime();
            tCmsStrategy.setAddTime(nowTime);
            // 获取当前管理员信息
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");

            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsStrategy.setAddUserId(addAdmin.getId());
            // 操作数据库进行添加
            System.out.println("要新增的对象是:" + tCmsStrategy);
            Object savedTCmsStrategy = tCmsStrategyService.save(tCmsStrategy);
            if (savedTCmsStrategy == null) {
                return new CommonResult(304, "保存失败");
            }
            // 返回成功结果
            return new CommonResult(200, "请求成功", savedTCmsStrategy);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500, "请求异常: " + e.getMessage());  // 返回500状态码并附带异常信息
        }
    }

    @GetMapping("/strategy_View/{id}")
    public String toDetail(@PathVariable("id") String id, Model model) {
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        model.addAttribute("entity", byId);
        return "strategy/strategyView";
    }

    @GetMapping("/strategy_toupdate/{id}")
    public String toEdit(@PathVariable("id") String id, Model model) {
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        model.addAttribute("entity", byId);
        return "strategy/strategyEdit";
    }

    @ResponseBody
    @PostMapping("strategy_update")
    public CommonResult update(TCmsStrategy tCmsStrategy, HttpSession session) {
        try {
            // 是数据库中原对象
            TCmsStrategy old = tCmsStrategyService.getById(tCmsStrategy.getId());
            // 如果上传了新照片，就需要删除老照片
            // 所以if来判断旧新的图片地址是不是一样的，不一样，就删掉老照片
            if (!tCmsStrategy.getImgUrl().equals(old.getImgUrl())) {
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = old.getImgUrl();
                realPath = realPath.substring(1, realPath.length()) + "static" + filePath;
                File f = new File(realPath);
                if (f.exists()) {
                    f.delete();
                    System.out.println("删除了老图片，地址是：" + realPath);
                }
            }
            //设置当前系统时间
            tCmsStrategy.setModifyTime(DateUtils.getNowTime());
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsStrategy.setAddUserId(addAdmin.getId());
            System.out.println("要更新的对象是：" + tCmsStrategy);
            return new CommonResult(200, "请求成功", tCmsStrategyService.updateById(tCmsStrategy));

        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500, "请求失败");

        }
    }

    @GetMapping("/strategy_delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable("id") String id){
        //补代码时需要判断这个车票是否在使用，如果在使用要提示暂时不能删除
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        byId.setDeleteStatus(1L);//删除状态
        tCmsStrategyService.updateById(byId);
        log.info("删除攻略信息",id);

        return new CommonResult(200,"请求成功");
    }



    @RequestMapping("/portal_strategy_list")
    public String portalList(@RequestParam(defaultValue = "1") Long pageNumber,
                             @RequestParam(defaultValue = "7") Long pageSize,
                             @RequestParam(defaultValue = "") String title,
                             Model model) {
        QueryWrapper<TCmsStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", "0");
        queryWrapper.orderByDesc("ADD_TIME");
        if (!"".equals(title)) {
            queryWrapper.like("TITLE", title);
        }
        IPage page = tCmsStrategyService.page(new Page<TCmsStrategy>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsStrategy> pageHelper = new PageHelper<TCmsStrategy>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "portal/strategy";
    }


    @GetMapping("/portal_strategy_view/{id}")
    public String toview(@PathVariable("id") String id, Model model) {
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        model.addAttribute("entity", byId);
        return "portal/strategyView";
    }

    @RequestMapping("/strategy")
    public String tostrategy(@RequestParam(defaultValue = "1") long pageNumber,
                             @RequestParam(defaultValue = "7") long pageSize,
                             Model model) {
        // 构造查询条件
        QueryWrapper<TCmsStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);  // 确保查询条件正确
        queryWrapper.orderByDesc("ADD_TIME");
        // 调用 service 层的分页查询方法
        IPage page = tCmsStrategyService.page(new Page<TCmsStrategy>(pageNumber, pageSize), queryWrapper);
        // 包装分页信息
        PageHelper<TCmsStrategy> PageHelper = new PageHelper<TCmsStrategy>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        // 将分页数据传递给前端页面
        model.addAttribute("pagerHelper", PageHelper);
        return "portal/strategy";  // 返回视图    }
    }





    @RequestMapping ("/tostrategyData")
    public String strategyData(Model model){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.select("count(id),STATE");
        queryWrapper.groupBy("STATE");
        queryWrapper.orderByAsc("STATE");

        List<Map<String,Object>> list = tCmsStrategyService.listMaps(queryWrapper);

// 检查列表长度并初始化默认值
        int size = list.size();
        Object count_0 = null;
        Object count_1 = null;
        Object count_2 = null;

        if (size > 0) {
            Map<String, Object> map0 = list.get(0);
            count_0 = map0.get("count(id)");
        }

        if (size > 1) {
            Map<String, Object> map1 = list.get(1);
            count_1 = map1.get("count(id)");
        }

        if (size > 2) {
            Map<String, Object> map2 = list.get(2);
            count_2 = map2.get("count(id)");
        }

// 构建数据字符串，考虑可能的null值
        String datas = "["
                + (count_0 != null ? count_0.toString() : "0") + ", "
                + (count_1 != null ? count_1.toString() : "0") + ", "
                + (count_2 != null ? count_2.toString() : "0")
                + "]";

        model.addAttribute("datas", datas);



        return "data/strategyData";
    }
//    数据分析



//    数据预定

}