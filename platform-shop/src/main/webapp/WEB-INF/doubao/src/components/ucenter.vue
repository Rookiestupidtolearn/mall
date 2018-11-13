<template>
  <div class="hello">
   <div class="viewTop">
   		<img class="userinfo-avatar" src="https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/150547696d798c.png"/>
   		<p class="userinfo-nickname">Hi,游客</p>
   </div>
   
   <div class="user-menu">
	   <div class="item">
	      <router-link to="/views/ucenter/order" class="a">
	        <p class="icon order"></p>
	        <p class="txt">我的订单</p>
	      </router-link >
	    </div>
	     <div class="item">
	      <router-link  to="/views/ucenter/coupon" class="a">
	        <p class="icon coupon"></p>
	        <p class="txt">优惠券</p>
	      </router-link >
	    </div>
	      <div class="item no-border">
	      <router-link  to="/views/ucenter/collect" class="a">
	        <p class="icon address"></p>
	        <p class="txt">我的收藏</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/views/ucenter/footprint" class="a">
	        <p class="icon security"></p>
	        <p class="txt">我的足迹</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/views/ucenter/addressList" class="a">
	        <p class="icon address"></p>
	        <p class="txt">地址管理</p>
	      </router-link >
	    </div>
	      <div class="item no-border">
	      <router-link  to="/views/ucenter/accountSecurity" class="a">
	        <p class="icon security"></p>
	        <p class="txt">账号安全</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/ucenter" class="a">
	        <p class="icon kefu"></p>
	        <p class="txt">联系客服</p>
	      </router-link >
	    </div>
	      <div class="item">
	      <router-link  to="/views/ucenter/helpCenter" class="a">
	        <p class="icon help"></p>
	        <p class="txt">帮助中心</p>
	      </router-link >
	    </div>
	    <div class="item no-border">
	      <router-link  to="/views/ucenter/feedback" class="a">
	        <p class="icon feedback"></p>
	        <p class="txt">意见反馈</p>
	      </router-link >
	    </div>
	    <div class="item item-bottom">
	      <router-link  to="/views/ucenter/mobile" class="a">
	        <p class="icon phone"></p>
	        <p class="txt">绑定手机</p>
	      </router-link >
     </div>
    </div>
   
   <!--底部导航栏-->
		<mt-tabbar v-model="selected">
		  <mt-tab-item id="tab1">
		    <img slot="icon" src="../../static/images/ic_menu_choice_nor.png">
		    首页
		  </mt-tab-item>
		  <mt-tab-item id="tab2">
		    <img slot="icon" src="../../static/images/ic_menu_sort_nor.png">
		    分类
		  </mt-tab-item>
		  <mt-tab-item id="tab3">
		    <img slot="icon" src="../../static/images/ic_menu_shoping_nor.png">
		   购物车
		  </mt-tab-item>
		  <mt-tab-item id="tab4">
		    <img slot="icon" src="../../static/images/ic_menu_me_nor.png">
		    我的
		  </mt-tab-item>
		</mt-tabbar>
  </div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	
export default {
  name: 'ucenter',
  data () {
    return {
      category:[],
      selected:'tab4'
    }
  },
   watch: {
    selected: function (val, oldVal) {
    	console.log(val,oldVal)
      // 这里就可以通过 val 的值变更来确定去向
      switch(val){
        case 'tab1':
          this.$router.push('/');
        break;
        case 'tab2':
          this.$router.push('/classification');
        break;
        case 'tab3':
          this.$router.push('/shoppingcar');
        break;
        case 'tab4':
          this.$router.push('/ucenter');
        break;
      }
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
        method: 'post',
        url:that.$url+ 'user/userAccount',
    	}).then(function (response) {
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
//  			MessageBox({
//					  title: ' ',
//					  message: '请先登录 ',
//					  showCancelButton: true
//					});
//  			return false;
    		}else{
    			
    		}
		  })
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
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
