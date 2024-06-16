<template>
  <el-container class="home-container">
    <el-header><!-- 头部区 -->
      <div>
        <img src="../assets/image/logo.png" style="height: 50px;margin-left:10px;border-radius: 50%"/>
        <span style="font-family: 隶书;font-weight: bold;letter-spacing: 3px">阿Z书屋</span>
      </div>
      <el-dropdown trigger="click">
        <div class="avatar-wrapper">
          <el-avatar shape="square" :size="50"
              :src="user.touxiag_url"
          />
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/personCenter">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

<!--      <el-button type="info" @click="logout">退出</el-button>-->
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <!-- 折叠条 -->
        <div class="toggle-button" @click="toggleCollapse()">|||</div>
        <el-menu
            router
            :default-active="this.$route.path"
            class="el-menu-vertical-demo"
            background-color="#333744"
            text-color="#fff"
            active-text-color="#409BFF"
            :unique-opened="true"
            :collapse-transition="false"
            :collapse="isCollapse">
          <!--使用v-if语句进行判断有无children菜单，确定一级菜单是否需要下拉功能-->
          <template v-for="item in arrType">
          <el-sub-menu
              text-color="#fff"
              :index="1"
              :key="item.key"
              v-if="subtitleIsExist(item)">
            <!--一级菜单在submenu中为下拉状态-->
            <template #title>
              <component :is="item.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
              <span>{{item.title}}</span>
            </template>
            <!--二级菜单再次进行v-if判断是否显示二级菜单-->
            <template v-for="item2 in item.children" :key="item2.key">
              <el-menu-item
                :index="item2.path"

                >
                <component :is="item2.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
                <span>{{item2.title}}</span>
              </el-menu-item>
            </template>
          </el-sub-menu>

          <!--一级菜单使用v-else在不在submenu中为非下拉状态-->
          <el-menu-item :index="item.path" v-else>
            <component :is="item.icon" style="width: 20px;height: 20px;margin-right: 5px"></component>
            <template #title>
              <span>{{item.title}}</span>
            </template>
          </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <!-- 右侧内容主体 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import axios_request from "@/utils/axios_request";

export default {
  data(){
    return{
      // 是否折叠显示菜单栏
      isCollapse: false,
      // 侧边栏数据
      arrType: [
        {
          key: 'menu-1',
          title: '读者管理',
          path: '/reader',
          icon: 'Avatar',
        },
        {
          key: 'menu-2',
          title: '图书管理',
          path: '/book',
          icon: 'Notebook',
        },
        {
          key: 'menu-3',
          title: '借阅管理',
          path: '/borrow',
          icon: 'Operation'
        },
        {
          key: 'menu-4',
          title: '基础信息',
          path: '/reader2',
          icon: 'Menu',
          children: [
            {
              key: 'menu-4-1',
              title: '图书种类',
              path: '/bookType',
              icon: 'FolderOpened',
            },
            {
              key: 'menu-4-2',
              title: '读者种类',
              path: '/readerType',
              icon: 'UserFilled',
            }
          ]
        },
        {
          key: 'menu-5',
          title: '用户管理',
          path: '/user',
          icon: 'User',
        },
      ],
      //当前用户
      user:{},
    }
  },
  created() {
    // this.getMenuList();
    this.getCurrentUesr();
  },
  methods:{

    //登出
    logout(){
      window.sessionStorage.clear();
      this.$router.push('/');
    },

    // 判断菜单是否有子菜单
    subtitleIsExist (item) {
      if (item.children) {
        return true
      } else {
        return false
      }
    },
    //点击按钮，切换菜单的折叠与展开
    toggleCollapse(){
      this.isCollapse = !this.isCollapse;
    },
    // 获取所有的菜单
    // getMenuList(){
    //   axios_request({
    //     url:'/menu/findAllMenu',
    //     method:'get'
    //   }).then(res=>{
    //     if(res.data.code==200){
    //       this.menuList=res.data.menuList
    //     }else {
    //       return this.$message.error(res.statusText);
    //     }
    //   })
    // }

    // 获取正在登录用户
    getCurrentUesr(){
      axios_request({
        url:'/user/currentUser',
        method:'get'
      }).then(res=>{
        if(res.code==200){
          this.user=res.data.currentUser
        }else {
          return this.$message.error(res.statusText);
        }
      })
    }
  },
};
</script>

<style lang="less" scoped>
.home-container{
  height: 100%;
}
.el-header{
  background-color: #373d41;
  display: flex; //设置显示为flex布局
  justify-content: space-between;//设置为flex左右布局
  padding-left: 0;//左内边距为0（Logo贴左边）
  align-items: center;//元素上下居中（防止右边按钮贴上下边）
  color: #fff;
  font-size: 20px;
  > div {//内嵌的div样式
    display: flex;
    align-items: center;//Logo和文字上下居中
    span {
      margin-left: 15px;//文字左侧设置间距，防止与Logo紧贴
    }
  }
}
.el-aside{
  background-color: #333744;
}
.el-menu-vertical-demo{
  border:0;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.el-menu-item{
  letter-spacing: 3px;
}
.el-sub-menu{
  letter-spacing: 3px;
  display: grid;
}
.el-main{
  background-color: #eaedf1;
}
.toggle-button{
  background-color: #4A5064;//背景色(浅灰)
  font-size: 10px;//字体大小10像素
  line-height: 24px;//行高24像素
  color: #fff;//字体颜色白色
  text-align: center;//字体居中
  letter-spacing: 0.2em;//字体之间的距离
  cursor: pointer;//鼠标的形状（手形）
}
.avatar-wrapper{

}
</style>