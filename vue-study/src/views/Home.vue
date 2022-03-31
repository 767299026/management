<template>
  <div id="home">
    <el-container class="home_container">
      <!--头部区域-->
      <el-header height="80px">
        <div class="head_photo">
          <img src="../assets/logo.png" alt="header.jpg" height="100%">
          <span class="head_font">电商后台管理系统</span>
        </div>
        <!--头像区域-->
        <el-dropdown>
          <el-avatar :src="UserInfo.avatar" shape="circle" fit="contain"></el-avatar>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="ali-iconfont icon-logout" @click="">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <el-container>
        <!--侧边栏区域-->
        <el-aside width="200px" :width="isCollapsed ? '64px' : '200px'" >
          <div class="toggle-button" @click="changeCollapsed">|||</div>
          <el-menu  background-color="#f0fff0" text-color="black" active-text-color="#399bdd"
                    unique-opened :collapse="isCollapsed" :collapse-transition="false" router
                    :default-active="activePath">
            <!--一级菜单-->
            <el-submenu :index="item.id+''" v-for="item in menuList" :key="item.id">
              <!--一级菜单模板-->
              <template slot="title">
                <!--图标-->
                <i :class="iconsObj[item.id]"></i>
                <!--文本-->
                <span>   {{ item.authName }}</span>
              </template>
              <!--二级菜单-->
              <el-menu-item :index="'/' + subItem.paths" v-for="subItem in item.children"
                            :key="subItem.id" @click="saveNavState('/'+ subItem.paths)">
                <template slot="title">
                  <!--图标-->
                  <i class="el-icon-menu"></i>
                  <!--文本-->
                  <span>{{ subItem.authName }}</span>
                </template>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <!--主体区域-->
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "Home",
  data() {
    return {
      menuList: [],
      iconsObj: {
        '125': 'iconfont icon-user',
        '103': 'iconfont icon-tijikongjian',
        '101': 'iconfont icon-shangpin',
        '102': 'iconfont icon-danju',
        '145': 'iconfont icon-baobiao'
      },
      isCollapsed: false,
      activePath: '',
      UserInfo: {},
    }
  },
  created() {
    this.getMenuList()
    this.activePath = window.sessionStorage.getItem("activePath")
    this.UserInfo = JSON.parse(window.localStorage.getItem("userInfo"))
  },
  methods: {
    getMenuList() {
      this.$axios.getMenuList().then((res) =>{
        this.menuList = res.data.data
        Message.success(res.data.msg)
      })
    },
    changeCollapsed(){
      this.isCollapsed = !this.isCollapsed
    },
    saveNavState(activePath){
       window.sessionStorage.setItem("activePath",activePath)
      this.activePath = activePath
    },
  }
}
</script>

<style scoped>
#home {
  height: 100%;
  width: 100%;
}
.el-header {
  background-color: #399bdd;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  color: white;
  font-size: 20px;
}
.el-aside {
  background-color: #f0fff0;
}
.el-main {
  background-color: white;
}
.el-avatar {
  height: 60px;
  width: 60px;
  cursor: pointer;
}
.el-dropdown {
  display: flex;
  align-items: center;
}
.home_container {
  height: 100%;
}
.head_photo {
  display: flex;
  align-items: center;
}
.head_font {
  margin-left: 10px;
}
.toggle-button {
  background-color: #f0fff0;
  font-size: 10px;
  line-height: 24px;
  color: black;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>