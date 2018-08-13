// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router/index'
import iView from 'iview';
import 'iview/dist/styles/iview.css';
import './assets/css/main.css';
import './assets/css/animate.css';
import './assets/css/tachyons.css';
import Cookies from 'vue-cookie'
import axios from 'axios'
import VueAxios from 'vue-axios'
import echarts from 'echarts'
import $ from 'jquery'


Vue.use(iView);
Vue.use(Cookies);
Vue.use(VueAxios,axios);
Vue.prototype.$echarts = echarts

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});
