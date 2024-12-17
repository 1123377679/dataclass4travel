package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.HotelMapper;
import cn.lanqiao.dataclass4travel.pojo.*;
import cn.lanqiao.dataclass4travel.service.HotelService;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;
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
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private TYwOrderService tYwOrderService;

    @Autowired
    private HotelMapper hotelMapper;
    @RequestMapping("/hotel_list")
    public String list(@RequestParam(defaultValue = "1") long pageNumber,
                       @RequestParam(defaultValue = "7") long pageSize,
                       Model model) {
        // 构造查询条件
        QueryWrapper<Hotel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);  // 确保查询条件正确
        queryWrapper.orderByDesc("ADD_TIME");
        // 调用 service 层的分页查询方法
        IPage page = hotelService.page(new Page<Hotel>(pageNumber, pageSize), queryWrapper);
        // 包装分页信息
        PageHelper<Hotel> hotelPageHelper = new PageHelper<Hotel>(pageNumber, pageSize, page.getPages(), page.getTotal(),page.getRecords());
        // 将分页数据传递给前端页面
        model.addAttribute("pagerHelper", hotelPageHelper);
        //应该用工具类返回撒,哦哦你用了的
        return "hotel/hotelList";  // 返回视图
    }

    //跳转新增页面
    @RequestMapping("/hotel_toadd")
    public String toAdd(){
        return "hotel/hotelAdd";
    }

    //新增功能
    @PostMapping("/hotel_add")
    @ResponseBody
    public CommonResult add( Hotel hotel, HttpSession session){
        try {
            // 当前时间
            String nowTime = DateUtils.getNowTime();
            hotel.setAddTime(nowTime);
            // 获取当前管理员信息
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            // 没什么必要，以防万一
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            hotel.setAddUserId(addAdmin.getId());
            // 操作数据库进行添加
            System.out.println("要新增的对象是:" + hotel);
            Object savedHotel = hotelService.save(hotel);
            if (savedHotel == null) {
                return new CommonResult(304, "保存失败");
            }
            // 返回成功结果
            return new CommonResult(200, "请求成功", savedHotel);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500, "请求异常: " + e.getMessage());  // 返回500状态码并附带异常信息
        }
    }

    //跳转详情页面
    @GetMapping("hotel_toEdit/{id}")
    public String todetail(@PathVariable("id") String id,Model model){
        Hotel byId = hotelService.getById(id);
        model.addAttribute("entity",byId);
        return "hotel/hotelEdit";
    }
    //更新
    @ResponseBody
    @PostMapping("hotel_update")
    public CommonResult update(Hotel hotel,HttpSession session){
        try {
            // 是数据库中原对象
            Hotel old_hotel = hotelService.getById(hotel.getId());
            // 如果上传了新照片，就需要删除老照片
            // 所以if来判断旧新的图片地址是不是一样的，不一样，就删掉老照片
            if (!hotel.getImgUrl().equals(old_hotel.getImgUrl())){
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = old_hotel.getImgUrl();
                realPath = realPath.substring(1,realPath.length())+"static"+filePath;
                File f = new File(realPath);
                if (f.exists()){
                    f.delete();
                    System.out.println("删除了老照片，地址是："+realPath);
                }
            }
            //设置当前系统时间
            hotel.setModifyTime(DateUtils.getNowTime());
            //设置新增酒店的人
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            hotel.setAddUserId(addAdmin.getId());
            System.out.println("要更新的对象是："+hotel);
            return new CommonResult(200,"请求成功",hotelService.updateById(hotel));

        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }

    //删除
    @GetMapping("hotel_delete/{id}")
    @ResponseBody
    public CommonResult delete(Hotel hotel,@PathVariable("id") String id){
        // 待做.判断该酒店发布没
        Hotel byId = hotelService.getById(id);
        hotel.setDeleteStatus(1L);
        hotelService.updateById(byId);
        return new CommonResult(200,"请求成功");
    }
    //查看详情
    @GetMapping("/hotel_todetail/{id}")
    public String toDetail(@PathVariable("id") String id,Model model){
        Hotel byId = hotelService.getById(id);
        model.addAttribute("entity",byId);
        return "hotel/hotelView";
    }
//     前台
    @RequestMapping("/portal_hotel_list")
    public String sceniclist(@RequestParam(defaultValue = "1") long pageNumber,
                       @RequestParam(defaultValue = "7") long pageSize,
                       Model model) {
        // 构造查询条件
        QueryWrapper<Hotel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);  // 确保查询条件正确
        queryWrapper.orderByDesc("ADD_TIME");
        // 调用 service 层的分页查询方法
        IPage page = hotelService.page(new Page<Hotel>(pageNumber, pageSize), queryWrapper);
        // 包装分页信息
        PageHelper<Hotel> hotelPageHelper = new PageHelper<Hotel>(pageNumber, pageSize, page.getPages(), page.getTotal(),page.getRecords());
        // 将分页数据传递给前端页面
        model.addAttribute("pagerHelper", hotelPageHelper);
        return "portal/hotelAccommodation";  // 返回视图
    }

    //跳转前台详情页面
    @GetMapping("/portal_hotel_toview/{id}")
    public String toview(@PathVariable("id") String id,Model model){
        Hotel byId = hotelService.getById(id);
        model.addAttribute("entity",byId);
        return "portal/hotelAccommodationView";
    }
    // 酒店数据分析
    @RequestMapping("tohotelData")
    public String hotelData(Model model){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.select("count(id),STATE");
        queryWrapper.groupBy("STATE");
        queryWrapper.orderByAsc("STATE");

        List<Map<String,Object>> list = hotelService.listMaps(queryWrapper);
        Map<String, Object> map0 = list.get(0);
        Object count_0 = map0.get("count(id)");

        Map<String, Object> map1 = list.get(1);
        Object count_1 = map1.get("count(id)");

        Map<String, Object> map2 = list.get(2);
        Object count_2 = map2.get("count(id)");

        String datas="["+count_0+", "+count_1+", "+count_2+"]";

        model.addAttribute("datas",datas);

        return "data/hotelData";

    }
    //酒店预定
    @GetMapping("user_tohotelOrder/{id}")
    public String createHotelOrder(@PathVariable String id, HttpSession session,Model model){
        Hotel hotel = hotelService.getById(id);
        try {
            TPzUser user = (TPzUser) session.getAttribute("user");
            TYwOrder tYwOrder = new TYwOrder();
            tYwOrder.setAddUserId(user.getId());
            tYwOrder.setAddTime(DateUtils.getNowTime());
            tYwOrder.setDeleteStatus(0L);
            tYwOrder.setUserId(user.getId());
            tYwOrder.setUserName(user.getUserName());
            tYwOrder.setProductId(id);
            tYwOrder.setProductType(2L);
            tYwOrder.setState(0L);
            tYwOrder.setOrderCode(DateUtils.getOrderId());
            tYwOrder.setOrderTime(DateUtils.getNowTime());
            tYwOrder.setLinkTel(hotel.getLinkPhone());
            tYwOrder.setIcCode(user.getIcCode());

            tYwOrder.setProductName(hotel.getHotelName());
            tYwOrder.setFee(hotel.getPrice());
            tYwOrder.setSetoffTime(DateUtils.getNowTime());
            tYwOrder.setImgUrl(hotel.getImgUrl());


            tYwOrderService.save(tYwOrder);
            model.addAttribute("msg","预订成功，请前往会员中心-我的订单查看订单");

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg","预定异常");

        }finally {

        }
        model.addAttribute("entity",hotel);
        return "portal/hotelAccommodationView";

    }
}
