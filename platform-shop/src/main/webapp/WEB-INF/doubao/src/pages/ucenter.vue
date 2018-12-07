<template>
  <div class="hello">
  	<!--公用头部-->
  	<!--<headbar :headFont = "headFont"></headbar>-->
  	
   <div class="viewTop ">
   		<img class="userinfo-avatar" :src="avatarImg" @click="gologin"/>
   		<p class="userinfo-nickname">{{userName}}</p>
   		<router-link class="userinfo-availMoney" :to="availUrl" :style="{display:[availResult?'block':'none']}" tag="div">
	      <p class="userinfo-title">平台币可用余额</p>
	      <p class="money">{{ availMoney }}</p>
	    </router-link>
   </div>
   <div class="order">
   		<div class="myorder">
   			<div class="left">我的订单</div>
   			<router-link class="right"  to="/pages/ucenter/order">查看全部></router-link>
   		</div>
   		<div class="orderList">
   			<ul>
   				<router-link to="/pages/ucenter/order" tag="li">
   					<img src="../../static/images/order1.png" alt="" />
   					<p>待付款</p>
   				</router-link>
   				<router-link to="/pages/ucenter/order" tag="li">
   					<img src="../../static/images/order2.png" alt="" />
   					<p>待发货</p>
   				</router-link>
   				<router-link to="/pages/ucenter/order" tag="li">
   					<img src="../../static/images/order3.png" alt="" />
   					<p>待收货</p>
   				</router-link>
   				<router-link to="/pages/ucenter/order" tag="li">
   					<img src="../../static/images/order4.png" alt="" />
   					<p>退款/售后</p>
   				</router-link>
   			</ul>
   		</div>
   </div>
    <div class="order ordermt">
   		<div class="myorder">
   			<div class="left">我的服务</div>
   		</div>
   <div class="user-menu">
	   <div class="item">
	      <router-link to="/pages/ucenter/account" class="a">
	        <img src="../../static/images/fuwu1.png" alt=""  class="icon security"/>
	        <p class="txt">账户中心</p>
	      </router-link >
	    </div>
	      <div class="item no-border">
	      <router-link  to="/pages/ucenter/collect" class="a">
	        <img src="../../static/images/fuwu2.png" alt=""  class="icon gift"/>
	        <p class="txt">我的收藏</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/pages/ucenter/footprint" class="a">
	        <img src="../../static/images/fuwu3.png" alt=""  class="icon address"/>
	        <p class="txt">我的浏览</p>
	      </router-link >
	    </div>
	     <div class="item">
		      <router-link  to="/pages/ucenter/userservice" class="a">
			       <img src="../../static/images/fuwu4.png" alt=""  class="icon kefu"/>
			        <p class="txt">客户服务</p>
		      </router-link >
	    </div>
	      <!--<div class="item">
	      <router-link  to="/pages/ucenter/helpCenter" class="a">
	        <p class="icon help"></p>
	        <p class="txt">消息中心</p>
	      </router-link >
	    </div>-->
	     <div class="item">
	      <router-link  to="/pages/ucenter/coupon" class="a">
	        <p class="icon coupon"></p>
	        <p class="txt">优惠券</p>
	      </router-link >
	    </div>
	    <!--<div class="item item-bottom">
	      <router-link  to="/pages/ucenter/mobile" class="a">
	        <p class="icon phone"></p>
	        <p class="txt">绑定手机</p>
	      </router-link >
     </div>-->
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
  	var getCookieInfo = that.$cookie.getCookie('userInfo');
  	if(getCookieInfo != ""){
	  		var userInfo =  JSON.parse(getCookieInfo);
	  		if(userInfo !== null){
				this.availResult = true;
				if(userInfo.avatar == null || userInfo.avatar == ""){
					this.avatarImg = 'https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/150547696d798c.png'
				}else{
					this.avatarImg = userInfo.avatar;
				}
		    this.userName = userInfo.nickname;
		    this.availMoney = userInfo.availMoney;
		    that.$http({
			        method: 'post',
			        url:that.$url+ 'user/userAccount',
		    	}).then(function (res) {
						that.availMoney = res.data.data;
				})
	    }
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
	.order{
		font-size:.29rem;
		background: #fff;
	}
	.ordermt{
		margin-top:.16rem;
	}
	.order .myorder {
		padding:.24rem .24rem .24rem 0rem;
		overflow: hidden;
		margin-left: .24rem;
		border-bottom: 1px solid #eeeeee;
	}
	.order .myorder .left{
		float:left;
	}
	.order .myorder .right{
		float:right;
		color:#a1a1a1
	}
	.orderList ul{
		overflow: hidden;
		padding: .3rem 0;
	}
	.orderList ul li{
		float:left;
		width:25%;
	}
	.orderList ul li img{
		width:.48rem;
	}
	.orderList ul li p{
		margin-top:.18rem;
	}
	.userinfo-availMoney{
	  font-size:.29rem;
	  text-align: center;
	  position:absolute;
	  bottom:0;
	  width:7.0215rem;
	  background-color:#e08438;
	  overflow: hidden;
	  padding: .24rem;
	}
	.userinfo-availMoney .userinfo-title{
	  float:left;
	  color:#fff;
	}
	.userinfo-availMoney .money{
	  float:right;
	  color:#fff;
	  font-size:.29rem;
	}
	.user-menu{
    width: 100%;
    height: auto;
    overflow: hidden;
    background: #fff;
    padding: .25rem 0;
	}
	.user-menu .item{
    float: left;
    width: 33.33333%;
    height: 1.875rem;
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
    margin-bottom: .16rem;
    width:.55rem;
    height:.55rem;
}

.user-menu .icon.coupon{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/ucenter-sdf6a55ee56-f2c2b9c2f0.png) 0 -.58rem no-repeat;
    background-size: .52803rem;
}

.user-menu .icon.gift{
    width:.55rem;
}

.user-menu .icon.address{
     width:.5rem;
}

.user-menu .icon.security{
    width:.52rem;
}

.user-menu .icon.kefu{
  width:.44rem;
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
}

.viewTop {
	position:relative;
	background: -webkit-linear-gradient(top, #fdd052, #f76c1d);
	padding: .3rem 0 1.3rem 0;
	width:7.50rem;
	border-radius:.1rem;
}

</style>
