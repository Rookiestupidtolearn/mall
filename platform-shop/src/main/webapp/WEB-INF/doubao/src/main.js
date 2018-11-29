// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
//接口后加.options是因为后台对于请求参数的接口进行的跨域特殊处理
import Vue from 'vue'
import App from './App'
import router from './router'
import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'
import axios from 'axios'  //ajax
import fontSize from './assets/fontSize.js' //字号适配js
import cookie from './assets/cookie.js'  //cookie.js公用方法
import './assets/css/reset.css' //公用样式引入
  import { MessageBox } from 'mint-ui';
  

Vue.use(MintUI);

Vue.prototype.$http= axios
Vue.prototype.$cookie= cookie
Vue.config.productionTip = false

//接口配置
//Vue.prototype.$url= '/platform/api/'; //本地代理
//Vue.prototype.$url= 'http://106.75.99.126:6302/platform/api/'; //外网
//Vue.prototype.$url= 'http://192.168.1.244:8093/platform/api/'; //外网吴晚龙
Vue.prototype.$url= 'http://192.168.0.11:6101/platform/api/'; //外网(打正式包需要替换)

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    var token = cookie.getCookie('token');
    if(token !== null || token !== ""){
    	config.headers['X-Nideshop-Token'] = token
    }
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    /*登录公用方法*/
   if(response.data.errmsg == "请先登录" || response.data.errno ==401 ){
   				cookie.delCookie('userId');
    			cookie.delCookie('userInfo');
    			cookie.delCookie('token');
				function yzUserMessage(userMessage){
					if(cookie.getCookie(userMessage) == undefined || cookie.getCookie(userMessage) == null || cookie.getCookie(userMessage) == ''){
						return true;
					}else{
						return false;
					}
				}
	    	if(yzUserMessage('userId') && yzUserMessage('userInfo')){
			    	MessageBox({
						  title: ' ',
						  message: '请先登录 ',
						  showCancelButton: true
						},function(params){
								if(params == 'confirm'){
										router.push('/pages/register/register');
								}
						});
					}
		}
    return response;
  }, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
  });



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
