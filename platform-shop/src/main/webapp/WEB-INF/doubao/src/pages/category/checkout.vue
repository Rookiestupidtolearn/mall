<template>
 	<div class="container">
		 <div class="address-box">
	        <div class="address-item" bindtap="selectAddress" v-if="checkedAddress.id > 0">
	            <div class="l">
	                <text class="name">{{checkedAddress.userName}}</text>
	                <text class="default" v-if="checkedAddress.is_default === 1">默认</text>
	            </div>
	            <!--<div class="m">
	                <text class="mobile">{{checkedAddress.telNumber}}</text>
	                <text class="address">{{checkedAddress.full_region+checkedAddress.detailInfo}}</text>
	            </div>
	            <div class="r">
	                <image src="/static/images/address_right.png"></image>
	            </div>
	        </div>
	        <div class="address-item address-empty" bindtap="addAddress" wx:if="{{checkedAddress.id <= 0}}">
	            <div class="m">
	               还没有收货地址，去添加
	            </div>
	            <div class="r">
	                <image src="/static/images/address_right.png"></image>
	            </div>-->
	        </div>
	    </div>
  	</div>
</template>

<script>
	import { Toast } from 'mint-ui';
		
	export default {
	  name: 'checkout',
	  data () {
	    return {
	    	checkedGoodsList: [],
		    checkedAddress: {},
		    checkedCoupon: [],
		    couponList: [],
		    goodsTotalPrice: 0.00, //商品总价
		    freightPrice: 0.00,    //快递费
		    couponPrice: 0.00,     //优惠券的价格
		    orderTotalPrice: 0.00,  //订单总价
		    actualPrice: 0.00,     //实际需要支付的总价
		    addressId: 0,
		    couponId: 0,
		    isBuy: false,
		    couponDesc: '',
		    couponCode: '',
		    buyType: '',
		    isBuyType:''
	    }
	  },
	  mounted(){
  			this.isBuyType = this.$route.query.isBuy
		    // 页面初始化 options为页面跳转所带来的参数
		    console.log(this.isBuyType);
		    if (this.$route.query.isBuy!="false") {
		     	 this.isBuy = this.$route.query.isBuy
		    }
    		this.buyType = this.isBuy?'buy':'cart'
		    //每次重新加载界面，清空数据
//		    app.globalData.userCoupon = 'NO_USE_COUPON'
//		    app.globalData.courseCouponCode = {}
	  		//关联
//	    	that.$http({
//	    		method: 'post',
//		        url:that.$url+ 'goods/related',
//		        params:{id:that.idm}
//	    	}).then(function (response) {
//		          that.relatedGoods = response.data.data.goodsList;
//		    });
	    	
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--v-html  的标签 加样式 用 >>>-->
<style scoped>
.address-box{
    width: 100%;
    height: 1.6655rem;
    background: url('http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/address-bg-bd30f2bfeb.png') 0 0 repeat-x;
    background-size: .625rem .105rem;
    margin-bottom: .20rem;
    padding-top: .105rem;
}

.address-item{
    display: flex;
    height: 1.5555rem;
    background: #fff;
    padding: .416rem 0 .416rem .3125rem;
}

.address-item.address-empty{
  line-height: .75rem;
  text-align: center;
}

.address-box .l{
    width: 1.55rem;
    height: 100%;
}

.address-box .l .name{
    margin-left: .0625rem;
    margin-top: -.0725rem;
    display: block;
    width: 1.55rem;
    height: .43rem;
    line-height: .43rem;
    font-size: .30rem;
    color: #333;
    margin-bottom: .05rem;

}

.address-box .l .default{
    margin-left: .0625rem;
    display: block;
    width: .62rem;
    height: .33rem;
    border-radius: 5rem;
    border: 1px solid #b4282d;
    font-size: .205rem;
    text-align: center;
    line-height: .29rem;
    color: #b4282d;
}

.address-box .m{
    flex: 1;
    height: .7225rem;
    color: #999;
}

.address-box .mobile{
    display: block;
    height: .29rem;
    line-height: .29rem;
    margin-bottom: .0625rem;
    font-size: .30rem;
    color:#333;
}

.address-box .address{
    display: block;
    height: .375rem;
    line-height: .375rem;
    font-size: .25rem;
    color:#666;
}

.address-box .r{
    width: .77rem;
    height: .77rem;
    display: flex;
    justify-content: center;
    align-items: center;
}

.address-box .r image{
    width: .52078rem;
    height: .52078rem;
}

.coupon-box{
    width: 100%;
    height: auto;
    overflow: hidden;
    background: #fff;
}

.coupon-box .coupon-item{
    width: 100%;
    height: 1.083rem;
    overflow: hidden;
    background: #fff;
    display: flex;
    padding-left: .3125rem;
}

.coupon-box .l{
    flex: 1;
    height: .43rem;
    line-height: .43rem;
    padding-top: .35rem;
}

.coupon-box .l .name{
    float: left;
    font-size: .30rem;
    color: #666;
}

.coupon-box .l .txt{
    float: right;
    font-size: .30rem;
    color: #666;
}

.coupon-box .r{
    margin-top: .155rem;
    width: 77rem;
    height: 77rem;
    display: flex;
    justify-content: center;
    align-items: center;
}

.coupon-box .r image{
    width: .52078rem;
    height: .52078rem;
}

.order-box{
    margin-top: 20rem;
    width: 100%;
    height: auto;
    overflow: hidden;
    background: #fff;
}

.order-box .order-item{
    height: 1.043rem;
    overflow: hidden;
    background: #fff;
    display: flex;
    margin-left: .3125rem;
    padding-right: .3125rem;
    padding-top: .26rem;
    border-bottom: 1px solid #d9d9d9;
}

.order-box .order-item .l{
    float: left;
    height: .52rem;
    width: 50%;
    line-height: .52rem;
    overflow: hidden;
}

.order-box .order-item .r{
    float: right;
    text-align: right;
    width: 50%;
    height: .52rem;
    line-height: .52rem;
    overflow: hidden;
}

.order-box .order-item.no-border{
    border-bottom: none;
}

.goods-items{
    margin-top: .20rem;
    width: 100%;
    height: auto;
    overflow: hidden;
    background: #fff;
    padding-left: .3125rem;
    margin-bottom: 1.20rem;
}

.goods-items .item{
    height: 1.92rem;
    padding-right: .3125rem;
    display: flex;
    align-items: center;
    border-bottom: 1px solid rgba(0,0,0,0.15);
}

.goods-items .item.no-border{
    border-bottom: none;
}


.goods-items .item:last-child{
    border-bottom: none;
}

.goods-items .img{
    height: 1.4583rem;
    width: 1.4583rem;
    background-color: #f4f4f4;
    margin-right: .20rem;
}

.goods-items .img image{
    height: 1.4583rem;
    width: 1.4583rem;
}

.goods-items .info{
    flex: 1;
    height: 1.4583rem;
    padding-top: 5rem;
}

.goods-items .t{
    height:  ..33rem;
    line-height: .33rem;
    margin-bottom: .10rem;
    overflow: hidden;
    font-size: .30rem;
    color: #333;
}

.goods-items .t .name{
    display: block;
    float: left;
}

.goods-items .t .number{
    display: block;
    float: right;
    text-align: right;
}

.goods-items .m {
    height:  .29rem;
    overflow: hidden;
    line-height: .29rem;
    margin-bottom: .25rem;
    font-size: .25rem;
    color: #666;
}

.goods-items .b {
    height:  .41rem;
    overflow: hidden;
    line-height: .41rem;
    font-size: 3.0rem;
    color: #333;
}

.order-total{
    position: fixed;
    left:0;
    bottom: 0;
    height: 1.00rem;
    width: 100%;
    display: flex;
}

.order-total .l{
    flex: 1;
    height: 1.00rem;
    line-height: 1.00rem;
    color: #b4282d;
    background: #fff;
    font-size: .33rem;
    padding-left: .3125rem;
    border-top: 1rem solid rgba(0,0,0,0.2);
    border-bottom: 1rem solid rgba(0,0,0,0.2);
}

.order-total .r{
    width: 2.33rem;
    height: 1.00rem;
    background: #b4282d;
    border: 1px solid #b4282d;
    line-height: 1.00rem;
    text-align: center;
    color: #fff;
    font-size: .30rem;
}
</style>
