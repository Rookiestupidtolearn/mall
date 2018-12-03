<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
  		<!--pages/ucenter/namecardSecurity/namecard.wxml-->
		  <div class="titleBottom">
			    <span>姓名</span>
			    <input type='text' placeholder="请输入用户姓名" maxlength="5"  v-model="name"></input>
		  </div>
		  <div class="titleBottom">
		    	<span>身份证件号</span>
		   		 <input type='idcard' placeholder="请输入用户身份证号" maxlength="18" v-model="idcard" ></input>
		  </div>
		  <div class="confirm" @click="confirm">确认</div>
	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
//	import headbar from '@/components/headbar.vue';	
		
export default {
  name: 'accountSecurity',
//  components:{headbar},
  data () {
    return {
//		headFont:'账户安全',
		 name:'',
   		 idcard:''
    }
  },
  mounted(){
  	
  },
  methods:{
  	confirm() {
	    var name = this.name;
	    var idcard = this.idcard;
	    if(name == ""){
	      this.$toast('请输入用户姓名');
	      return false;
	    }
	    
	    if (idcard == ''){
	      this.$toast('请输入身份证号');
	      return false;
	    } else if (!idcard.match(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/)){
	      this.$toast('身份证号不正确');
	      return false;
	    }else if (idcard.length == 15){
	    	this.$toast('非18位证件号认证请联系客服');
	      	return false;
	    }
	
	    let that = this;
	    //调用接口
	    that.$http({
	        method: 'post',
	        url:that.$url+ 'user/bind_user_idcard.options',
	        headers:{
	        	'Content-Type':'application/json'
	        },
	        data:{
	        	username: that.name,
	      		idcard: that.idcard
	        }
    	}).then(function (res) {
	      if(res.data.code == 500){
	      	that.$toast(res.data.data);
	      }else{
	        that.$router.push('/pages/ucenter');
	      }  
	    })

  },
  	exitLogin(){
  		let that = this;
		MessageBox({
		  title: ' ',
		  message: '确定退出登录？ ',
		  showCancelButton: true
		},function(params){
			if(params == 'confirm'){
					that.$cookie.delCookie('userId');
	    			that.$cookie.delCookie('userInfo');
	    			that.$cookie.delCookie('token');
					that.$router.push('/pages/ucenter');
			}
		});
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.titleBottom{
  background-color:#fff;
  display: flex;
  padding:.30rem;
  margin-top:.05rem;
  font-size:.28rem;
}
.confirm{
  width:5.60rem;
  height:.80rem;
  line-height:.80rem;
  background: -webkit-linear-gradient(left, #FFB559, #F76B1C);
  background: linear-gradient(left, #FFB559, #F76B1C);
  color:#fff;
  border-radius:.05rem;
  text-align: center;
  margin:0 auto;
  font-weight: bold;
  font-size:.30rem;
  margin-top:2.10rem;
}
.titleBottom span{
  width:1.80rem;
  text-align: left;
}
.titleBottom input{
  width:4.00rem;
  text-align: left;
  font-size:.29rem;
  /* border:1rem solid #b1b1b1; */
  padding-left:.15rem;
}
.col5e5e5e{
  color:#5e5e5e;
}
</style>
