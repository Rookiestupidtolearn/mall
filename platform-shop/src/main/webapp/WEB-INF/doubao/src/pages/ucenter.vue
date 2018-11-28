<template>
  <div class="hello">
  	<!--公用头部-->
  	<!--<headbar :headFont = "headFont"></headbar>-->
  	
   <div class="viewTop ">
   		<img class="userinfo-avatar" :src="avatarImg" @click="gologin"/>
   		<p class="userinfo-nickname">{{userName}}</p>
   		<router-link class="userinfo-availMoney" :to="availUrl" :style="{display:[availResult?'block':'none']}">
	      <p class="userinfo-title">平台币可用余额</p>
	      <p class="money">{{ availMoney }}</p>
	    </router-link>
   </div>
   
   <div class="user-menu">
	   <div class="item">
	      <router-link to="/pages/ucenter/order" class="a">
	        <p class="icon order"></p>
	        <p class="txt">我的订单</p>
	      </router-link >
	    </div>
	     <div class="item">
	      <router-link  to="/pages/ucenter/coupon" class="a">
	        <p class="icon coupon"></p>
	        <p class="txt">优惠券</p>
	      </router-link >
	    </div>
	      <div class="item no-border">
	      <router-link  to="/pages/ucenter/collect" class="a">
	        <p class="icon address"></p>
	        <p class="txt">我的收藏</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/pages/ucenter/footprint" class="a">
	        <p class="icon security"></p>
	        <p class="txt">我的足迹</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/pages/ucenter/addressList" class="a">
	        <p class="icon address"></p>
	        <p class="txt">地址管理</p>
	      </router-link >
	    </div>
	      <div class="item no-border">
	      <router-link  to="/pages/ucenter/accountSecurity" class="a">
	        <p class="icon security"></p>
	        <p class="txt">账号安全</p>
	      </router-link >
	    </div>
	      <div class="item">
	      	<div class="a">
	      <!--<router-link  to="/ucenter" class="a">-->
		        <p class="icon kefu"></p>
		        <p class="txt">联系客服</p>
	        </div>
	      <!--</router-link >-->
	    </div>
	      <div class="item">
	      <router-link  to="/pages/ucenter/helpCenter" class="a">
	        <p class="icon help"></p>
	        <p class="txt">帮助中心</p>
	      </router-link >
	    </div>
	    <div class="item no-border">
	      <router-link  to="/pages/ucenter/feedback" class="a">
	        <p class="icon feedback"></p>
	        <p class="txt">意见反馈</p>
	      </router-link >
	    </div>
	    <div class="item item-bottom">
	      <router-link  to="/pages/ucenter/mobile" class="a">
	        <p class="icon phone"></p>
	        <p class="txt">绑定手机</p>
	      </router-link >
     </div>
    </div>
   <!--公用底部导航-->
  	<tabbar :selected="selected" :tabs='tabs'></tabbar> 
  </div>
</template>

<script>
	import tabbar from '@/components/tabbar.vue'
//	import headbar from '@/components/headbar.vue'
	
export default {
	components: {tabbar },
  name: 'ucenter',
  data () {
    return {
    	avatarImg:'https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/150547696d798c.png',
      category:[],
      userName:'Hi,游客',
      availResult:false,
      availUrl:'/pages/ucenter/amountMoney',
      availMoney:'',
//    headFont:'个人中心',
     selected:"ucenter",
   	 tabs:[require("../../static/images/ic_menu_choice_nor.png"),require("../../static/images/ic_menu_sort_nor.png"),
    	require("../../static/images/ic_menu_shoping_nor.png"),require("../../static/images/ic_menu_me_pressed.png")],
    }
  },
  mounted(){
  	let that = this;
    var userInfo =  JSON.parse(that.$cookie.getCookie('userInfo'));
    if(userInfo !== null){
			this.availResult = true;
    	this.avatarImg = userInfo.avatar;
	    this.userName = userInfo.nickname;
	    this.availMoney = userInfo.availMoney;
	    that.$http({
		        method: 'post',
		        url:that.$url+ 'user/userAccount',
	    	}).then(function (res) {
					that.availMoney = res.data.data;
			})
    }
    
  },
  methods:{
  	gologin(){
  		let that = this;
  		that.$http({
		        method: 'post',
		        url:that.$url+ 'user/userAccount',
	    	}).then(function (res) {
					that.availMoney = res.data.data;
			})
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.userinfo-availMoney{
	  font-size:.29rem;
	  text-align: center;
	}
	.userinfo-availMoney .userinfo-title{
	  display:block;
	  color:#fff;
	  margin-top:.10rem;
	}
	.userinfo-availMoney .money{
	  display:block;
	  margin-top:.20rem;
	  color:#fff;
	  font-size:.36rem;
	}
	.user-menu{
    width: 100%;
    height: auto;
    overflow: hidden;
    background: #fff;
    margin-top: .25rem;
	}
	.user-menu .item{
    float: left;
    width: 33.33333%;
    height: 1.875rem;
    border-right: 1px solid rgba(0,0,0,.15);
    border-bottom: 1px solid rgba(0,0,0,.15);
    text-align: center;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
}
.user-menu .item .a{
  display: flex;
  width: 100%;
  height: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.user-menu .item.no-border {
    border-right: 0;
}

.user-menu .item.item-bottom {
    border-bottom: none;
}
.user-menu .txt{
    display: block;
    height: .24rem;
    width: 100%;
    font-size: .24rem;
    color:#333;
}

.user-menu .icon{
    margin: 0 auto;
    display: block;
    height: .52803rem;
    width: .52803rem;
    margin-bottom: .16rem;
}
.user-menu .icon.order{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -4.4rem no-repeat;
    background-size: .52803rem;
}

.user-menu .icon.coupon{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -.58rem no-repeat;
    background-size: .52803rem;
}

.user-menu .icon.gift{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -1.875rem no-repeat;
        background-size: .52803rem;
}

.user-menu .icon.address{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 0 no-repeat;
        background-size: .52803rem;
}

.user-menu .icon.security{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -5.1rem no-repeat;
        background-size: .52803rem;
}

.user-menu .icon.kefu{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -3.125rem no-repeat;
        background-size: .52803rem;
}

.user-menu .icon.help{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -2.52rem no-repeat;
        background-size: .52803rem;
}

.user-menu .icon.feedback{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -1.27rem no-repeat;
        background-size: .52803rem;
}
.user-menu .icon.phone{
    background: url(https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/15011540ebe21.png) no-repeat;
        background-size: .52803rem;
}
	.userinfo-avatar {
	width:1.60rem;
	height:1.60rem;
	margin:.20rem .20rem 0 .20rem;
	border-radius:50%;
}
.userinfo-nickname {
	color:#FFF;
	font-size: .29rem;
	margin-top: .25rem;
}

.viewTop {
	display:flex;
	flex-direction:column;
	padding:.30rem 0 .40rem 0;
	align-items:center;
	background:#333;
	width:7.50rem;
	border-radius:.1rem;
}

</style>
