package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@CrossOrigin //解决网络跨域问题
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //新增用户信息
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        try {
            userService.save(user);
        }
        catch (Exception e){
            if(e instanceof DuplicateKeyException){ //不是DataFormatException，而是被springboot接管了
                return Result.error("插入数据库错误");
            }
            else{
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }
    //修改用户信息
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }
    //删除用户信息
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        User currentUser = TokenUtils.getCurrentUser();
        if(id.equals(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeById(id);
        return Result.success();
    }
    //批量删除用户信息
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids){
        User currentUser = TokenUtils.getCurrentUser();
        if(currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())){
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }
    //查询用户信息
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<User> users = userService.list(new QueryWrapper<User>().orderByDesc("id")); //select * from user order by id desc
        return Result.success(users);
    }
    //根据用户id查询用户信息
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        User user = userService.getById(id);
        return Result.success(user);
    }
//    //根据name查询用户信息
//    @GetMapping("/selectByName/{name}")
//    public Result selectByName(@PathVariable String name){
//        List<User> user = userService.selectByName(name);
//        return Result.success(user);
//    }
//    //根据username和name查询用户信息
//    @GetMapping("/selectByMore")
//    public Result selectByMore(@RequestParam String username, @RequestParam String name){
//        User user = userService.selectByMore(username, name);
//        return Result.success(user);
//    }
//    //根据username和name模糊查询
//    @GetMapping("/selectByFuzzy")
//    public Result selectByFuzzy(@RequestParam String username, @RequestParam String name){
//        List<User> users = userService.selectByFuzzy(username, name);
//        return Result.success(users);
//    }
    //多页模糊查询用户信息
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum, //pageNum表示当前页码
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        //select * from user where username like '%#{username}%' and name like '%#{name}%'
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    //批量导出
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String ids,
                           HttpServletResponse response) throws IOException{
        ExcelWriter writer = ExcelUtil.getWriter(true);

        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(ids)){
            List<Integer> idsArr1 = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr1);
        }
        else{         //全部或查询导出
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }
        list = userService.list(queryWrapper); //查询当前user表的数据

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "utf-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    //批量导入
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException{
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        //写入数据库
        try {
            userService.saveBatch(userList);
        }   catch (Exception e){
            e.printStackTrace();
            return Result.error("数据批量导入出错");
        }

        return Result.success();
    }

}

