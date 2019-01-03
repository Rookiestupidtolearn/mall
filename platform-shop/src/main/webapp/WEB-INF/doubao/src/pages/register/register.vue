<template>
 	<div class="container">
 		<div class="icon">
 			<img :src="logo"/>
 			<h3>斗宝商城</h3>
 		</div>
 		<div class="form">
 			<p>快速登录</p>
 			<mt-field label="手机号码"  placeholder="请输入手机号码" type="tel" v-model="phone"  :attr="{ maxlength: 11 }" ></mt-field>
 			<mt-field placeholder="请输入图形验证码" v-model="imgcaptcha"  :attr="{ maxlength: 4}"  :style="{display:[ showImg ? 'block' : 'none']}">
				<img :src="imgyzm" class="imgYzm" @click="refreshCode"/>
			</mt-field>
 			<mt-field label="验证码" placeholder="请输入验证码" v-model="captcha"  :attr="{ maxlength: 4}" >
				<mt-button type="primary" size="small" :disabled="disabled" @click="yzm" >{{count}}</mt-button>
			</mt-field>
 		</div>
 		<p class="last">
 			<input type="checkbox" id="checkbox"  v-model="checked" />
 			<span>已同意并阅读</span>
 			<i @click="fwxieyi">《平台服务协议》</i>
 		</p>
 		<mt-button type="primary" size="large" class="mtButton" @click="submit">登录</mt-button>
 		<returnhome :scrollshow = "scrollshow"></returnhome>
  </div>
</template>

<script>
	import returnhome from '@/components/returnHome';
	
	export default {
	  name: 'coupon',
	  components:{returnhome},
	  data () {
	    return {
	    	scrollshow:true,
	    	phone:'',
	    	captcha:'',
	    	imgcaptcha:'',
	    	imgyzm:'',
	    	showImg:false,
	    	count:'获取验证码',
	    	disabled:false,
	    	checked:false,
	    	logo:require('../../../static/images/logo.png')
	    }
	  },
	  mounted(){
	  		let that = this;
	  		var phone =  that.$cookie.getCookie('phone');
	  		var captcha =  that.$cookie.getCookie('captcha');
	  		var checked =  that.$cookie.getCookie('checked');
	  		if(phone !=="" ){
	  			that.phone = phone;
	  		}
	  		if(captcha !=="" ){
	  			that.captcha = captcha;
	  		}
	  		if(checked !==""){
	  			that.checked = JSON.parse(checked);
	  		} 
	  },
	  methods:{
	  	refreshCode(){
	  		var randomData = new Date();
    	    this.imgyzm="http://app.doubaoclub.com:6101/platform/api/image.jpg?date"+randomData.getMilliseconds(); //正式
//	  	    this.imgyzm="http://192.168.124.28:8080/platform/api/image.jpg?date"+randomData.getMilliseconds();//冯蓉基本地
//	  	    this.imgyzm="http://192.168.124.28:8080/platform/captcha.jpg?date"+randomData.getMilliseconds();//冯蓉基本地
//        this.imgyzm="http://192.168.124.50:6101/platform/captcha.jpg?date"+randomData.getMilliseconds();//测试环境
	//  		this.imgyzm="http://simuwap.doubaoclub.com:6201/platform/api/image.jpg?date"+randomData.getMilliseconds();  //预发布
	  	},
	  	fwxieyi(){
	  		let that = this;
	  		that.$cookie.setCookie('phone',this.phone);
	  		that.$cookie.setCookie('captcha',this.captcha);
	  		that.$cookie.setCookie('checked',this.checked);
	  		that.$router.push('/pages/xieyi/ptfwxy');
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
	  		
	  		if(this.showImg == true){
	  			if(this.imgcaptcha == ''){
		  			this.$toast({message:'请输入图形验证码',duration:1500});
		  			return false;
		  		}
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
		        	code:this.captcha,
		        	yzm:this.imgcaptcha//图形验证码需要传的参数
		        }
	    	}).then(function (res) {
	    		if(res.data.code !== 500){
		    		if(that.$cookie.getCookie('userId') == undefined || that.$cookie.getCookie('userId') == null || that.$cookie.getCookie('userId') == ''){
						that.$cookie.setCookie('userId',res.data.data.userId);
					}
					if(that.$cookie.getCookie('userInfo') == undefined || that.$cookie.getCookie('userInfo') == null || that.$cookie.getCookie('userInfo') == ''){
						that.$cookie.setCookie('userInfo', JSON.stringify(res.data.data.userInfo));
					}
					if(that.$cookie.getCookie('token') == undefined || that.$cookie.getCookie('token') == null || that.$cookie.getCookie('token') == ''){
						that.$cookie.setCookie('token', res.data.data.token);
					}
					that.$router.go(-1);
				}else{
					that.$toast({message:res.data.msg,duration:1500});
				}
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
	  		this.$http.defaults.withCredentials = true;
	  		//that.refreshCode();
			this.$http({
		        method: 'post',
		        url:that.$url+ 'sendSms',
		        params:{
		        	mobile:this.phone,
		        	imageCode:this.imgcaptcha
		        }
	    	}).then(function (res) {
	    	
//	    		var res ={"data":{"msg": "请传入图形验证码","errno": 0,"count": 5}};
	    		if(res.data.errno == 0){
	    			/*图形验证码是否显示*/
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
		    		that.$toast({message:res.data.msg,duration:3000});
		    		if (res.data.count >=5){
			  			that.showImg = true;
						that.refreshCode();
			  		}
		    	}
			})
	  	}
	  }
	  
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.imgYzm{
		width:2rem;
	}
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
