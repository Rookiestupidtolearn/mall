<template>
 	<div class="container">
 		<div class="icon">
 			<img src="../../../static/images/logo.png"/>
 			<h3>斗宝商城</h3>
 		</div>
 		<div class="form">
 			<p>快速登录</p>
 			<mt-field label="手机号码" placeholder="请输入手机号码" type="tel" v-model="phone"></mt-field>
 			<mt-field label="验证码" placeholder="请输入验证码" v-model="captcha">
			<mt-button type="primary" size="small" :disabled="disabled" @click="yzm" class="buyzm">{{count}}</mt-button>
			</mt-field>
 		</div>
 		<p class="last">
 			<input type="checkbox" id="checkbox"  v-model="checked"/>
 			<span>已同意并阅读</span>
 			<i @click="fwxieyi">《平台服务协议》</i>
 		</p>
 		<mt-button type="primary" size="large" class="mtButton" @click="submit">登录</mt-button>
  </div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Toast } from 'mint-ui';
	import { Field } from 'mint-ui';
	import { Checklist } from 'mint-ui';
	import ptfwxy from '@/pages/xieyi/ptfwxy';
		
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
	  		if(this.phone == ''){
	  			Toast({message:'请输入手机号码',duration:1500});
	  			return false;
	  		}else if( this.phone.length !== 11){
	  			Toast('手机号码格式不正确');
	  			return false;
	  		}
	  		
	  		if(this.captcha == ''){
	  			Toast({message:'请输入验证码',duration:1500});
	  			return false;
	  		}
	  		
	  		if(this.checked == false){
	  			Toast({message:'请阅读平台服务协议',duration:1500});
	  			return false;
	  		}
	  		console.log('成功');
	  	},
	  	yzm(){
	  		let that = this;
			this.$http({
		        method: 'post',
		        url:that.$url+ 'user/smscode',
	    	}).then(function (response) {
	    		response = {"errno":0,"data":"短信发送成功","errmsg":"执行成功"};
	    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
	    			MessageBox({
						  title: ' ',
						  message: '请先登录 ',
						  showCancelButton: true
						});
	    			return false;
	    		}else{
					Toast('发送成功');
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
	.buyzm{
		width:1.9rem; 
		height:.66rem;
	}
</style>
