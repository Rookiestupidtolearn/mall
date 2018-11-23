<template>
 	<div class="container">
 		<div class="icon">
 			<img src="../../../static/images/logo.png"/>
 			<h3>斗宝商城</h3>
 		</div>
 		<div class="form">
 			<p>快速登录</p>
 			<mt-field label="手机号码"  placeholder="请输入手机号码" type="tel" v-model="phone" :attr="{ maxlength: 11 }" ></mt-field>
 			<mt-field label="验证码" placeholder="请输入验证码" v-model="captcha" :attr="{ maxlength: 4}" >
			<mt-button type="primary" size="small" :disabled="disabled" @click="yzm" >{{count}}</mt-button>
			</mt-field>
 		</div>
 		<p class="last">
 			<input type="checkbox" id="checkbox"  v-model="checked" />
 			<span>已同意并阅读</span>
 			<i @click="fwxieyi">《平台服务协议》</i>
 		</p>
 		<mt-button type="primary" size="large" class="mtButton" @click="submit">登录</mt-button>
  </div>
</template>

<script>
	import { setCookie,getCookie,delCookie } from '@/assets/cookie';
		
	export default {
	  name: 'coupon',
	  data () {
	    return {
	    	phone:'',
	    	captcha:'',
	    	count:'获取验证码',
	    	disabled:false,
	    	checked:false
	    }
	  },
	  methods:{
	  	fwxieyi(){
	  		this.$router.push('/pages/xieyi/ptfwxy');
	  	},
	  	submit(){
	  		let that = this;
	  		let reg = /^1\d{10}$/ ;
	  		let reg2 = /^1\d{3}$/ ;
	  		if(this.phone == ''){
	  			this.$toast({message:'请输入手机号码',duration:1500});
	  			return false;
	  		}else if( this.phone.length !== 11){
	  			this.$toast('手机号码格式不正确');
	  			return false;
	  		}else if( !reg.test(this.phone)){
	  			this.$toast('手机号码格式不正确');
                return false;
	  		}
	  		
	  		if(this.captcha == ''){
	  			this.$toast({message:'请输入验证码',duration:1500});
	  			return false;
	  		}
	  		
	  		if(this.checked == false){
	  			this.$toast({message:'请阅读平台服务协议',duration:1500});
	  			return false;
	  		}
	  		this.$http({
		        method: 'post',
		        url:that.$url+ 'auth/login_by_mobile',
		        params:{
		        	mobile:this.phone,
		        	code:this.captcha
		        }
	    	}).then(function (res) {
	    		if(getCookie('userId') == undefined || getCookie('userId') == null || getCookie('userId') == ''){
					setCookie('userId',res.data.data.userId);
				}
				if(getCookie('userInfo') == undefined || getCookie('userInfo') == null || getCookie('userInfo') == ''){
					setCookie('userInfo', JSON.stringify(res.data.data.userInfo));
				}
				if(getCookie('token') == undefined || getCookie('token') == null || getCookie('token') == ''){
					setCookie('token', res.data.data.token);
				}
//				var code = res.data.data.token;
//	    		var userInfo = res.data.data.userInfo;
//	    		var data = {code:code,userInfo:userInfo}
//				that.$http({
//			        method: 'post',
//			        url:that.$url+ 'auth/login_by_weixin',
//			        params:JSON.stringify(data)
//		    	}).then(function (res) {
//		    		console.log(res);
//				})
		    	that.$router.go(-1);
			})
	  	},
	  	yzm(){
	  		let that = this;
	  		let reg = /^1\d{10}$/ ;
	  		if(this.phone == ''){
	  			this.$toast({message:'请输入手机号码',duration:1500});
	  			return false;
	  		}else if( this.phone.length !== 11){
	  			this.$toast('手机号码格式不正确');
	  			return false;
	  		}else if( !reg.test(this.phone)){
	  			this.$toast('手机号码格式不正确');
                return false;
	  		}
			this.$http({
		        method: 'post',
		        url:that.$url+ 'sendSms',
		        params:{
		        	mobile:this.phone
		        }
	    	}).then(function (res) {
	    		if(res.data.code == 0){
                    that.disabled = true;
			  		let i = 60;
			  		let countDown = setInterval(function(){
			  			i -- ;
			  			if( i<10){
			  				that.count = '0'+i +'s';
			  			}else{
			  				that.count =i +'s';
			  			}
			  			if(i<=0){
			  				clearInterval(countDown);
			  				that.count = '获取验证码';
			  				that.disabled = false;
			  			}
			  		}, 1000);
		    	}else{
		    		this.$toast({message:res.msg,duration:3000});
		    	}
			})
	  	}
	  }
	  
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.icon{
		margin-top:.5rem;
	}
	.icon img{
		width:1rem;
	}
	.icon h3{
		font-size:.34rem;
	}
	.form p{
		font-size:.32rem;
		text-align: left;
		padding:0 .2rem;
		margin-top:.4rem;
		margin-bottom:.2rem;
	}
	.last{
		font-size:.28rem;
		margin-top:.2rem;
		text-align: left;
		padding-left:.2rem;
	}
	#checkbox{
		-webkit-appearance: checkbox;
	}
	.last i{
		color:#26a2ff;
	}
	.mtButton{
	    width: 6.4rem;
	    margin: 0 auto;
	    margin-top: .5rem;
	}
</style>
