<template>
  <div>
    <!--    面白屑导航-->
    <el-breadcrumb separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>图书管理</el-breadcrumb-item>
      <el-breadcrumb-item>图书列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    卡片视图-->
    <el-card>
      <el-row :gutter="20"><!-- gutter是指定每个分栏的间隔 -->
        <!-- 分栏一共占24格，给搜索框7格，添加按钮4格 -->
        <el-col :span="7">
          <!-- 搜索与添加区域 -->
          <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getBookLsit">
            <template #append>
              <el-button icon="Search" @click="getBookLsit"></el-button>
            </template>
          </el-input>
        </el-col>

        <el-col :span="4">
          <el-button type="primary" @click="openaddBookVisible">添加图书</el-button>
        </el-col>

      </el-row>

      <el-table :data="bookList" max-height="700" stripe>
        <el-table-column label="封面" prop="bookimage_url" >
          <template #default="scope">
            <el-image
                style="width: 100px; height: 100px"
                :preview-src-list="[scope.row.bookimage_url]"
                preview-teleported="true"
                :src="scope.row.bookimage_url"
                :zoom-rate="1.2"
                fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="book_name"  sortable/>
        <el-table-column label="作者" prop="author"  />
        <el-table-column label="出版社" prop="publishing_house"  />
        <el-table-column label="定价" prop="price"  />
        <el-table-column label="出版日期" prop="publishing_time" />
        <el-table-column label="图书类型" prop="type_name" />
        <el-table-column fixed="right" label="操作"  width="200">
          <template #default="scope">
            <el-tooltip content="修改" effect="dark">
              <el-button  type="primary" icon="Edit" size="small" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" effect="dark">
              <el-button  type="danger" icon="Delete" size="small" @click="deleteBook(scope.row.id)"></el-button>
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
  <!--  添加图书对话框-->
  <el-dialog
      v-model="addBookVisible"
      title="添加图书"
      width="30%"
      @close="addDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="addBookForm" :rules="addBookFormRules" ref="addBookFormRef" label-width="100px">

      <el-form-item label="名称" prop="book_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="addBookForm.book_name"></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="bookimage_url">

        <el-input v-model="addBookForm.bookimage_url" v-if="false"></el-input>
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
            :data="addBookForm"
        >
          <el-icon><Plus /></el-icon>

          <template #tip>
            <div class="el-upload__tip">只能上传图片,且单张图片大小不能超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="addBookForm.author"></el-input>
      </el-form-item>
      <el-form-item label="出版社" prop="publishing_house"> <!-- prop是验证规则属性 -->
        <el-input v-model="addBookForm.publishing_house"></el-input>
      </el-form-item>

      <el-form-item label="定价" prop="price">
        <el-input v-model="addBookForm.price"></el-input>
      </el-form-item>
      <el-form-item label="出版日期" prop="publishing_time">
        <el-date-picker
            v-model="addBookForm.publishing_time"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            :size="size"
        />
      </el-form-item>
      <el-form-item label="类型" prop="book_type">
        <el-select v-model="addBookForm.book_type" clearable placeholder="请选择类型">
          <el-option
              v-for="item in bookTypeList"
              :key="item.id"
              :label="item.type_name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>

    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addBookVisible = false">取消</el-button>
        <el-button type="primary" @click="addBook()">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!--  放大图片-->
  <el-dialog style="width: 35%" v-model="dialogVisible" title="预览">
    <img style="width: 100%;" :src="dialogImageUrl" alt="预览图象" />
  </el-dialog>


  <!--  修改图书对话框-->
  <el-dialog
      v-model="editBookVisible"
      title="修改图书"
      width="30%"
      @close="editDialogClosed"
  >
    <!--    内容主题区-->
    <el-form :model="editBookForm" :rules="editBookFormRules" ref="editBookFormRef" label-width="100px">
      <el-form-item label="名称" prop="book_name"> <!-- prop是验证规则属性 -->
        <el-input v-model="editBookForm.book_name"></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="bookimage_url">
        <el-input v-model="editBookForm.bookimage_url" v-if="false"></el-input>
        <el-upload
            ref="uploadEdit"
            class="avatar-uploader"
            action="/api/upload/uploadImage"
            list-type="picture-card"
            :limit="1"
            :on-preview="handlePictureCardPreview"
            :on-success="handleAvatarEditSuccess"
            :before-upload="beforeAvatarUpload"
            :data="editBookForm"
        >
          <img style="width:148px" v-if="editBookForm.bookimage_url && editBookForm.bookimage_url.indexOf('8083')!==-1" :src="editBookForm.bookimage_url" class="avatar" />
          <el-icon v-else ><Plus /></el-icon>

          <template #tip>
            <div class="el-upload__tip">只能上传图片,且单张图片大小不能超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="editBookForm.author"></el-input>
      </el-form-item>
      <el-form-item label="出版社" prop="publishing_house"> <!-- prop是验证规则属性 -->
        <el-input v-model="editBookForm.publishing_house"></el-input>
      </el-form-item>

      <el-form-item label="定价" prop="price">
        <el-input v-model="editBookForm.price"></el-input>
      </el-form-item>
      <el-form-item label="出版日期" prop="publishing_time">
        <el-date-picker
            v-model="editBookForm.publishing_time"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            :size="size"
        />
      </el-form-item>
      <el-form-item label="类型" prop="book_type">
        <el-select v-model="editBookForm.book_type" clearable placeholder="请选择类型">
          <el-option
              v-for="item in bookTypeList"
              :key="item.id"
              :label="item.type_name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>

    </el-form>

    <!--    底部区-->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editBookVisible = false">取消</el-button>
        <el-button type="primary" @click="editBookSubmit()">确定</el-button>
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
    return{
      //查询参数
      queryInfo:{
        query:"",
        pagenum:1,
        pagesize:5,
      },
      dialogImageUrl:"",//放大图片路径
      dialogVisible:false,//放大图片
      addBookVisible:false,//控制添加图书对话框
      editBookVisible:false,//控制修改图书对话框
      bookTypeList:[],//图书类型
      bookList:[],//图书
      total:0,//查询总数
      //添加图书的表单数据
      addBookForm:{},
      //修改图书的表单数据
      editBookForm:{},
      //添加图书表单的验证规则对象
      addBookFormRules: {
        reader_name: [{required:true,message:'请输入用户名',trigger:'blur'},
          {min:3,max:10,message:'用户名长度在3~10个字符',trigger:'blur'}],
        reader_type: [{required:true,message:'请选择身份',trigger:'blur'}],
        sex: [{required:true,trigger:'blur'}],
        faculties: [{required:true,message:'请输入院系',trigger:'blur'}],
      },
      //修改图书表单的验证规则对象
      editBookFormRules: {
        id_number: [{message:'请输入身份证号',trigger:'blur'}],
      }
    }
  },
  created() {
    this.getBookLsit();
  },
  methods:{

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

    // 添加图书上传成功，获取返回的图片地址
    handleAvatarAddSuccess(res){
      console.log("res="+res);
      this.addBookForm.bookimage_url=res.data.fileName;

    },

    // 修改图书上传成功，获取返回的图片地址
    handleAvatarEditSuccess(res){
      console.log("res="+res);
      this.editBookForm.bookimage_url=res.data.fileName;

    },

    getBookLsit(){
      axios_request({
        url:'/book/findAllbook',
        method:'get',
        params:this.queryInfo
      }).then(res=>{
        if(res.code!==200){
          this.$message.error("获取图书列表失败！")
        }
        console.log(res);
        this.bookList=res.data.bookList;
        this.total=res.data.total;
      })
    },

    // 监听添加图书对话框的关闭状态
    addDialogClosed() {
      this.$refs.addBookFormRef.resetFields();
      this.$refs.uploadAdd.clearFiles();
    },

    //关闭编辑图书的对话框
    editDialogClosed(){
      // this.$refs.editBookFormRef.resetFields();
      this.$refs.uploadEdit.clearFiles();
    },

    //监听 pagesize 改变的事件
    handleSizeChange(newSize){
      console.log(newSize);
      this.queryInfo.pagesize=newSize;//重新指定每页数据量
      this.getBookLsit();//带着新的分页请求获取数据

    },

    //监听 页码值 改变的事件
    handleCurrentChange(newPage){
      console.log(newPage);
      this.queryInfo.pagenum=newPage;//重新指定当前页
      this.getBookLsit();//带着新的分页请求获取数据
    },

    //展示编辑图书的对话框
    showEditDialog(id){
      this.getBookType();
      axios_request({
        url: '/book/editBook',
        method: 'get',
        params: {
          id:id
        },
      }).then(res => {
        if (res.code !== 200) {
          this.$message.error("查询图书失败！")
        }
        this.editBookForm=res.data.book;
        this.editBookVisible = true;
      })
    },
    //打开添加图书对话框
    openaddBookVisible(){
      this.getBookType();
      this.addBookVisible=true;
    },
    //获取图书类型
    getBookType(){
      axios_request({
        url:'/bookType/findAllbookType',
        method:'get',
        params:this.queryInfo
      }).then(res=>{
        if(res.code!==200){
          this.$message.error("获取图书类型列表失败！")
        }
        console.log(res);
        this.bookTypeList=res.data.bookTypeList;
        this.total=res.data.total;
      })
    },

    //删除图书
    deleteBook(id) {
      //弹框询问是否删除数据
      this.$confirm('此操作将永久删除该书, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        axios_request({
          url: '/book/deleteBook',
          method: 'post',
          params: {
            id:id
          },
        }).then(res => {
          if (res.code !== 200) {
            this.$message.error("删除图书失败！")
          }
          this.$message.success('删除图书成功！');
          //重新获取图书列表数据
          this.getBookLsit();
        })
      }).catch(() => {
        this.$message({type: 'info', message: '已取消删除'});
      });
    },


    //添加图书
    addBook() {
      this.$refs.addBookFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/book/addBook',
            method: 'post',
            params: this.addBookForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("添加图书失败！")
            }
            this.$message.success('添加图书成功！');
            // 隐藏添加图书的对话框
            this.addBookVisible = false;
            //重新获取图书列表数据
            this.getBookLsit();
          })
        }
      })

    },

    //修改图书确认
    editBookSubmit(){
      this.$refs.editBookFormRef.validate(valid => {
        if (valid) {
          axios_request({
            url: '/book/updateBook',
            method: 'post',
            params: this.editBookForm
          }).then(res => {
            if (res.code !== 200) {
              this.$message.error("修改图书失败！")
            }
            this.$message.success('修改图书成功！');
            //隐藏修改图书的对话框
            this.editBookVisible = false;
            //重新获取图书列表数据
            this.getBookLsit();
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