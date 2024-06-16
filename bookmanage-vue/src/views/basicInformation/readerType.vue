<template>
  <div>
    <!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>基础信息</el-breadcrumb-item>
      <el-breadcrumb-item>读者种类</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    卡片视图-->
    <el-card>
      <el-row :gutter="20"><!-- gutter是指定每个分栏的间隔 -->
        <!-- 分栏一共占24格，给搜索框7格，添加按钮4格 -->
        <el-col :span="7">
          <!-- 搜索与添加区域 -->
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getReaderTypeLsit">
            <template #append>
              <el-button icon="Search" @click="getReaderTypeLsit"></el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addReaderTypeVisible = true">添加种类</el-button>
        </el-col>
      </el-row>

      <el-table :data="readerTypeList" max-height="700" stripe
                :default-sort="{ prop: 'id', order: 'descending' }">
        <el-table-column label="序号" prop="id" sortable/>
        <el-table-column label=名称 prop="type_name"  />
        <el-table-column  label="操作" >
          <template #default="scope">
            <el-tooltip content="修改" effect="dark">
              <el-button  type="primary" icon="Edit" size="small" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" effect="dark">
              <el-button  type="danger" icon="Delete" size="small" @click="deleteReaderType(scope.row.id)"></el-button>
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

  <!--  添加读者种类对话框-->
  <el-dialog
      v-model="addReaderTypeVisible"
      title="添加读者种类"
      width="30%"
      :before-close="handleClose"
      @close="addDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="addReaderTypeForm" :rules="addReaderTypeFormRules" ref="addReaderFormRef" label-width="100px">
      <el-form-item label="名称" prop="type_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="addReaderTypeForm.type_name"></el-input>
      </el-form-item>
    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addReaderTypeVisible = false">取消</el-button>
        <el-button type="primary" @click="addReaderType()">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!--  修改读者种类对话框-->
  <el-dialog
      v-model="editReadertypeVisible"
      title="修改读者种类"
      width="30%"
      :before-close="handleClose"
      @close="editDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="editReaderTypeForm" :rules="editReaderTypeFormRules" ref="editReaderTypeFormRef" label-width="100px">
      <el-form-item label="名称" prop="type_name"> <!-- prop是后端参数名 -->
        <el-input v-model="editReaderTypeForm.type_name"></el-input>
      </el-form-item>

    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editReadertypeVisible = false">取消</el-button>
        <el-button type="primary" @click="editRederTypeSubmit()">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import axios_request from "@/utils/axios_request";

export default {
  data(){
    return{
      queryInfo:{
        query:"",
        pagenum:1,
        pagesize:5,
      },
      readerTypeList:[],
      total:0,
      editReadertypeVisible:false,//控制修改读者种类对话框
      addReaderTypeVisible:false,//控制添加读者种类对话框
      //添加读者种类的表单数据
      addReaderTypeForm: {},
      //编辑读者种类的表单数据
      editReaderTypeForm:{},
      //添加读者种类表单的验证规则对象
      addReaderTypeFormRules: {
        type_name: [{required:true,message:'请输入名称',trigger:'blur'}],
      },
      //修改读者种类表单的验证规则对象
      editReaderTypeFormRules: {
        type_name: [{required:true,message:'请输入名称',trigger:'blur'}],
      }
    }
  },
  created() {
    this.getReaderTypeLsit();
  },
  methods: {
    getReaderTypeLsit() {
      axios_request({
        url: '/readerType/findAllreaderType',
        method: 'get',
        params: this.queryInfo
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("获取读者种类失败！")
        }
        console.log(res);
        this.readerTypeList = res.data.readerTypeList;
        this.total = res.data.total;
      })
    },

    //监听 pagesize 改变的事件
    handleSizeChange(newSize) {
      console.log(newSize);
      this.queryInfo.pagesize = newSize;//重新指定每页数据量
      this.getReaderTypeLsit();//带着新的分页请求获取数据

    },

    //监听 页码值 改变的事件
    handleCurrentChange(newPage) {
      console.log(newPage);
      this.queryInfo.pagenum = newPage;//重新指定当前页
      this.getReaderTypeLsit();//带着新的分页请求获取数据
    },

    //监听添加图书种类对话框的关闭状态
    addDialogClosed() {
      this.$refs.addReaderFormRef.resetFields();
    },


    //添加读者种类
    addReaderType() {
      this.$refs.addReaderFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/readerType/addReaderType',
            method: 'post',
            params: this.addReaderTypeForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("添加读者种类失败！")
            }
            this.$message.success('添加读者种类成功！');
            // 隐藏添加读者种类的对话框
            this.addReaderTypeVisible = false;
            //重新获取读者种类列表数据
            this.getReaderTypeLsit();
          })
        }
      })

    },

    //删除读者种类
    deleteReaderType(id) {
      //弹框询问图书种类是否删除数据
      this.$confirm('此操作将永久删除的读者种类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/readerType/deleteReaderType',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("删除读者种类失败！")
          }
          this.$message.success('删除读者种类成功！');
          //重新获取读者种类列表数据
          this.getReaderTypeLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消删除'});
      });
    },

    //关闭编辑图书种类的对话框
    editDialogClosed(){
      this.$refs.editReaderTypeFormRef.resetFields();
    },
    //展示编辑图书种类的对话框
    showEditDialog(id){
      axios_request({
        url: '/readerType/editReaderType',
        method: 'get',
        params: {
          id:id
        },
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("查询读者种类失败！")
        }
        this.editReaderTypeForm=res.data.readerType;
        this.editReadertypeVisible = true;
      })
    },
    //修改图书种类确认
    editRederTypeSubmit(){
      this.$refs.editReaderTypeFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/readerType/updateReaderType',
            method: 'post',
            params: this.editReaderTypeForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("修改读者种类失败！")
            }
            this.$message.success('修改读者种类成功！');
            // 隐藏修改图书种类的对话框
            this.editReadertypeVisible = false;
            //重新获取图书种类列表数据
            this.getReaderTypeLsit();
          })
        }
      })
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