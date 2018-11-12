<template>
  <div class="shoppingcar">
  	<div class="service-policy">
  		<span class="item">30天无忧退货</span>
  		<span class="item">48小时快速退款</span>
  		<span class="item">免邮费</span>
  	</div>
  	
  	<div class="cart-view">
  		<div class="list">
  			<div class="group-item"></div>
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
export default {
  name: 'shoppingcar',
  data () {
    return {
			selected:'tab3'
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
//	var that = this;   
//	//index
//		that.$http({
//      method: 'post',
//      url: that.$url+'cart/index',
//  	}).then(function (response) {
//		    that.cartList = response.data.data.cartList,
//		    that.cartTotal = response.data.data.cartTotal,
//		    that.couponInfoList = response.data.data.couponInfoList
//		  })
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style scoped>
	.cart-view{
		width:100%;
		height:auto;
		overflow:hidden;
	}
	.cart-view .list {
		height:auto;
		width:100%;
		overflow:hidden;
		margin-bottom:1.20rem;
}
.cart-view .group-item {
		height:auto;
		width:100%;
		background:#fff;
		margin-bottom:.18rem;
}
.cart-view .item {
	height:1.64rem;
	width:100%;
	overflow:hidden;
}
.cart-view .item .checkbox.checked {
	background:url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-checked-822e54472a.png) no-repeat center center;
	background-size:.34rem;
}
.cart-view .item .checkbox {
   float:left;
	height:.34rem;
	width:.34rem;
	padding:.80rem .18rem .80rem .56rem;
	background:url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-0e09baa37e.png) no-repeat center center;
	background-size:.34rem;
}
.cart-view .item .cart-goods{
    float: right;
    height: 1.64rem;
    width: 6.72rem;
    border-bottom: 1px solid #f4f4f4;
    position:relative;
}
.cart-view .item .info{
    float: left;
    height:1.25rem;
    width: 6.53rem;
    margin: 1.95rem .26rem .195rem 0;
}
.midHover {
		float:left;
		height:1.25rem;
}
.cart-view .item .img {
		float:left;
		height:1.25rem;
		width:1.25rem;
		background:#f4f4f4;
		margin:0 .18rem 0 0;
}
.cart-view .item .t {
		margin:.08rem 0;
		height:.28rem;
		font-size:.25rem;
		color:#333;
		overflow:hidden;
}
.cart-view .item .attr {
		margin-bottom:..17rem;
		height:.24rem;
		line-height:.24rem;
		font-size:.22rem;
		color:#666;
		overflow:hidden;
}
.cart-view .item .price {
	float:left;
}
.cart-view .item .t {
		margin:.08rem 0;
		height:.28rem;
		font-size:.25rem;
		color:#333;
		overflow:hidden;
}
.cart-view .item .b {
		height:.28rem;
		line-height:.28rem;
		font-size:.25rem;
		color:#333;
		overflow:hidden;
}
	.service-policy {
	width:7rem;
	height:.73rem;
	background:#f4f4f4;
	display: flex;
	justify-content: space-between;
	margin:0 auto;
}
.service-policy .item {
	background:url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/servicePolicyRed-518d32d74b.png) 0 center no-repeat;
	background-size:.10rem;
	padding-left:.15rem;
	font-size:.25rem;
	color:#666;
	display: flex;
	align-items: center;
}

</style>
