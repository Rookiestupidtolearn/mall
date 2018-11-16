// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
//zr minui 组件使用页面单独引要使用的组件   举例:order.vue import { InfiniteScroll } from 'mint-ui';
import Vue from 'vue'
import App from './App'
import router from './router'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import axios from 'axios'  //ajax
import './assets/fontSize.js' //字号适配js
import './assets/css/reset.css' //公用样式引入

Vue.use(MintUI)
Vue.prototype.$http= axios
Vue.config.productionTip = false

//接口配置
Vue.prototype.$url='http://localhost:8080/platform/api/'  //本地
//Vue.prototype.$url='http://192.168.0.11:6101/platform/api/'  //本地
//Vue.prototype.$url= 'http://106.75.99.126:6302/platform/api/'; //外网



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
