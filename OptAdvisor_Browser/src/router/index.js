import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

/**
  / 留作统一nav页面的嵌套路由的父路由
 展示页(主页) /home
 登录 /login
 (行情展示) /{50ETF,50ETFOption}
 个人信息 /profile/:id
 资产配置 /allocation
 套期保值 /hedging
 信息采集 /infoCollect
 自选组合 /diy
 风险跟踪(这个就放在我的组合页面里好了,做成dropdown之类的)
 组合查看 /myPortfolio
 消息查看 /prompt

 */
export default new Router({
  mode:'history',
  base:'/',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../pages/Login')
    },
    {
      path: '/signUp',
      name: 'SignUp',
      component: () => import('../pages/SignUp')
    },
    {
      path: '/forgetPassword',
      name: 'ForgetPassword',
      component: () => import('../pages/ForgetPassword')
    },
    {
      path: '/home',
      component: () => import('../pages/Home')
    },
    {
      path: '/',
      redirect: '/home',
      name: 'Father',
      component: () => import('../pages/FatherPage'),
      children :[
        {
          path:'50ETF',
          component:() => import('../pages/Display50ETF')
        },
        {
          path:'50ETFOption',
          component:() => import('../pages/Display50ETFOption')
        },
        {
          path:'profile/:id',
          component:() => import('../pages/Profile')
        },
        {
          path: 'allocation',
          component: () => import('../pages/Allocation')
        },
        {
          path: 'hedging',
          component: () => import('../pages/Hedging')
        },
        {
          path: 'diy',
          component: () => import('../pages/DIY')
        },
        {
          path: 'myPortfolio',
          component: () => import('../pages/MyPortfolio')
        },
        {
          path: 'prompt',
          component: () => import('../pages/Prompt')
        }
      ]
    }
  ]
})
