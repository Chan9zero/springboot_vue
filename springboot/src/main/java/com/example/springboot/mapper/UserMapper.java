package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

//import java.util.List;

//@Mapper  升级成Mybatis Plus
public interface UserMapper extends BaseMapper<User> {
//    @Insert("insert into `user` (username, password, name, phone, email, address, avatar, role)" +
//            "values (#{username}, #{password}, #{name}, #{phone}, #{email}, #{address}, #{avatar}, #{role})")
//    void insert(User user);
//
//    @Update("update `user` set username = #{username}, password = #{password}, name= #{name}," +
//            "email = #{email}, address = #{address}, avatar = #{avatar} where id = #{id}")
//    void updateUser(User user);
//
//    @Delete("delete from `user` where id = #{id}")
//    void deleteUser(Integer id);
//
//    @Select("select * from `user` order by id desc")
//    List<User> selectAll();
//
//    @Select("select * from `user` where id = #{id}")
//    User selectById(Integer id);
//
//    @Select("select * from `user` where name = #{name} order by id desc")
//    List<User> selectByName(String name);
//
//    @Select("select * from `user` where username = #{username} and name = #{name}")
//    User selectByMore(@Param("username") String username, @Param("name") String name);
//
//    @Select("select * from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%')")
//    List<User> selectByFuzzy(String username, String name);
//
//    @Select("select * from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc limit #{skipNum}, #{pageSize}")
//    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize, @Param("username") String username, @Param("name") String name);
//
//    @Select("select count(id) from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%')")
//    Integer selectCountByPage(@Param("username") String username, @Param("name") String name);
//
//    @Select("select * from `user` where username = #{username} order by id desc")
//    User selectByUsername(String username);

    //Mybatis Plus



}
