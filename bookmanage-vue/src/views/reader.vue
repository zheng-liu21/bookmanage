<template>
  <div>
<!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>读者管理</el-breadcrumb-item>
      <el-breadcrumb-item>读者列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    卡片视图-->
    <el-card>
      <el-row :gutter="20"><!-- gutter是指定每个分栏的间隔 -->
        <!-- 分栏一共占24格，给搜索框7格，添加按钮4格 -->
        <el-col :span="7">
          <!-- 搜索与添加区域 -->
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getReaderLsit">
            <template #append>
            <el-button icon="Search" @click="getReaderLsit"></el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="openaddReaderVisible">添加读者</el-button>
        </el-col>
      </el-row>

      <el-table :data="readerList" max-height="700" stripe>
      <el-table-column label="姓名" prop="reader_name" width="180" sortable/>
      <el-table-column label="借阅数量" prop="borrow_number" width="180" />
      <el-table-column label="类别" prop="type_name" width="180" />
      <el-table-column label="性别" prop="sex" width="180" />
      <el-table-column label="电话" prop="phone" />
      <el-table-column label="院系" prop="faculties" />
      <el-table-column label="注册日期" prop="registration_date" />
      <el-table-column fixed="right" label="操作"  width="200">
        <template #default="scope">
          <el-tooltip content="修改" effect="dark">
            <el-button  type="primary" icon="Edit" size="small" @click="showEditDialog(scope.row.id)"></el-button>
          </el-tooltip>
          <el-tooltip content="删除" effect="dark">
            <el-button  type="danger" icon="Delete" size="small" @click="deleteReader(scope.row.id)"></el-button>
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

  <!--  添加读者对话框-->
  <el-dialog
      v-model="addReaderVisible"
      title="添加读者"
      width="30%"
      :before-close="handleClose"
      @close="addDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="addReaderForm" :rules="addReaderFormRules" ref="addReaderFormRef" label-width="100px">
      <el-form-item label="姓名" prop="reader_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="addReaderForm.reader_name"></el-input>
      </el-form-item>
      <el-form-item label="身份" prop="reader_type">
        <el-select v-model="addReaderForm.reader_type" clearable placeholder="请选择身份">
          <el-option
              v-for="item in readerTypeList"
              :key="item.id"
              :label="item.type_name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="addReaderForm.sex" class="ml-4" >
          <el-radio label="男" size="large">男</el-radio>
          <el-radio label="女" size="large">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model="addReaderForm.phone"></el-input>
      </el-form-item>

      <el-form-item label="院系" prop="faculties">
        <el-input v-model="addReaderForm.faculties"></el-input>
      </el-form-item>
    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addReaderVisible = false">取消</el-button>
        <el-button type="primary" @click="addReader()">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!--  修改用户对话框-->
  <el-dialog
      v-model="editReaderVisible"
      title="修改用户"
      width="30%"
      :before-close="handleClose"
      @close="editDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="editReaderForm" :rules="editReaderFormRules" ref="editReaderFormRef" label-width="100px">
      <el-form-item label="姓名" prop="reader_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="editReaderForm.reader_name" ></el-input>
      </el-form-item>
      <el-form-item label="身份" prop="reader_type">
        <el-select v-model="editReaderForm.reader_type" clearable placeholder="请选择身份">
          <el-option
              v-for="item in readerTypeList"
              :key="item.id"
              :label="item.type_name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="editReaderForm.sex" class="ml-4">
          <el-radio label="男" size="large">男</el-radio>
          <el-radio label="女" size="large">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model="editReaderForm.phone"></el-input>
      </el-form-item>
      <el-form-item label="院系" prop="faculties">
        <el-input v-model="editReaderForm.faculties"></el-input>
      </el-form-item>
      <el-form-item label="注册日期" prop="registration_date">
        <el-date-picker
            v-model="editReaderForm.registration_date"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            :size="size"
        />
      </el-form-item>
    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editReaderVisible = false">取消</el-button>
        <el-button type="primary" @click="editReaderSubmit()">确定</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script>
