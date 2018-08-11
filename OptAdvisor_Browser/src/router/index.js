import Vue from 'vue'
import Router from 'vue-router'
import Login from '../pages/Login'
import SignUp from '../pages/SignUp'
import ForgetPassword from '../pages/ForgetPassword'
import FatherPage from '../pages/FatherPage'
import Home from '../pages/Home'
import Display50ETF from '../pages/Display50ETF'
import Display50ETFOption from '../pages/Display50ETFOption'
import Profile from '../pages/Profile'
import Allocation from '../pages/Allocation'
import Hedging from '../pages/Hedging'
import InfoCollect from '../pages/InfoCollect'
import DIY from '../pages/DIY'
import MyPortfolio from '../pages/MyPortfolio'
import Prompt from '../pages/Prompt'


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
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component : Login
    },
    {
      path: '/signUp',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/forgetPassword',
      name: 'ForgetPassword',
      component: ForgetPassword
    },
    {
      path: '/',
      redirect: '/home',
      name: 'Father',
      component: FatherPage,
      children :[
        {
          path: 'home',
          component: Home
        },
        {
          path:'50ETF',
          component:Display50ETF
        },
        {
          path:'50ETFOption',
          component:Display50ETFOption
        },
        {
          path:'profile/:id',
          component:Profile
        },
        {
          path: 'allocation',
          component: Allocation
        },
        {
          path: 'hedging',
          component: Hedging
        },
        {
          path: 'infoCollect',
          component: InfoCollect
        },
        {
          path: 'diy',
          component: DIY
        },
        {
          path: 'myPortfolio',
          component: MyPortfolio
        },
        {
          path: 'prompt',
          component: Prompt
        }
      ]
    }
  ]
})
