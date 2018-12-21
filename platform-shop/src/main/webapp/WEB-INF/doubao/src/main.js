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
/*解决android白版问题*/
import 'babel-polyfill'
import Es6Promise from 'es6-promise'
Es6Promise.polyfill()

 import { MessageBox } from 'mint-ui';
  

Vue.use(MintUI);

Vue.prototype.$http= axios
Vue.prototype.$cookie= cookie
Vue.config.productionTip = false

//接口配置
//Vue.prototype.$url= '/platform/api/'; //本地代理
//Vue.prototype.$url= 'http://192.168.124.29:8084/platform/api/'; //本地环境
Vue.prototype.$url= 'http://117.50.60.55:6201/platform/api/'; //预发布环境
//Vue.prototype.$url= 'http://192.168.124.50:6101/platform/api/'; //测试环境

//切换页面时滚动条自动滚动到顶部的方法
router.afterEach((to,from,next) => {
  window.scrollTo(0,0);
});
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
//	var appHref = 'http://192.168.124.29:8081/#/pages/ucenter/order?device=android&token=token1';  //android和ios返回链接样本
//var appHref = 'http://192.168.124.29:8081/#/pages/ucenter/order?id=2132323&device=android&token=token1';  //商品详情处理android和ios返回链接样本
//获取token 区分android和ios
var appHref = window.location.href;
	if(appHref.indexOf('device')>-1){
		var tokenDevice = appHref.split('?')[1].split('=')[1].split('&')[0];
		if(appHref.split('=')[2] != undefined){
			var tokenDetail = appHref.split('=')[2].split('&')[0];  //商品详情单独处理
		}
	}
	if(appHref.indexOf('token')>-1){
		var splitLength = appHref.split('=');
		var tokenApp = splitLength[splitLength.length-1];
	}
	if(tokenDevice == 'android'  || tokenDetail == 'android' ){
		config.headers['X-Nideshop-Token'] = tokenApp;
	}else if(tokenDevice == 'ios'  || tokenDetail == 'ios' ){
		alert(window.location.href)
		config.headers['X-Nideshop-Token'] = tokenApp;
	}else{
		 // 在发送请求之前做些什么
	    var token = cookie.getCookie('token');
	    if(token !== null || token !== ""){
	    	config.headers['X-Nideshop-Token'] = token
	    }
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
								var hrefD = window.location.href;
								if(hrefD.indexOf('device')>-1){
									var device = hrefD.split('&')[1].split('=')[1];
								}
								if(device == 'android'){
					    			window.android.toLogin(); //调起andriod交互方法(由app发起。浏览器会报错正常)
					    			return false;
						    	}else if(device == 'ios'){
						    		var message = {'url':'toLogin'}
									window.webkit.messageHandlers.webViewApp.postMessage(message);
									return false;
						    	}else{
									router.push('/pages/register/register');
								}
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
