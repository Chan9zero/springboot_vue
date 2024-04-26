import Vue from 'vue'
import VueRouter from 'vue-router'
//解决导航栏点击报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location){
  return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager'),
    redirect: 'home', //重定向
    children: [
      { path:'403', name: 'Auth', meta: {name: '无权限'}, component: () => import('../views/manager/Auth')},
      { path:'home', name:'Home', meta: {name: '系统首页'}, component: () =>import('../views/manager/Home') },
      { path:'user', name:'User', meta: {name: '用户信息'},component: () =>import('../views/manager/User') },
      { path:'person', name:'Person', meta: {name: '个人信息'}, component: () =>import('../views/manager/Person') },
      { path:'password', name:'Password', meta: {name: '修改密码'}, component: () =>import('../views/manager/Password') },
    ]
  },

  { path: '/login', name: 'Login', meta: {name: '登录'}, component: () => import('../views/Login')},
  { path: '/register', name: 'Register', meta: {name: '注册'}, component: () => import('../views/Register')},
  { path: '/*', name:'404', meta: {name: '无法访问'},component: () =>import('../views/404') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) =>{
  //to是到达的路由信息
  //from是开源的路由信息
  //next 是帮助我们跳转路由的函数
  let adminPaths = ['/user']
  let user = JSON.parse(localStorage.getItem('honey-user') || '{}')
  //如果当前用户不是管理员且访问了管理员权限的路径，那么访问跳转别的页面
  if(user.role !== '管理员' && adminPaths.includes(to.path)){
    next('/403')
  }
  else{
    next()
  }


})

export default router
