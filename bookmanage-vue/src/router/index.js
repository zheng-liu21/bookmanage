
/* eslint-disable */
import store from '@/store'
import storage from '@/model/storage'
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
    component: function () {
      return import( '../views/index')
    }
  }
  // {
  //   path: '/',
  //   name: 'Main',
  //   redirect: '/login',
  //   // 某些页面规定必须登录后才能查看 ，可以在router中配置meta，将需要登录的requireAuth设为true，
  //   meta: {
  //     requireAuth: true,
  //   }
  // },
  // {
  //   path: '/login',
  //   component: () => import('@/views/login'),
  // },

]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 设置路由守卫，在进页面之前，判断有token，才进入页面，否则返回登录页面
if (storage.get("token")) {
  store.commit("setToken", storage.get("token"));
}
router.beforeEach((to, from, next) => {
  // 判断要去的路由有没有requiresAuth
  if (to.matched.some(r => r.meta.requireAuth)) {
    if (store.state.token) {
      next(); //有token,进行request请求，后台还会验证token
    } else {
      next({
        path: "/login",
        // 将刚刚要去的路由path（却无权限）作为参数，方便登录成功后直接跳转到该路由，这要进一步在登陆页面判断
        query: { redirect: to.fullPath }
      });
    }
  } else {
    next(); //如果无需token,那么随它去吧
  }
});


export default router
