package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    //@Autowired
    @Resource
    UserMapper userMapper;

    @Override
    public boolean save(User entity) {
        if(StrUtil.isBlank(entity.getName())){
            entity.setName(entity.getUsername());
        }
        if(StrUtil.isBlank(entity.getPassword())){
            entity.setPassword("123"); //默认密码123
        }
        if(StrUtil.isBlank(entity.getRole())){
            entity.setRole("用户");   //默认用户名
        }
        return super.save(entity);
    }

    //    public void insertUser(User user){
//        userMapper.insert(user);
//    }
//
//    public void updateUser(User user) {
//        userMapper.updateUser(user);
//    }
//
//    public void deleteUser(Integer id) {
//        userMapper.deleteUser(id);
//    }
//
//    public void batchDeleteUser(List<Integer> ids) {
//        for(Integer id : ids){
//            userMapper.deleteUser(id);
//        }
//    }
//
//    public List<User> selectAll() {
//        return userMapper.selectAll();
//    }
//
//    public User selectById(Integer id) {
//        return userMapper.selectById(id);
//    }
//
//    public List<User> selectByName(String name) {
//        return userMapper.selectByName(name);
//    }
//
//    public User selectByMore(String username, String name) {
//        return userMapper.selectByMore(username, name);
//    }
//
//    public List<User> selectByFuzzy(String username, String name) {
//        return userMapper.selectByFuzzy(username, name);
//    }
//
//    public Page<User> selectByPage(Integer pageNum, Integer pageSize, String username, String name) {
//        Integer skipNum = (pageNum - 1) * pageSize;
//        Page<User> page = new Page<>();
//        List<User> userList = userMapper.selectByPage(skipNum, pageSize, username, name);
//        Integer total = userMapper.selectCountByPage(username, name);
//        page.setTotal(total);
//        page.setList(userList);
//        return page;
//    }

    public User selectByUsername(String username){
        //根据用户名查询用数据库的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); // eq => == where username = #{username}
        return getOne(queryWrapper); //select * from user where username = #{username}
    }

//验证用户账户是否合法
    public User login(User user) {
        User dbUser = selectByUsername(user.getUsername());
        //抛出一个自定义异常
        if(dbUser == null){
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        //生成token
        String token = TokenUtils.creatToken(dbUser.getId().toString(), user.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User register(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new ServiceException("用户名已存在");
        }
        user.setName(user.getUsername());
        userMapper.insert(user);
        return user;
    }

    public void resetPassword(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser == null){
            throw new ServiceException("用户不存在");
        }
        if(!user.getPhone().equals(dbUser.getPhone())){
            throw new ServiceException("验证错误");
        }
        dbUser.setPassword("112233"); //重置密码
        updateById(dbUser);
    }

}
