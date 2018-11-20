<template>
 	<div class="container">
 		<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  		
  		<div class="mt88">
 		<div class='titleTop'>
		    <p>绑定账号</p>
		    <div style="overflow: hidden;">
		      <span class='col5e5e5e'>昵称:{{accountSecurity.nickname}}</span>
		      <img  class="image" :src="accountSecurity.avatar"/>
		    </div>
		  </div>
		  <div class="titleBottom">
		    <span>绑定手机号</span>
		    <span class='mobile'  data-c="cee" v-if="telephone">{{telephone}}</span>
			<span class='mobile'  data-c="cdd" v-else @click="bindPhone">去绑定</span>
		  </div>
		  <div class="logout" @click="exitLogin">退出登录</div>
	  </div>
 	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import headbar from '@/components/headbar.vue';	
		
export default {
  name: 'accountSecurity',
    components:{headbar},
  data () {
    return {
		headFont:'账户安全',
    	accountSecurity:[],
    	telephone:''
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'user/userInfo',
	        params:{typeId:0}
    	}).then(function (response) {
      		response = {"data":{"userId":28,"username":"微信用户jp895zmvcjbd","password":"","gender":2,"birthday":null,"register_time":1539068898000,"last_login_time":1541642314000,"last_login_ip":"8.8.8.8","user_level_id":null,"nickname":"张然","mobile":"15158017054","register_ip":"8.8.8.8","avatar":"https://wx.qlogo.cn/mmopen/vi_32/WV9ibUvvrphP9auBTSl3HLpcBmZcWyR3IkXibN4q0dkxiakUfR8TLZfibzokH1tOcUial8yaa6SM5cxkmZAJQjZQpBw/132","weixin_openid":"oohoZ4zvNYZ6_S3ghj4Zy1xX4fhI"}};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
	    			console.log(response.data)
	    			that.accountSecurity = response.data;
	    			if(response.data.mobile == null || response.data.mobile ==''){
	    				that.telephone = response.data.mobile;
    				}else{
	    				that.telephone = that.validateMobile(response.data.mobile);
    				}
    			}
		  })
  },
  methods:{
  	validateMobile(mobile){
	    /*手机号加密处理*/
	    var first = mobile.substr(0, 3);
	    var last = mobile.substr(mobile.length - 4, 4);
	    var finalPhone = first + '****' + last;
	    return finalPhone;
	},
  	bindPhone(){
  		console.log('aaaa');
  	},
  	exitLogin(){
  		console.log('退出登录接口');
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
p{
  font-size:.29rem;
  color:#333;
  display:block;
  padding-top:.11rem;
}
.image{
  width:.56rem;
  height:.56rem;
  border-radius:50%;
  float:right;
  margin-top:-.05rem
}
.titleTop{
  text-align: left;
  padding:0 .30rem;
  background-color:#fff;
  margin-bottom:.05rem;
  padding: .2rem .3rem .3rem .3rem;
}
.titleBottom{
  background-color:#fff;
  padding:.30rem;
  font-size:.28rem;
  overflow: hidden;
}
.titleBottom span{
  float:left;
}
.titleBottom .mobile{
  float:right;
}
.titleTop view text{
  font-size:.26rem;
  padding-top:.20rem;
  padding-bottom: .30rem;
}
.logout{
  width:5.60rem;
  height:.80rem;
  line-height:.80rem;
  background: #c9c9c9;
  color:#333;
  border-radius:.10rem;
  text-align: center;
  margin:0 auto;
  font-weight: bold;
  font-size:.30rem;
  margin-top:2.10rem;
}
.col5e5e5e{
  color:#5e5e5e;
  font-size:.28rem;
  float:left;
  margin-top: .1rem;
}
</style>
