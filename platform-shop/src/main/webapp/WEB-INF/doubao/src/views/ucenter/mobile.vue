<template>
 	<div class="container">
 		<div class="weibind"  :style="{display:[!bindResult ? 'none' : 'block' ]}">
	 		<div class="noBind">
				<div class="userinfo">
					<img class="userinfo-avatar"  :src="mobileInfo.avatar" background-size="cover"/>
				 	 <p class="userinfo-nickname">{{mobileInfo.nickname}}</p>
				</div>
			</div>
		    <div class="login-title">关联手机号</div>
		        <div class="login">
		          <div class="first-line"><input type="number" v-model="telephone"  oninput="if(value.length > 11)value = value.slice(0, 11)"  placeholder="输入手机号" /></div>
		          <div class="second-line">
		            <input type="input" name="code" v-model="teleyzm" oninput="if(value.length > 4)value = value.slice(0, 4)" placeholder="四位验证码"/>
		            <button @click="countDownPassCode" :disabled="disableGetMobileCode" >{{getCodeButtonText}}</button>
		             <!--disabled="{{disableGetMobileCode}}" @click="countDownPassCode"-->
		          </div>
		        </div>
		      <div class="third-line"><button @click="submitForm" :disabled="disableSubmitMobileCode">提交</button></div>
		  </div>
			<div class="titleamount" :style="{display:[bindResult ? 'none' : 'block' ]}">
				<div class='titleTop'>
				    <p>绑定账号</p>
				    <div >
				      <span class='col5e5e5e'>昵称:{{mobileInfo.nickname}}</span>
				      <img  class="image" :src="mobileInfo.avatar"/>
				    </div>
				  </div>
				  <div class="titleBottom">
				    <span>绑定手机号</span>
				    <span class='mobile'  data-c="cee" v-if="telephone">{{telephone}}</span>
					<span class='mobile'  data-c="cdd" v-else @click="bindPhone">去绑定</span>
				  </div>
			 	</div>
			</div>
	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Toast } from 'mint-ui';
		
	export default {
	  name: 'feedback',
	  data () {
	    return {
	        telephone: '',
	        teleyzm:'',
	        bindResult:true,
	        disableGetMobileCode: false,
	        disableSubmitMobileCode: true,
	        getCodeButtonText: '获取验证码',
	        mobileInfo:''
	    }
	  },
	  mounted(){
	  	let that = this;
	  		that.$http({
	        method: 'post',
	        url:that.$url+ 'user/userInfo',
	    	}).then(function (response) {
	    		response = {"data":{"mobile":"","userId":28,"username":"微信用户jp895zmvcjbd","password":"","gender":2,"birthday":null,"register_time":1539068898000,"last_login_time":1541727929000,"last_login_ip":"8.8.8.8","user_level_id":null,"nickname":"张然","register_ip":"8.8.8.8","avatar":"https://wx.qlogo.cn/mmopen/vi_32/WV9ibUvvrphP9auBTSl3HLpcBmZcWyR3IkXibN4q0dkxiakUfR8TLZfibzokH1tOcUial8yaa6SM5cxkmZAJQjZQpBw/132","weixin_openid":"oohoZ4zvNYZ6_S3ghj4Zy1xX4fhI"}};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
    			that.mobileInfo = response.data;
    			console.log(that.mobileInfo);
    			if (response.data.mobile == null || response.data.mobile == '') {
		            that.bindResult = true;
		        }else{
		        	that.bindResult = false;
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
  		countDownPassCode(){
  			if(this.telephone.length < 11){
  				Toast('手机号码格式不正确');
  				return false;
  			}else if (!this.telephone.match(/^1[3-9][0-9]\d{8}$/)) {
            	Toast('手机号码格式不正确');
            	return false;
  			}else{
  				var that = this;
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
	                    var i = 60;
	                    var intervalId = setInterval(function () {
	                        i--
	                        if (i <= 0) {
                                that.disableGetMobileCode = false,
                                that.disableSubmitMobileCode = false,
                                that.getCodeButtonText = '获取验证码'
	                            clearInterval(intervalId);
	                        } else {
                                that.getCodeButtonText = i+'s',
                                that.disableGetMobileCode = true,
                                that.disableSubmitMobileCode = false
	                        }
	                    }, 1000);
			    	}
				})
  			}
  		},
  		submitForm(){
  			if (!(this.teleyzm.length === 4)) {
            	return false;
  			}else{
  				var that = this;
  				this.$http({
			        method: 'post',
			        url:that.$url+ 'user/bindMobile',
			        params:{ mobile_code: this.teleyzm, mobile: this.telephone }
		    	}).then(function (response) {
		    		response = {"errno":0,"data":"短信发送成功","errmsg":"执行成功"};
		    		if(response.data.data.errno == '0'){
						that.$router.push('/')	
					}else{
						Toast('绑定失败');
					}
	             })
        	}	
       }
 	 }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--因加scoped会picker样式不可用-->
<style scoped>
	/*已绑定显示*/
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
	height: 0.9rem;
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
	/*未绑定显示 */
.userinfo {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: .25rem;
  background: linear-gradient(to bottom, #FCD33D 0%, #FFF6D7 100%);
}

.userinfo-avatar {
  width: 1.00rem;
  height: 1.00rem;
  border-radius: 50%;
  margin-top: .20rem;
  margin-bottom: .25rem;
}

.userinfo-nickname {
  color: #272727;
  font-size: .28rem;
  line-height: .40rem;
}

.separate {
  height: .18rem;
  background-color: #f2f2f2;
}

.zichan {
  display: flex;
  flex-direction: column;
}

.zichan .first-line {
  font-size: .27rem;
  margin-left: .20rem;
  margin-top: .20rem;
  margin-bottom: .20rem;
  background-color:#F7F7F7;
  text-align: left;
}

.zichan .second-line {
  padding-top: .15rem;
  padding-bottom: .15rem;
  border-top: 1rem solid #F6F6F6;
  border-bottom: 1rem solid #F6F6F6;
  height: max-content;
}

.long-div {
  display: flex;
  flex-direction: row;
  width: 18.50rem;
}

.zichan .second-line .item {
  width: 5.60rem;
  height: 2.64rem;
  margin-left: .20rem;
}

.zichan .second-line .item .image{
  height: 2.64rem;
}

.zichan .second-line .desc {
  position: relative;
  top: -2.62rem;
  left: 0;
  width: 5.60rem;
  height: 2.64rem;
  background-color: rgba(0,0,0,0.3);
  border-radius: .05rem;
  text-align: center;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: .25rem;
}

.desc-line {
  margin-top: .05rem;
  margin-bottom: .05rem;
}

.desc-line.asset-count {
  font-size: .32rem;
  color: #FFC800;
}

.slide-img {
  border-radius: .05rem;
  width: 5.60rem;
  height: 2.64rem;
}

/* 绑定手机号的两个form */
.login-title {
  margin: .20rem 0 .35rem;
  text-align: center;
  font-size: .30rem;
}

.login {
  font-size: .32rem;
}

.login .first-line {
  height: .80rem;
  border: 1px solid rgb(217, 217, 217);
  border-radius: .1rem;
  -webkit-border-radius: .1rem;
  width: 6.00rem;
  margin:0 auto;
  margin-bottom: .20rem;
  text-align: left;
  overflow: hidden;
}

.login .first-line input {
  padding-left: .20rem;
  height: .80rem;
  background-color:#F7F7F7;
  font-size:.28rem
}

.login .second-line {
  height: .80rem;
  width: 6.00rem;
  margin:0 auto;
  margin-bottom: .50rem;
  text-align: left;
}

.login .second-line input {
  height: .80rem;
  width: 3.50rem;
  margin-right: .20rem;
  border: 1px solid rgb(217, 217, 217);
  padding-left: .20rem;
  border-radius: .1rem;
  background-color:#F7F7F7;
  font-size:.28rem
}

.login .second-line button {
  text-align: center;
  height: .84rem;
  line-height: .84rem;
  width: 1.95rem;
  font-size: .30rem;
  border-radius: .1rem;
  -webkit-border-radius: .1rem;
  background-color: #FFC800;
}

.login .password-second-line {
  height: .80rem;
  display: flex;
  width: 6.00rem;
  margin-bottom: .50rem;
}

.login .password-second-line input {
  height: .80rem;
  width: 6.00rem;
  border: 1px solid rgb(217, 217, 217);
  padding-left: .20rem;
  border-radius: 5px;
}

.third-line {
  margin-left: auto;
  margin-right: auto;
  width: 6.00rem;
}

.third-line button {
  	height: .80rem;
    font-size: .32rem;
    background-color: #FFC800;
    display: block;
    text-align: center;
    width: 6.00rem;
    border-radius: .1rem;
}

.login-type {
  width: 6.00rem;
  margin: .60rem auto 0 auto;
  padding: .20rem 0 .20rem;
  font-size: .32rem;
  text-align: right;
  text-decoration: underline;
}
/*************/
.profile-button-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: .10rem;
  margin-bottom: .10rem;
}

.profile-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 1.88rem;
  height: 1.30rem;
  outline: none;
}

.profile-button image {
  width: .36rem;
  height: .36rem;
}

.profile-button text {
  font-size: .25rem;
}
.yesBind p{
  font-size:.29rem;
  color:#333;
  display:block;
  padding-top:.11rem;
}
.yesBind image{
  width:.56rem;
  height:.56rem;
  border-radius:50%;
  
}
.yesBind .titleTop{
  padding:0 .30rem;
  background-color:#fff;
  margin-top:.30rem;
  margin-bottom:.05rem;
}
.yesBind .titleBottom{
  background-color:#fff;
  display: flex;
  justify-content:space-between;
  padding:.30rem;
}
.yesBind .titleTop div text{
  font-size:.26rem;
  padding-top:.20rem;
  padding-bottom: .30rem;
}
.yesBind .titleTop div{
  display: flex;
  justify-content:space-between;
}
.yesBind .logout{
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
.yesBind .col5e5e5e{
  color:#5e5e5e;
}
</style>
