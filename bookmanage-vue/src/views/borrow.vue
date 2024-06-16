<template>
  <div>
    <!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>借阅管理</el-breadcrumb-item>
      <el-breadcrumb-item>借阅列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    卡片视图-->
    <el-card>
      <el-row :gutter="20"><!-- gutter是指定每个分栏的间隔 -->
        <!-- 分栏一共占24格，给搜索框7格，添加按钮4格 -->
        <el-col :span="7">
          <!-- 搜索与添加区域 -->
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getBorrowLsit">
            <template #append>
              <el-button icon="Search" @click="getBorrowLsit"></el-button>
            </template>
          </el-input>
        </el-col>

      </el-row>

      <el-table :data="borrowList" max-height="700" stripe>
        <el-table-column label="读者" prop="reader_name"  sortable/>
        <el-table-column label="图书名称" prop="book_name"  />
        <el-table-column label="借出时间" prop="borrow_date"  />
        <el-table-column label="归还时间" prop="return_date"  />
        <el-table-column label="状态" prop="status_name" />
        <el-table-column fixed="right" label="操作"  width="200">
          <template #default="scope">
            <el-tooltip content="修改" effect="dark">
              <el-button  type="primary" icon="Edit" size="small" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" effect="dark">
              <el-button  type="danger" icon="Delete" size="small" @click="deleteBorrow(scope.row.id)"></el-button>
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

  <!--  修改借阅记录对话框-->
  <el-dialog
      v-model="editBorrowVisible"
      title="修改借阅记录"
      width="30%"
      :before-close="handleClose"
      @close="editDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="editBorrowForm" :rules="editBorrowFormRules" ref="editBorrowFormRef" label-width="100px">
      <el-form-item label="读者" prop="reader_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="editBorrowForm.reader_name" disabled></el-input>
      </el-form-item>
      <el-form-item label="图书名称" prop="book_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="editBorrowForm.book_name" disabled></el-input>
      </el-form-item>
      <el-form-item label="借出时间" prop="borrow_date">
        <el-date-picker
            v-model="editBorrowForm.borrow_date"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            :size="size"
        />
      </el-form-item>
      <el-form-item label="归还时间" prop="return_date">
        <el-date-picker
            v-model="editBorrowForm.return_date"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            :size="size"
        />
      </el-form-item>

      <el-form-item label="状态" prop="status"> <!-- prop是验证规则属性 -->
        <el-radio-group v-model="editBorrowForm.status">
          <el-radio label="0">待还</el-radio>
          <el-radio label="1">超期</el-radio>
          <el-radio label="2">已还</el-radio>
        </el-radio-group>
      </el-form-item>

    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editBorrowVisible = false">取消</el-button>
        <el-button type="primary" @click="editBorrowSubmit()">确定</el-button>
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
      addBorrowVisible:false,//控制添加读者对话框
      editBorrowVisible:false,//控制修改读者对话框
      borrowList:[],
      total:0,
      //添加读者的表单数据
      addBorrowForm:{},
      //修改读者的表单数据
      editBorrowForm:{},
      //添加读者表单的验证规则对象
      addBorrowFormRules: {
        reader_name: [{required:true,message:'请输入用户名',trigger:'blur'},
          {min:3,max:10,message:'用户名长度在3~10个字符',trigger:'blur'}],
        reader_type: [{required:true,message:'请选择身份',trigger:'blur'}],
        sex: [{required:true,trigger:'blur'}],
        faculties: [{required:true,message:'请输入院系',trigger:'blur'}],
        phone: [{required:true,message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      },
      //修改读者表单的验证规则对象
      editBorrowFormRules: {
        id_number: [{message:'请输入身份证号',trigger:'blur'}],
        phone: [{message:'请输入手机号',trigger:'blur'},{validator: checkPhone,trigger:'blur'}]
      }
    }
  },
  created() {
    this.getBorrowLsit();
  },
  methods:{
    getBorrowLsit(){
      axios_request({
        url:'/borrow/findAllborrow',
        method:'get',
        params:this.queryInfo
      }).then(res=>{
        if(res.code!==200){
          this.$message.error("获取借阅记录列表失败！")
        }
        console.log(res);
        this.borrowList=res.data.borrowList;
        this.total=res.data.total;
      })
    },


    //关闭编辑借阅记录的对话框
    editDialogClosed(){
      this.$refs.editBorrowFormRef.resetFields();
    },

    //监听 pagesize 改变的事件
    handleSizeChange(newSize){
      console.log(newSize);
      this.queryInfo.pagesize=newSize;//重新指定每页数据量
      this.getBorrowLsit();//带着新的分页请求获取数据

    },

    //监听 页码值 改变的事件
    handleCurrentChange(newPage){
      console.log(newPage);
      this.queryInfo.pagenum=newPage;//重新指定当前页
      this.getBorrowLsit();//带着新的分页请求获取数据
    },

    //展示编辑读者的对话框
    showEditDialog(id){
      axios_request({
        url: '/borrow/editBorrow',
        method: 'get',
        params: {
          id:id
        },
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("查询读者失败！")
        }
        this.editBorrowForm=res.data.borrow;
        this.editBorrowVisible = true;
      })
    },

    //删除借阅记录
    deleteBorrow(id) {
      //弹框询问是否删除数据
      this.$confirm('此操作将永久删除此条记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/borrow/deleteBorrow',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("删除记录失败！")
          }
          this.$message.success('删除记录成功！');
          //重新获取用户列表数据
          this.getBorrowLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消删除'});
      });
    },



    //修改借阅记录确认
    editBorrowSubmit(){
      this.$refs.editBorrowFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/borrow/updateBorrow',
            method: 'post',
            params: this.editBorrowForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("修改借阅记录失败！")
            }
            this.$message.success('修改借阅记录成功！');
            // 隐藏修改借阅记录的对话框
            this.editBorrowVisible = false;
            //重新获取借阅记录列表数据
            this.getBorrowLsit();
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