<template>
  <div class="app-container">
    <!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
    </el-breadcrumb>


    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div style="margin-bottom: 20px;"  >
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center" style="text-align: center">
              <el-avatar  :size="100"
                         :src="user.touxiag_url"
              />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <el-icon><UserFilled /></el-icon>用户名称
                <div class="pull-right">{{ user.user_name }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Iphone /></el-icon>手机号码
                <div class="pull-right">{{ user.phone }}</div>
              </li>
              <li class="list-group-item">
                <el-icon><Postcard /></el-icon>身份证号
                <div class="pull-right">{{ user.id_number }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
<!--      <el-col :span="18" :xs="24">-->
<!--        <el-card>-->
<!--          <div slot="header" class="clearfix">-->
<!--            <span>基本资料</span>-->
<!--          </div>-->
<!--&lt;!&ndash;          <el-tabs v-model="activeTab">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-tab-pane label="基本资料" name="userinfo">&ndash;&gt;-->
<!--&lt;!&ndash;              <userInfo :user="user" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-tab-pane>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-tab-pane label="修改密码" name="resetPwd">&ndash;&gt;-->
<!--&lt;!&ndash;              <resetPwd :user="user" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-tab-pane>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-tabs>&ndash;&gt;-->
<!--        </el-card>-->
<!--      </el-col>-->
    </el-row>
  </div>
</template>

<script>
import axios_request from "@/utils/axios_request";

export default {
  name: "index",
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getCurrentUesr();
  },
  methods: {
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
  }
}
</script>

<style lang='less' scoped>

.el-breadcrumb{
  margin-bottom: 15px;
  font-size: 14px;
}
.list-group{
  list-style-type: none;
}
.list-group-item{
  margin-top: 15px;
}
.pull-right{
  margin-top: 5px;
}
</style>