import axios_request from "@/utils/axios_request";

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
      addReaderVisible:false,//控制添加读者对话框
      editReaderVisible:false,//控制修改读者对话框
      readerList:[],
      readerTypeList:[],
      total:0,
      //添加读者的表单数据
      addReaderForm:{},
      //修改读者的表单数据
      editReaderForm:{},
      //添加读者表单的验证规则对象
      addReaderFormRules: {
        reader_name: [{required:true,message:'请输入用户名',trigger:'blur'},
          {min:3,max:10,message:'用户名长度在3~10个字符',trigger:'blur'}],
        reader_type: [{required:true,message:'请选择身份',trigger:'blur'}],
        sex: [{required:true,trigger:'blur'}],
        faculties: [{required:true,message:'请输入院系',trigger:'blur'}],
        phone: [{required:true,message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      },
      //修改读者表单的验证规则对象
      editReaderFormRules: {
        id_number: [{message:'请输入身份证号',trigger:'blur'}],
        phone: [{message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      }
    }
  },
  created() {
    this.getReaderLsit();
  },
  methods:{
    getReaderLsit(){
      axios_request({
        url:'/reader/findAllreader',
        method:'get',
        params:this.queryInfo
      }).then(res=>{
        if(res.code!==200){
          this.$message.error("获取读者列表失败！")
        }
        console.log(res);
        this.readerList=res.data.readerList;
        this.total=res.data.total;
      })
    },

    //监听添加读者对话框的关闭状态
    addDialogClosed() {
      this.$refs.addReaderFormRef.resetFields();
    },

    //关闭编辑读者的对话框
    editDialogClosed(){
      this.$refs.editReaderFormRef.resetFields();
    },

    //监听 pagesize 改变的事件
    handleSizeChange(newSize){
      console.log(newSize);
      this.queryInfo.pagesize=newSize;//重新指定每页数据量
      this.getReaderLsit();//带着新的分页请求获取数据

    },

    //监听 页码值 改变的事件
    handleCurrentChange(newPage){
      console.log(newPage);
      this.queryInfo.pagenum=newPage;//重新指定当前页
      this.getReaderLsit();//带着新的分页请求获取数据
    },

    //展示编辑读者的对话框
    showEditDialog(id){
      this.getReaderType();
      axios_request({
        url: '/reader/editReader',
        method: 'get',
        params: {
          id:id
        },
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("查询读者失败！")
        }
        this.editReaderForm=res.data.reader;
        this.editReaderVisible = true;
      })
    },

    //删除读者
    deleteReader(id) {
      //弹框询问用户是否删除数据
      this.$confirm('此操作将永久删除读者, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/reader/deleteReader',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("删除读者失败！")
          }
          this.$message.success('删除读者成功！');
          //重新获取用户列表数据
          this.getReaderLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消删除'});
      });
    },

    //打开添加读者对话框
    openaddReaderVisible(){
      this.getReaderType();
      this.addReaderVisible=true;
    },

    //获取读者类型
    getReaderType(){
      axios_request({
        url:'/readerType/findAllreaderType',
        method:'get',
        params:this.queryInfo
      }).then(res=>{
        if(res.code!==200){
          this.$message.error("获取读者类型列表失败！")
        }
        console.log(res);
        this.readerTypeList=res.data.readerTypeList;
        this.total=res.data.total;
      })
    },

    //添加读者
    addReader() {
      this.$refs.addReaderFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/reader/addReader',
            method: 'post',
            params: this.addReaderForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("添加读者失败！")
            }
            this.$message.success('添加读者成功！');
            // 隐藏添加读者的对话框
            this.addReaderVisible = false;
            //重新获取读者列表数据
            this.getReaderLsit();
          })
        }
      })

    },

    //修改读者确认
    editReaderSubmit(){
      this.$refs.editReaderFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/reader/updateReader',
            method: 'post',
            params: this.editReaderForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("修改读者失败！")
            }
            this.$message.success('修改读者成功！');
            // 隐藏修改读者的对话框
            this.editReaderVisible = false;
            //重新获取用户列表数据
            this.getReaderLsit();
          })
        }
      })
    },
  }
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

.el-radio.el-radio--large{
  height:32px;
}


.el-pagination{
  margin-top:15px;
  margin-bottom:15px;
  float:right;
}
</style>