<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
  		<div class="">
 		<div class='titleTop'>
		    <p>绑定账号</p>
		    <div style="overflow: hidden;">
		      <span class='col5e5e5e' v-if="accountSecurity.nickname" :data-c="accountSecurity.nickname">昵称:{{accountSecurity.nickname}}</span>
		      <span class='col5e5e5e' v-else></span>
		      <img  class="image" v-if="accountSecurity.avatar" :src="accountSecurity.avatar"/>
		      <img  class="image" v-else src="https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/150547696d798c.png"/>
		    </div>
		  </div>
		   <div class="titleBottom">
			    <span>姓名</span>
			    <span class='mobile'  data-c="cee" v-if="name">{{name}}</span>
				<router-link to="/pages/ucenter/namecard/" class='mobile'  v-else >去认证</router-link>
		  </div>
		   <div class="titleBottom">
		    <span>身份证件号</span>
		    <span class='mobile'  data-c="cee" v-if="idCard">{{idCard}}</span>
			<router-link to="/pages/ucenter/namecard/" class='mobile'  v-else >去认证</router-link>
		  </div>
		  <div class="titleBottom">
		    <span>绑定手机号</span>
		    <span class='mobile'  data-c="cee" v-if="telephone">{{telephone}}</span>
			<router-link to="/pages/ucenter/mobile/" class='mobile'  v-else >去绑定</router-link>
		  </div>
		  <div class="logout" @click="exitLogin">退出登录</div>
	  </div>
	  <returnhome :scrollshow = "scrollshow"></returnhome>
 	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import returnhome from '@/components/returnHome';
//	import headbar from '@/components/headbar.vue';	
		
export default {
  name: 'accountSecurity',
components:{returnhome},
  data () {
    return {
//		headFont:'账户安全',
		scrollshow:true,
		name:'',
		idCard:'',
    	accountSecurity:[],
    	telephone:''
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'user/userInfo.options',
	        headers:{
	        	'Content-Type':'application/json'
	        },
	        data:{typeId:0}
    	}).then(function (response) {
    		if(response.data.errno != 401){
    			that.accountSecurity = response.data.data;
    			if(response.data.data.mobile == null || response.data.data.mobile ==''){
    				that.telephone = response.data.data.mobile;
				}else{
    				that.telephone = that.validateMobile(response.data.data.mobile);
				}
				
				if(response.data.data.username == null || response.data.data.username ==''){
    				that.name = response.data.data.username;
				}else{
    				that.name = that.validatename(response.data.data.username);
				}
				
				if(response.data.data.idcard == null || response.data.data.idcard ==''){
    				that.idCard = response.data.data.idcard;
				}else{
    				that.idCard = that.validateidCard(response.data.data.idcard);
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
	validatename(name) {
	    /*姓名加密处理*/
	    var last = name.substr(name.length-1);
	    var finalname = '*' + last;
	    return finalname;
	  },
	  validateidCard(idcard) {
	    /*身份证号加密处理*/
	    var first = idcard.substr(0, 1);
	    var last = idcard.substr(idcard.length-1);
	    var finalidcard = first + '***********' + last;
	    return finalidcard;
	  },
  	exitLogin(){
  		let that = this;
		MessageBox({
		  title: ' ',
		  message: '确定退出登录？ ',
		  showCancelButton: true
		},function(params){
			if(params == 'confirm'){
				/*清除cookie*/
				that.$cookie.delCookie('userId');
    			that.$cookie.delCookie('userInfo');
    			that.$cookie.delCookie('token');
    			/*分类记住用户选择*/
    			that.$cookie.delCookie('eventId');
    			that.$cookie.delCookie('inviteCount');	
    			/*记住用记搜索商品*/
    			that.$cookie.delCookie('search');
    			that.$cookie.delCookie('searchKey');
				that.$router.push('/pages/ucenter');
			}
		});
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
  /*background: -webkit-linear-gradient(left, #FFB559, #F76B1C);
  background: linear-gradient(left, #FFB559, #F76B1C);
  color:#fff;*/
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
