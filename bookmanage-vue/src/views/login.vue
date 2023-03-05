<template>
  <div id="login">
    <div id="bgd">
      <canvas
          id='myCanvas'
          :width='width'
          :height='height'
      >
      </canvas>
    </div>
    <div id="sysname1">阿z</div>
    <div id="sysname2">书屋</div>
    <div id="loginBox">
<!--      <h4 style="margin-left: 40px">登录</h4>-->
      <el-form
          :model="loginForm"
          :rules="loginRules"
          ref="loginForm"
          label-width="0px"
      >
        <el-form-item
            label=""
            prop="username"
            style="margin-top:60px;margin-left: 40px"
        >
          <el-row>
            <el-col :span='30'>
              <el-input
                  name="username"
                  type="text"
                  class="inps"
                  placeholder='用户名'
                  v-model="loginForm.username"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item
            label=""
            prop="password"
            style="margin-left: 40px;margin-top: 30px;"
        >
          <el-row>
            <el-col :span='30'>
              <el-input
                  name="password"
                  type="password"
                  class="inps"
                  placeholder='密码'
                  v-model="loginForm.password"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item style="margin-top:50px;margin-left: 50px;">
          <el-button
              type="primary"
              round
              class="submitBtn"
              @click="submitForm"
          >登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios_request from "@/utils/axios_request";
export default {
  name:"login",
  data() {
    return {
      canvas: null,
      context: null,
      stars: [], //星星数组
      shadowColorList: [
        "#39f",
        "#ec5707",
        "#b031d4",
        "#22e6c7",
        "#92d819",
        "#14d7f1",
        "#e23c66"
      ], //阴影颜色列表
      directionList: ["leftTop", "leftBottom", "rightTop", "rightBottom"], //星星运行方向
      speed: 10, //星星运行速度
      last_star_created_time: new Date(), //上次重绘星星时间
      Ball: class Ball {
        constructor(radius) {
          this.x = 0;
          this.y = 0;
          this.radius = radius;
          this.color = "";
          this.shadowColor = "";
          this.direction = "";
        }

        draw(context) {
          context.save();
          context.translate(this.x, this.y);
          context.lineWidth = this.lineWidth;
          var my_gradient = context.createLinearGradient(0, 0, 0, 8);
          my_gradient.addColorStop(0, this.color);
          my_gradient.addColorStop(1, this.shadowColor);
          context.fillStyle = my_gradient;
          context.beginPath();
          context.arc(0, 0, this.radius, 0, Math.PI * 2, true);
          context.closePath();

          context.shadowColor = this.shadowColor;
          context.shadowOffsetX = 0;
          context.shadowOffsetY = 0;
          context.shadowBlur = 10;

          context.fill();
          context.restore();
        }
      }, //工厂模式定义Ball类
      width: window.innerWidth,
      height: window.innerHeight,
      loginForm: {
        username: "",
        password: ""
      },
      loginRules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        passWord: [{ required: true, message: "请输入密码", trigger: "blur" }]
      }
    };
  },
  methods: {
    //提交登录
    submitForm() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // 验证通过 提交数据给后台 存token 路由跳转
          // token 单纯存在sessionStorage、localStorage、cookie是可行的  若存在vuex中 刷新页面就没了
          axios_request({
            url:'/login/login',
            method:'post',
            params:this.loginForm
          }).then(res => {
                console.log(res)
                // 存token setItem(key,value)
                if (res.data.code === 200) {
                  this.$store.commit('setToken',res.data.token)
                  // console.log(this.$store.getters.getToken())
                  this.$router.push("/index");
                } else {
                  this.$notify.error({
                    message: res.statusText,
                  });
                }
              })
              .catch((err) => {
                this.$notify.error({
                  title: "错误",
                  message: "服务器内部错误",
                });
              });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    //重复动画
    drawFrame() {
      let animation = requestAnimationFrame(this.drawFrame);
      this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
      this.createStar(false);
      this.stars.forEach(this.moveStar);
    },
    //展示所有的星星
    createStar(params) {
      let now = new Date();
      if (params) {
        //初始化星星
        for (var i = 0; i < 50; i++) {
          const radius = Math.random() * 3 + 2;
          let star = new this.Ball(radius);
          star.x = Math.random() * this.canvas.width + 1;
          star.y = Math.random() * this.canvas.height + 1;
          star.color = "#ffffff";
          star.shadowColor = this.shadowColorList[
              Math.floor(Math.random() * this.shadowColorList.length)
              ];
          star.direction = this.directionList[
              Math.floor(Math.random() * this.directionList.length)
              ];
          this.stars.push(star);
        }
      } else if (!params && now - this.last_star_created_time > 3000) {
        //每隔3秒重绘修改颜色其中30个球阴影颜色
        for (var i = 0; i < 30; i++) {
          this.stars[i].shadowColor = this.shadowColorList[
              Math.floor(Math.random() * this.shadowColorList.length)
              ];
        }
        this.last_star_created_time = now;
      }
    },
    //移动
    moveStar(star, index) {
      if (star.y - this.canvas.height > 0) {
        //触底
        if (Math.floor(Math.random() * 2) === 1) {
          star.direction = "leftTop";
        } else {
          star.direction = "rightTop";
        }
      } else if (star.y < 2) {
        //触顶
        if (Math.floor(Math.random() * 2) === 1) {
          star.direction = "rightBottom";
        } else {
          star.direction = "leftBottom";
        }
      } else if (star.x < 2) {
        //左边
        if (Math.floor(Math.random() * 2) === 1) {
          star.direction = "rightTop";
        } else {
          star.direction = "rightBottom";
        }
      } else if (star.x - this.canvas.width > 0) {
        //右边
        if (Math.floor(Math.random() * 2) === 1) {
          star.direction = "leftBottom";
        } else {
          star.direction = "leftTop";
        }
      }
      if (star.direction === "leftTop") {
        star.y -= star.radius / this.speed;
        star.x -= star.radius / this.speed;
      } else if (star.direction === "rightBottom") {
        star.y += star.radius / this.speed;
        star.x += star.radius / this.speed;
      } else if (star.direction === "leftBottom") {
        star.y += star.radius / this.speed;
        star.x -= star.radius / this.speed;
      } else if (star.direction === "rightTop") {
        star.y -= star.radius / this.speed;
        star.x += star.radius / this.speed;
      }
      star.draw(this.context);
    }
  },
  mounted() {
    this.canvas = document.getElementById("myCanvas");
    this.context = this.canvas.getContext("2d");

    this.createStar(true);
    this.drawFrame();
  }
};
</script>

