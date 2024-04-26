<template>
  <div>
  <el-container>
    <!--侧边栏-->
    <el-aside style="min-height: 100vh; background-color: #001529"
    :width="asideWidth">
      <div style="height: 100px; color: white;
      display: flex; align-items: center; justify-content: center">
      <img src="@/assets/logo1.png" alt="" style="width: 40px; height: 40px">
          <span class="logo-title" v-show="!isCollapse">江南渔业</span>
      </div>
      <el-menu router style="border: none" background-color="#001529"
               text-color="rgba(255,255,255,0.85)" active-text-color="#fff"
               :default-active="$route.path" :collapse="isCollapse" :collapse-transition="false">
        <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
        </el-menu-item>
        <el-submenu index="info" v-if="user.role === '管理员'">
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>信息管理</span>
          </template>
          <el-menu-item index="/user">用户信息</el-menu-item>
<!--          <el-menu-item>教师信息</el-menu-item>-->
        </el-submenu>
      </el-menu>
    </el-aside>


    <el-container>
      <!--头部区域-->
      <el-header style="height: 100px">
          <i :class="collapseIcon" style="font-size: 30px" @click="handleCollapse"></i>
        <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{$route.meta.name}}</el-breadcrumb-item>
        </el-breadcrumb>

        <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: flex-end">
          <el-dropdown placement="bottom">
            <div style="display: flex; align-items: center; cursor: default">
              <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; border-radius: 50%; height: 40px">
              <span>{{user.name}}</span>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </el-header>
      <!--主体区域-->

      <el-main>
        <router-view @update:user="updateUser" />
      </el-main>


    </el-container>
  </el-container>
  </div>
</template>

<script>

import axios from "axios";
import request from "@/utils/request";
export default {
  name: 'Manager',
  data(){
    return{
      isCollapse: false, //不收缩
      asideWidth: '200px',
      collapseIcon: 'el-icon-s-fold',
      // users: [],
      //fileList: [],
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      // url:'',
      // urls: []
    }
  },
  mounted() {
    if(!this.user.id){ //页面加载完后触发
      this.$router.push('/login') //当前页面没有用户信息 跳转登录界面
    }
    // axios.get('http://localhost:9090/user/selectAll').then(res => {
    //   console.log(res.data) //后台返回的数据
    //   this.users = res.data.data
    // })
    // request.get('user/selectAll').then(res => {
    //   this.users = res.data
    // })
  },
  methods: {
    // preview(url){
    //   window.open(url)
    // },
    // showUrls(){
    //   console.log(this.urls)
    // },
    // handleMultipleFileUpload(response, file, fileList) {
    //   //console.log(response, file, fileList)
    //   this.urls = fileList.map(v => v.response?.data)
    // },
    // handleTableFileUpload(res, file, fileList, row){
    //   row.avatar = res.data
    //   console.log(res, file, fileList, row)
    //   //触发更新就可以了
    //   request.put('/user/update', row).then(res => {
    //     if(res.code === '200'){
    //       this.$message.success('上传成功')
    //     } else{
    //       this.$message.error(res.msg)
    //     }
    //   })
    // },
    // handleFileUpload(response, file, fileList){
    //   this.fileList = fileList
    // },
    updateUser(user){ //获取子组件上传来的信息
      this.user = JSON.parse(JSON.stringify(user)) //让父级对象与子级对象没有关系
    },
    logout(){
      localStorage.removeItem('honey-user')  //清除当前用户的数据
      this.$router.push('/login')
    },
    handleCollapse(){
      this.isCollapse = !this.isCollapse,
          this.asideWidth = this.isCollapse ? '64px' : '200px',
          this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    }
  }

}
</script>


<style>
.el-menu--inline{
  background-color: #000c17 !important;
}
.el-menu--inline .el-menu-item{
  background-color: #000c17 !important;
  padding-left: 49px;
}
.el-menu-item:hover, .el-submenu__title:hover{
  color: #fff !important;
}
.el-menu-item.is-active{
  background-color: #40a9ff !important;
  border-radius: 10px;
}
.el-menu-item{
  height: 40px;
  line-height: 40px;
}
.el-submenu__title{
  height: 40px;
  line-height: 40px;
}
.el-aside{
  transition: width.3s;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
}
.logo-title{
  margin-left: 5px;
  font-size: 20px;
  transition: all .3s;   /* 0.3s*/
}
.el-header{
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  align-items: center;
}
</style>