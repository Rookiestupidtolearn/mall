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
		  <div class="confirm" bindtap="confirm">确认</view>
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
	    }
	
	    let that = this;
	    //调用接口
	    that.$http({
	        method: 'post',
	        url:that.$url+ 'user/userInfo',
	        data:{
	        	username: that.name,
	      		idcard: that.idcard
	        }
    	}).then(function (res) {
	      console.log(res);
	      if(res.code == '500'){
	        util.showErrorToast(res.msg);
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
}
.confirm{
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
.titleBottom text{
  width:1.80rem;
  padding-top:.05rem;
}
.titleBottom input{
  width:4.00rem;
  text-align: left;
  vertical-align: text-top;
  font-size:.29rem;
  /* border:1rem solid #b1b1b1; */
  padding-left:.15rem;
}
.col5e5e5e{
  color:#5e5e5e;
}
</style>
