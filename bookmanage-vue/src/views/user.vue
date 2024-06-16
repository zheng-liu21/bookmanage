<template>
  <div>
    <!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    卡片视图-->
    <el-card>
      <el-row :gutter="20"><!-- gutter是指定每个分栏的间隔 -->
        <!-- 分栏一共占24格，给搜索框7格，添加按钮4格 -->
        <el-col :span="7">
          <!-- 搜索与添加区域 -->
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getUserLsit">
            <template #append>
              <el-button icon="Search" @click="getUserLsit"></el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addUserVisible = true">添加用户</el-button>
        </el-col>
      </el-row>

      <el-table :data="userList" max-height="700" stripe
                :default-sort="{ prop: 'user_name', order: 'descending' }">
        <el-table-column label="头像" prop="touxiag_url" >
          <template #default="scope">
            <el-image
                style="width: 100px; height: 100px"
                :preview-src-list="[scope.row.touxiag_url]"
                preview-teleported="true"
                :src="scope.row.touxiag_url"
                :zoom-rate="1.2"
                fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="user_name" sortable/>
        <el-table-column label="手机" prop="phone"  />
        <el-table-column label="身份证" prop="id_number" />
        <el-table-column label="状态" prop="state_bool">
          <template #default="scope">
            <el-switch v-model="scope.row.state_bool"
                       active-color="#13ce66"
                       inactive-color="#ff4949"
                       @change="userStateChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column  label="操作" >
          <template #default="scope">
            <el-tooltip content="修改" effect="dark">
              <el-button  type="primary" icon="Edit" size="small" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" effect="dark">
              <el-button  type="danger" icon="Delete" size="small" @click="deleteUser(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip content="重置密码" effect="dark">
              <el-button  type="warning" icon="Setting" size="small" @click="restUserPs(scope.row.id)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!--    分页-->
      <el-pagination
          v-model:current-page="queryInfo.pagenum"
          v-model:page-size="queryInfo.pagesize"
          :page-sizes="[5, 10, 15, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </el-card>
  </div>

  <!--  添加用户对话框-->
  <el-dialog
      v-model="addUserVisible"
      title="添加用户"
      width="30%"
      :before-close="handleClose"
      @close="addDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="addUserForm" :rules="addUserFormRules" ref="addUserFormRef" label-width="100px">
      <el-form-item label="头像" prop="touxiag_url">

        <el-input v-model="addUserForm.touxiag_url" v-if="false"></el-input>
        <el-upload
            ref="uploadAdd"
            class="avatar-uploader"
            action="/api/upload/uploadImage"
            list-type="picture-card"
            :limit="1"
            :show-file-list="true"
            :on-preview="handlePictureCardPreview"
            :on-success="handleAvatarAddSuccess"
            :before-upload="beforeAvatarUpload"
            :data="addUserForm"
        >
          <el-icon><Plus /></el-icon>

          <template #tip>
            <div class="el-upload__tip">只能上传图片,且单张图片大小不能超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="用户名" prop="user_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="addUserForm.user_name"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="addUserForm.password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="身份证" prop="id_number">
        <el-input v-model="addUserForm.id_number"></el-input>
      </el-form-item>
      <el-form-item label="手机" prop="phone">
        <el-input v-model="addUserForm.phone"></el-input>
      </el-form-item>
    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addUserVisible = false">取消</el-button>
        <el-button type="primary" @click="addUser()">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!--  修改用户对话框-->
  <el-dialog
      v-model="editUserVisible"
      title="修改用户"
      width="30%"
      :before-close="handleClose"
      @close="editDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="editUserForm" :rules="editUserFormRules" ref="editUserFormRef" label-width="100px">
      <el-form-item label="用户名" prop="user_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="editUserForm.user_name" disabled></el-input>
      </el-form-item>
      <el-form-item label="头像" prop="touxiag_url">
        <el-input v-model="editUserForm.touxiag_url" v-if="false"></el-input>
        <el-upload
            ref="uploadEdit"
            class="avatar-uploader"
            action="/api/upload/uploadImage"
            list-type="picture-card"
            :limit="1"
            :on-preview="handlePictureCardPreview"
            :on-success="handleAvatarEditSuccess"
            :before-upload="beforeAvatarUpload"
            :data="editUserForm"
        >
          <img style="width:148px" v-if="editUserForm.touxiag_url && editUserForm.touxiag_url.indexOf('8083')!==-1" :src="editUserForm.touxiag_url" class="avatar" />
          <el-icon v-else ><Plus /></el-icon>

          <template #tip>
            <div class="el-upload__tip">只能上传图片,且单张图片大小不能超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="身份证" prop="id_number">
        <el-input v-model="editUserForm.id_number"></el-input>
      </el-form-item>
      <el-form-item label="手机" prop="phone">
        <el-input v-model="editUserForm.phone"></el-input>
      </el-form-item>
    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editUserVisible = false">取消</el-button>
        <el-button type="primary" @click="editUserSubmit()">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!--  放大图片-->
  <el-dialog style="width: 35%" v-model="dialogVisible" title="预览">
    <img style="width: 100%;" :src="dialogImageUrl" alt="预览图象" />
  </el-dialog>
</template>

<script>
import axios_request from "@/utils/axios_request";
import ElMessage from "element-plus/dist/index.full.mjs";

