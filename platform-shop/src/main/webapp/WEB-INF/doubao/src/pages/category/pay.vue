<template>
 	<div class="container">
 		<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  		
	    <div class="total mt88">
	      <div class="label">订单金额</div>
	      <div class="txt">{{actualPrice}}元</div>
	    </div>
	    <div class="pay-list">
	        <div class="h">请选择支付方式</div>
	        <div class="b">
	            <!--<view class="item">
	                <view class="checkbox checked"></view>
	                <view class="icon-alipay"></view>
	                <view class="name">支付宝</view>
	            </view>
	            <view class="item">
	                <view class="checkbox"></view>
	                <view class="icon-net"></view>
	                <view class="name">网易支付</view>
	            </view>-->
	            <div class="item">
	                <div class="checkbox checked"></div>
	                <image src="/static/images/wxpay.png" class="icon"></image>
	                <div class="name">微信支付</div>
	            </div>
	        </div>
	    </div>
	    <!--<div class="tips">小程序只支持微信支付，如需其它支付方式，请在网页版支付</div>-->
	
	    <div class="pay-btn" @click="startPay">确定</div>
		    
	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Toast } from 'mint-ui';
	import headbar from '@/components/headbar.vue'
		
	export default {
	  name: 'brandDetail',
	   components:{headbar},
	  data () {
	    return {
	    	headFont:'支付订单',
	    	orderId: 0,
    		actualPrice: 0.00
	    }
	  },
	  mounted(){
	  		this.orderId = this.$route.query.orderId;
	  		this.actualPrice = this.$route.query.actualPrice;
	  },
	  methods:{
	  	startPay() {
			    let that = this;
			    //品牌详情
		    	that.$http({
			        method: 'post',
			        url:that.$url+ 'pay/prepay',
			        params:{ orderId: that.data.orderId, payType: 1 }
		    	}).then(function (response) {
				 })
		  }
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.total{
	  height: 1.04rem;
	  background: #fff;
	  width: 100%;
	  line-height: 1.04rem;
	  padding-left: .30rem;
	  padding-right: .30rem;
	  font-size:.29rem;
}

.total .label{
  float: left;
}

.total .txt{
  float: right;
}

.pay-list{
    margin-top: .30rem;
    height: auto;
    width: 100%;
    overflow: hidden;
}
.pay-list .h{
    width: 100%;
    margin-left: .3125rem;
    margin-bottom: .3125rem;
 	font-size:.29rem;
}

.pay-list .item{
    height: 1.08rem;
    padding-left: .3125rem;
    background: #fff;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #f4f4f4;
    font-size:.29rem;
}

.pay-list .checkbox{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/checkbox-sed825af9d3-a6b8540d42.png) 0 -4.48rem no-repeat;
    background-size: .38rem 4.86rem;
    width: .40rem;
    height: .40rem;
    display: inline-block;
    vertical-align: middle;
    margin-right: .30rem;
}

.pay-list .checkbox.checked{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/checkbox-sed825af9d3-a6b8540d42.png) 0 -1.92rem no-repeat;
    background-size: .38rem 4.86rem;
}

.pay-list .icon-alipay{
    display: inline-block;
    vertical-align: middle;
    background-image: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/payMethod-s3c1faebee4-d754da9c65.png);
    background-repeat: no-repeat;
    background-size: .5625rem 1.89583rem;
    margin-right: .105rem;
    width: .5625rem;
    height: .5625rem;
}

.pay-list .icon-net{
    display: inline-block;
    vertical-align: middle;
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/payMethod-s3c1faebee4-d754da9c65.png) 0 -.667rem no-repeat;
    background-size: .5625rem 1.89583rem;
    margin-right: .105rem;
    width: .5625rem;
    height: .5625rem;
}

.pay-list .icon{
    display: inline-block;
    vertical-align: middle;
    margin-right: .105rem;
    width: .5625rem;
    height: .5625rem;
}

.pay-list .name{
    display: inline-block;
    vertical-align: middle;
    height: .5625rem;
    line-height: .5625rem;
}

.pay-btn{
    position: fixed;
    left: 0;
    bottom: 0;
    height: 1.00rem;
    width: 100%;
    text-align: center;
    line-height: 1.00rem;
    background: #b4282d;
    color: #fff;
    font-size: .30rem;
}

.tips{
  height: .40rem;
  width: 100%;
  font-size: .24rem;
  color: #999;
  line-height: .40rem;
  padding-left: .30rem;
  padding-right: .30rem;
}
</style>
