import axios from 'axios'  ;//ajax
import cookie from '@/assets/cookie.js'  

const service = axios.create({
	headers:{
		'Content-Type':'application/json;charset=UTF-8'
	}
});

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


export default service;
///****** respone拦截器==>对响应做处理 ******/
//service.interceptors.response.use(
//  response => {  //成功请求到数据
//      //这里根据后端提供的数据进行对应的处理
//      if(response.data.errno == '401' || response.data.errno == '请先登录'){
//		    			that.$toast(response.data.errmsg);
//		    			that.$cookie.delCookie('userId');
//		    			that.$cookie.delCookie('userInfo');
//		    			that.$cookie.delCookie('token');
//		    			that.fontSize.goLogin()
//		}else if(response.data.errno == '1' ){
//			that.$toast(response.data.errmsg);
//		}
//  },
//  error => {  //响应错误处理
//      console.log('error');
//      console.log(error);
// 
//      let text = JSON.parse(JSON.stringify(error)).response.status === 404
//          ? '404'
//          : '网络异常，请重试';
// 
//      return Promise.reject(error)
//  }
//);