export default {
  data(){
    var checkPhone = (rule,value,callback) =>{
      //验证手机号的正则表达式
      const regMobile = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
      if(regMobile.test(value)){
        return callback(); //合法手机号
      }
      return callback(new Error("请输入合法的手机号"));
    }
    return{
      queryInfo:{
        query:"",
        pagenum:1,
        pagesize:5,
      },
      dialogImageUrl:"",//放大图片路径
      dialogVisible:false,//放大图片
      userList:[],
      total:0,
      editUserVisible:false,//控制修改用户对话框
      addUserVisible:false,//控制添加用户对话框
      //添加用户的表单数据
      addUserForm: {},
      //编辑用户的表单数据
      editUserForm:{},
      //添加用户表单的验证规则对象
      addUserFormRules: {
        user_name: [{required:true,message:'请输入用户名',trigger:'blur'},
          {min:3,max:10,message:'用户名长度在3~10个字符',trigger:'blur'}],
        password: [{required:true,message:'请输入密码',trigger:'blur'},
          {min:6,max:15,message:'密码长度在6~15个字符',trigger:'blur'}],
        id_number: [{required:true,message:'请输入身份证号',trigger:'blur'}],
        phone: [{required:true,message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      },
      //修改用户表单的验证规则对象
      editUserFormRules: {
        id_number: [{message:'请输入身份证号',trigger:'blur'}],
        phone: [{message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      }
    }
  },
  created() {
    this.getUserLsit();
  },
  methods: {
    getUserLsit() {
      axios_request({
        url: '/user/findAllUser',
        method: 'get',
        params: this.queryInfo
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("获取用户列表失败！")
        }
        console.log(res);
        this.userList = res.data.userList;
        this.total = res.data.total;
      })
    },

    //监听 pagesize 改变的事件
    handleSizeChange(newSize) {
      console.log(newSize);
      this.queryInfo.pagesize = newSize;//重新指定每页数据量
      this.getUserLsit();//带着新的分页请求获取数据

    },

    //监听 页码值 改变的事件
    handleCurrentChange(newPage) {
      console.log(newPage);
      this.queryInfo.pagenum = newPage;//重新指定当前页
      this.getUserLsit();//带着新的分页请求获取数据
    },

    //监听添加用户对话框的关闭状态
    addDialogClosed() {
      this.$refs.addUserFormRef.resetFields();
      this.$refs.uploadAdd.clearFiles();
    },

    //监听switch开关状态的改变
    userStateChange(userinfo) {
      axios_request({
        url: '/user/updateUser',
        method: 'post',
        params: userinfo
      }).then(res => {
        if (res.code !== 200) {
          userinfo.state_bool = !userinfo.state_bool;
          this.$message.error("用户状态更改失败！")
        }
        console.log(res);
        this.$message.success("用户状态更改成功！")
      })
    },

    //添加用户
    addUser() {
      this.$refs.addUserFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/user/addUser',
            method: 'post',
            params: this.addUserForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("添加用户失败！")
            }
            this.$message.success('添加用户成功！');
            // 隐藏添加用户的对话框
            this.addUserVisible = false;
            //重新获取用户列表数据
            this.getUserLsit();
          })
        }
      })

    },

    //删除用户
    deleteUser(id) {
      //弹框询问用户是否删除数据
      this.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/user/deleteUser',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("删除用户失败！")
          }
          this.$message.success('删除用户成功！');
          //重新获取用户列表数据
          this.getUserLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消删除'});
      });
    },

    //关闭编辑用户的对话框
    editDialogClosed(){
      this.$refs.editUserFormRef.resetFields();
      this.$refs.uploadEdit.clearFiles();
    },
    //展示编辑用户的对话框
    showEditDialog(id){
      axios_request({
        url: '/user/editUser',
        method: 'get',
        params: {
          id:id
        },
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("查询用户失败！")
        }
        this.editUserForm=res.data.user;
        this.editUserVisible = true;
      })
    },
    //修改用户确认
    editUserSubmit(){
      this.$refs.editUserFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/user/updateUser',
            method: 'post',
            params: this.editUserForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("修改用户失败！")
            }
            this.$message.success('修改用户成功！');
            // 隐藏修改用户的对话框
            this.editUserVisible = false;
            //重新获取用户列表数据
            this.getUserLsit();
          })
        }
      })
    },
    //重置密码
    restUserPs(id){
      //弹框询问用户是否删除数据
      this.$confirm('此操作将重置用户密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/user/rsetUserPs',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("重置用户密码失败！")
          }
          this.$message.success('重置用户密码成功！');
          //重新获取用户列表数据
          this.getUserLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消重置用户密码'});
      });
    },

//预览图象
    handlePictureCardPreview(file){
      this.dialogImageUrl=file.url;
      this.dialogVisible=true;
    },

    beforeAvatarUpload(rawFile){
      if(rawFile.size / 1024 / 1024 > 10){
        ElMessage.error("单张图片大小不能超过10MB!");
        return false;
      }
      return true;
    },

    // 添加用户上传成功，获取返回的图片地址
    handleAvatarAddSuccess(res){
      console.log("res="+res);
      this.addUserForm.touxiag_url=res.data.fileName;

    },

    // 修改用户上传成功，获取返回的图片地址
    handleAvatarEditSuccess(res){
      console.log("res="+res);
      this.editUserForm.touxiag_url=res.data.fileName;

    },
  },
}
</script>

<style lang='less'  scoped>
.el-breadcrumb{
  margin-bottom: 15px;
  font-size: 14px;
}

.el-card {
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15) !important;
}
.el-table{
  margin-top: 15px;
  font-size: 14px;
}




.el-pagination{
  margin-top:15px;
  margin-bottom:15px;
  float:right;
}
</style>