<style lang='less' scoped>
#login {
  width: 100vw;
  padding: 0;
  margin: 0;
  height: 100vh;
  font-size: 16px;
  background-repeat: no-repeat;
  //background-position: left top;
  background-color: #242645;
  //color: #fff;
  font-family: "Source Sans Pro";
  background-size: 100%;
  background-image: url("../assets/image/星空背景.png");
  position: relative;
  #bgd {
    height: 100vh;
    width: 100vw;
  }
  #sysname1{
    width: 800px;
    height: 300px;
    font-size: 100px;
    font-family: 隶书;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin-top: 300px;
    margin-left: 500px;
    opacity: 1;
    color: white;
    letter-spacing: 40px;
  }
  #sysname2{
    width: 800px;
    height: 300px;
    font-size: 100px;
    font-family: 隶书;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin-top: 400px;
    margin-left: 700px;
    opacity: 1;
    color: white;
    letter-spacing: 40px;
  }
  #loginBox {
    width: 300px;
    height: 350px;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin-left: 1400px;
    margin-top: 250px;
    padding: 50px 40px 40px 40px;
    box-shadow: -15px 15px 15px rgba(6, 17, 47, 0.7);
    opacity: 1;
    background: linear-gradient(
        230deg,
        rgba(53, 57, 74, 0) 0%,
        rgb(0, 0, 0) 100%
    );
    /deep/ .inps input {
      height: 37px;
      background-color: transparent;
      font-family: 隶书;
      font-size: 16px;
      color: white;
    }
    /deep/.el-input__wrapper {
      background-color: transparent !important;
      border: 0;
    }

    /deep/.el-form-item__error{
      font-family: 隶书;
      padding-top: 3px;
    }

    .submitBtn {
      //background-color: transparent;
      border-radius: 18px;
      border: 0;
      color: #fff;
      text-align: center;
      line-height: 30px;
      width: 200px;
      height: 40px;
      font-size: 16px;
      font-family: 隶书;
      background-image: linear-gradient(to right, #30cfd0, #330867);

    }
  }
}
</style>