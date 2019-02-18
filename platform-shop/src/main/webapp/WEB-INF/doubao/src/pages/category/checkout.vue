<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
		 <div class="address-box ">
	        <div class="address-item" @click="selectAddress" v-if="checkedAddress.id > 0">
	            <div class="l">
	                <span class="name">{{checkedAddress.userName}}</span>
	                <span class="default" v-if="checkedAddress.isDefault == 1">默认</span>
	            </div>
	            <div class="m">
	                <span class="mobile">{{checkedAddress.telNumber}}</span>
	                <span class="address">{{checkedAddress.full_region+checkedAddress.detailInfo}}</span>
	            </div>
	            <div class="r">
	                <img src="../../../static/images/address_right.png"/>
	            </div>
	        </div>
	       <div class="address-item address-empty" @click="addAddress" v-if="checkedAddress.id <= 0">
	            <div class="m">
	               还没有收货地址，去添加
	            </div>
	            <div class="r">
	                <img src="../../../static/images/address_right.png"/>
	            </div>
	        </div>
	    </div>
	    
	    <div class="coupon-box" @click='tapCoupon'>
	    	 <div class="coupon-item" v-if="couponPrice==0">
	    	 	 <div class="l">
	                <!--<span class="name">请选择优惠券</span>-->
	                <span class="txt">无可用优惠券</span>
	            </div>
	            <div class="r">
	                <img src="../../../static/images/address_right.png"/>
	            </div>
	        </div>
	         <div class="coupon-item" v-else>
	            <div class="l">
	            	<!--<span class="name">优惠券金额</span>-->
	                <span class="txt">克拉抵扣券</span>
	            </div>
	            <div class="r">
	                <img src="../../../static/images/address_right.png"/>
	            </div>
	        </div>
	    </div>
	       
     <div class="order-box">
        <div class="order-item">
            <div class="l">
                <span class="name">商品合计</span>
            </div>
            <div class="r">
                <span class="txt">￥{{goodsTotalPrice}}</span>
            </div>
        </div>
        <div class="order-item">
            <div class="l">
                <span class="name">运费</span>
            </div>
            <div class="r">
                <span class="txt">￥{{freightPrice}}</span>
            </div>
        </div>
        <div class="order-item">
            <div class="l">
                <span class="name">优惠券</span>
            </div>
            <div class="r">
                <span class="txt">-￥{{couponPrice}}</span>
            </div>
        </div>
         <div class="order-item no-border">
            <div class="l" >
                <span class="name">克拉</span><i @click="showTb">?</i>
            </div>
            <div class="r">
                <span class="txt">-￥{{disCountAmount}}</span>
            </div>
            <!--后期-->
        	<!--<mt-switch v-model="switchValue" class="mtSwitch" @change="change"></mt-switch>-->
         </div>
         <div class="order-item order-tips no-border"  :style="{display : [showBin ? 'none' :'block']}">
         	<!--//showCz判断克拉是否充足-->
         	<div class="tips" v-if="showCzn">  
        		共{{disCountAmount}}币，开通即可使用
        	</div>
        	<div class="tips" v-else>
        		此订单中您还可以支付{{usableAmount}}克拉，但您的克拉余额不足，剩余{{residueAmount}}克拉。
        	</div>
         </div>
    </div>
    
    <div class="goods-items">
        <div class="item" v-for="item in checkedGoodsList" >
            <div class="img">
                <img :src="item.list_pic_url"/>
            </div>
            <div class="info">
                <div class="t">
                    <span class="name">{{item.goods_name}}</span>
                    <span class="number">x{{item.number}}</span>
                </div>
                <div class="m">{{item.goods_specifition_name_value||''}}</div>
                <div class="b">￥{{item.market_price}}</div>
            </div>
        </div>
    </div>
    
    <div class="order-total">
        <div class="l">实付：￥{{actualPrice}}</div>
        <div class="r" @click="submitOrder">去付款</div>
    </div>
    
  	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Toast } from 'mint-ui';
	import { Switch } from 'mint-ui';
