
// /* eslint-disable */
import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'login',
    component: function () {
      return import( '../views/login')
    }
  },
  {
    path: '/index',
    name: 'index',
    redirect:'/reader',
    component: function () {
      return import( '../views/index')
    },
    children:[
      {
        path:'/reader',
        name:'reader',
        component: function () {
          return import( '../views/reader')
        },
      },
      {
        path:'/book',
        name:'book',
        component: function () {
          return import( '../views/book')
        },
      },
      {
        path:'/borrow',
        name:'borrow',
        component: function () {
          return import( '../views/borrow')
        },
      },
      {
        path:'/bookType',
        name:'bookType',
        component: function () {
          return import( '../views/basicInformation/bookType')
        },
      },
      {
        path:'/readerType',
        name:'readerType',
        component: function () {
          return import( '../views/basicInformation/readerType')
        },
      },
      {
        path:'/user',
        name:'user',
        component: function () {
          return import( '../views/user')
        },
      },
      {
        path:'/personCenter',
        name:'personCenter',
        component: function () {
          return import( '../views/personCenter/index')
        },
      }
    ]
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})


//挂载路由导航守卫
//to是我们跳转的路径，from是来自的路径，next为放行函数
router.beforeEach((to,from,next)=>{
  //如果用户访问登录页，直接放行
  if(to.path ==="/") return next();
  //从sessionStorage中获取到保存的token值
  const tokenStr = window.sessionStorage.getItem("token");
  //没有token，强制跳转到登录页面
  if(!tokenStr) return next("/");
  next();//有token，直接放行
})


export default router
