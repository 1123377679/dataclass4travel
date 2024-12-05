package cn.lanqiao.dataclass4travel.service.impl;

import cn.lanqiao.dataclass4travel.mapper.UserMapper;
import cn.lanqiao.dataclass4travel.pojo.User;
import cn.lanqiao.dataclass4travel.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.service.impl
 * @CreateTime: 2021-05-18 16:18
 * @Description: TODO
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