//	import headbar from '@/components/headbar.vue'
		
	export default {
	  name: 'checkout',
//	  components:{headbar},
	  data () {
	    return {
//	    	headFont:'去付款',
			usableAmount:'',
			residueAmount:'',
			showCzn:false, 
			showCz:false,//克拉是否充足
			showBin:true,
			confirmButtonText:'知道了',
//			switchValue:false,
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
		    couponId: ' ',
		    isBuy: false,
		    couponDesc: '',
		    couponCode: '',
		    buyType: '',
		    isBuyType:'',
		    disCountAmount:'',
	    }
	  },
	  mounted(){
	  		let that = this;
  			this.isBuyType = this.$route.query.isBuy;
//			var _day = 60 * 60 * 24 *1;
			if(that.$cookie.getCookie('addressId') == undefined || that.$cookie.getCookie('addressId') == null || that.$cookie.getCookie('addressId') == ''){
				that.$cookie.setCookie('addressId','0');
			}else{
				this.addressId = that.$cookie.getCookie('addressId');
			}
  			if(that.$cookie.getCookie('couponId') == undefined || that.$cookie.getCookie('couponId') == null || that.$cookie.getCookie('couponId') == ''){
  				that.$cookie.setCookie('couponId','');
  			}else{
  				this.couponId = that.$cookie.getCookie('couponId');
  			}
  			
		    // 页面初始化 options为页面跳转所带来的参数
		    if (this.$route.query.isBuy!="false") {
		     	 this.isBuy = this.$route.query.isBuy
		    }
    		this.buyType = this.isBuy?'buy':'cart';
    		this.getCheckoutInfo();
    		this.getCouponData();
 			
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
	    	
	 },
	 methods:{
//	 	change(){
//	 		if(this.switchValue){
//	 			this.showBin = this.switchValue;
//	 			if (this.showCz){
//	 				this.showCzn = true; 
//		 		}else{
//		 			this.showBin = !this.switchValue;
//		 			this.showCzn = false; 
//		 		}
//	 		}else{
//	 			this.showBin = this.switchValue;
// 				this.showCzn = true; 
//	 		}
//	 	},
		showTb(){
			MessageBox( '克拉','斗宝俱乐部"克拉"可在斗宝俱乐部平台消费时抵用现金，1克拉抵1元现金使用。');
			document.getElementsByClassName('mint-msgbox-confirm')[0].innerText = '我知道了';
		},
	 	tapCoupon(){
	 		this.$router.push('/pages/category/selCoupon?buyType=' + this.buyType+'&isBuy='+this.isBuy);
	 	},
	 	selectAddress(){
	 		this.$router.push({path:'addresscateList'}); //购物车选择地址
	 	},
	 	addAddress(){
	 		this.$router.push({path:'addresscateAdd'});
	 	},
	 	submitOrder(){
	 			let that = this;
		 		if (this.addressId <= 0) {
			      Toast('请选择收货地址');
			      return false;
			    }
			    that.$http({
	    		method: 'post',
		        url:that.$url+ 'order/submit.options',
		        headers: {
						'Content-Type':'application/json'
					},
			    data:{ 
		        	addressId: this.addressId, 
		        	couponId: this.couponId, 
		        	type: this.buyType 
		        }
		        }).then(function (res) {
		        	var res = res.data;
			      if (res.errno === 0) {
			        let orderId = res.data.orderInfo.id;
			        window.location.href = res.payurl;
			      } else {
			       		Toast('下单失败');
			      }
			    });
	 	},
	 	getCouponData () {
//			        this.couponDesc = app.globalData.courseCouponCode.name;
//			        this.couponId = app.globalData.courseCouponCode.user_coupon_id;
			        this.couponDesc =  "平台抵扣券";
			        this.couponId =  '';
		  },
	 	getCheckoutInfo(){
			 let that = this;
		    let buyType = this.isBuy ? 'buy' : 'cart';
		    that.$http({
	    		method: 'post',
		        url:that.$url+ 'cart/checkout.options',
		        headers: {
					'Content-Type':'application/json'
				},
		        data:{ 
		        	addressId: that.addressId,
		        	couponId: that.couponId, 
		        	type: buyType
		        }
	        }).then(function (res) {
		      if (res.data.errno === 0) {
		          that.checkedGoodsList = res.data.data.checkedGoodsList;
		          that.checkedAddress = res.data.data.checkedAddress;
		          that.actualPrice = res.data.data.actualPrice;
		          that.checkedCoupon = res.data.data.checkedCoupon;
		          that.disCountAmount = res.data.data.disCountAmount;
		          that.usableAmount = res.data.data.usableAmount;
		          that.residueAmount = res.data.data.residueAmount;
		          that.showCz = res.data.data.ampleAmountFlag;
		          that.couponList = res.data.data.couponList;
		          that.couponPrice = res.data.data.couponPrice;
		          that.freightPrice = res.data.data.freightPrice;
		          that.goodsTotalPrice = res.data.data.goodsTotalPrice;
		          that.orderTotalPrice = res.data.data.orderTotalPrice
		          
		          /*判断克拉*/
		        if (that.showCz){
	 				that.showCzn = true; 
		 		}else{
		 			that.showBin = false;
		 			that.showCzn = false; 
		 		}
		        //设置默认收获地址
		        if (that.checkedAddress){
		            let addressId = that.checkedAddress.id;
		            if (addressId) {
		                that.addressId =  addressId;
		            }
		        }else{
          			MessageBox({
					  title: ' ',
					  message: '请添加默认收货地址! ',
					  showCancelButton: true
					},success(function(res){
						if (res.confirm) {
	                        that.selectAddress();
	                        console.log('用户点击确定')
	                    }
					}));
		        }
		      }else{
		        Toast(res.data.msg);
		      }
		    });
		}
	 }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--v-html  的标签 加样式 用 >>>-->
<style scoped>
	.no-border .l{
		width:6rem !important;
	}
	.mtSwitch{
		float:right !important;
	}
	.order-box .order-item .l i{
		display: inline-block;
	    background-color: #c5bfbb;
	    color: #fff;
	    -webkit-border-radius: 50%;
	    width: .32rem;
	    height: .32rem;
	    line-height: .32rem;
	    text-align: center;
	    margin-left: .06rem;
	}
	.address-box{
	    width: 100%;
	    height: 1.6655rem;
	    background: url('http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/address-bg-bd30f2bfeb.png') 0 0 repeat-x;
	    background-size: .625rem .105rem;
	    margin-bottom: .20rem;
	    padding-top: .105rem;
	    text-align: left;
	}
	
	.address-item{
	    display: flex;
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
	    float: left;
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
	    border-radius: .05rem;
	    border: 1px solid #b4282d;
	    font-size: .205rem;
	    text-align: center;
	    line-height: .33rem;
	    color: #b4282d;
	}
	
	.address-box .m{
	    flex: 1;
	    height: .7225rem;
	    color: #999;
	    font-size: .29rem;
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
	
	.address-box .r img{
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
	    height: 1.083rem;
	    overflow: hidden;
	    background: #fff;
	    padding-left: .3125rem;
	    width: 7.15rem;
	}
	
	.coupon-box .l{
	    height: .43rem;
	    line-height: .43rem;
	    padding-top: .35rem;
	    float: left;
	    width: 6.3rem;
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
	    width: .77rem;
	    height: .77rem;
	    float: right;
	    line-height: .77rem;
	}
	
	.coupon-box .r img{
	    width: .52078rem;
	    height: .52078rem;
	}
	
	.order-box{
	    margin-top: .20rem;
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
	    border-bottom: 1px solid #d9d9d9;
	    font-size:.29rem;
	    text-align: left;
	}
	.order-box  .order-tips{
		height: auto;
	    font-size: .26rem;
	    color: #c5bfbb;
	    margin-top: -0.3rem;
	    padding: .2rem 0;
	}
	.order-box .order-item .l{
	    float: left;
	    height: .52rem;
	    width: 50%;
	    line-height: .52rem;
	    overflow: hidden;
	    padding-top: .26rem;
	}
	
	.order-box .order-item .r{
	    float: right;
	    text-align: right;
	    width: 50%;
	    height: .52rem;
	    line-height: .52rem;
	    overflow: hidden;
	    padding-top: .26rem;
	}
	
	.order-box .order-item.no-border{
	    border-bottom: none;
	}
	
	.goods-items{
	    margin-top: .20rem;
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
	    padding-top: .05rem;
	    font-size:.29rem;
	    text-align: left;
	}
	
	.goods-items .t{
	    height:  .33rem;
	    line-height: .33rem;
	    margin-bottom: .10rem;
	    overflow: hidden;
	    font-size: .30rem;
	    color: #333;
	    width: 5.2rem;
	}
	
	.goods-items .t .name{
	    display: block;
	    float: left;
	    width: 4.5rem;
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap;
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
	    font-size: .30rem;
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
	    border-top: 1px solid rgba(0,0,0,0.2);
	    border-bottom: 1px solid rgba(0,0,0,0.2);
	    text-align: left;
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
