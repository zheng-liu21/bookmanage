<template>
  <div class="menu">
    <div>
        <el-menu
            :router="true"
            active-text-color="#ffd04b"
            background-color="#545c64"
            class="el-menu-vertical-demo"
            default-active="2"
            text-color="#fff"
            style="border: 0"
        >
          <div class="logo">
            阿Z书屋
          </div>
          <el-menu-item index="/userindex">
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/employrindex">
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/departrindex">
            <span>图书管理</span>
          </el-menu-item>
          <el-menu-item index="/postindex">
            <span>系统管理</span>
          </el-menu-item>
        </el-menu>
    </div>
  </div>
  <div class="top">
    <div class="person">
      admin
    </div>
    <div class="touxiang">
      <image-preview :src="img" :width="50" :height="50"/>
      <!--      <el-avatar :src="img"/>-->
    </div>
  </div>
  <div class="body">
    <router-view></router-view>
  </div>
</template>

<script >

export default {
  name: "index",
  data() {
    return {
      img: null,
      userName: null,
      imgUrl: "../assets/logo.png"
    }
  }
}
</script>
<style scoped>

.top{
  float: left;
  width: 90%;
  height: 50px;
  background-color: black;
}
.body{
  float: right;
  /*background-color:#42b983;*/
  width: 1810px;
  height: 100%;
}
.menu{
  width: 10%;
  float: left;
  height: 100%;
  background-color: #545c64;
}
.logo{
  margin-top: 10px;
  height: 50px;
  text-align: center;
  font-size: 20px;
  font-family: "Yu Gothic UI Semilight";
  color: white;
}
.touxiang{
  margin-top: 5px;
  margin-right: 10px;
  float: right;
}
.person{
  margin-top: 10px;
  margin-right: 10px;
  float: right;
}
</style>
