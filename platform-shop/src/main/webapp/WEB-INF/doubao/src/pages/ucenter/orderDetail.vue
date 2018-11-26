<template>
	<div class="container">
		
		 <div class="order-info">
        <div class="item-a">下单时间：{{orderInfo.add_time}}</div>
        <div class="item-b">订单编号：{{orderInfo.order_sn}}</div>
      <div class="item-c">
            <div class="l">实付：<span class="cost">￥{{orderInfo.actual_price}}</span></div>
            <div class="r">
                <div v-if="orderInfo.handleOption.pay">
                  <div class="btn" @click="cancelOrder">取消订单</div>
                  <div class="btn active" @click="payOrder">去付款</div>
                </div>
                <div v-else-if="orderInfo.handleOption.confirm">
                  <div class="btn" @click="cancelOrder">取消订单</div>
                  <div class="btn active" @click="confirmOrder">确认收货</div>
                </div>
                <div v-else>
                  <div class="btn active" @click="cancelOrder"  :style="{display:[cancelBtnShow ? 'block' : 'none']}">取消订单</div>
                </div>
            </div>
        </div>
    </div>
    
     <div class="order-goods">
        <div class="h">
            <div class="label">商品信息</div>
            <div class="status">{{orderInfo.order_status_text}}</div>
        </div>
        <div class="goods">
            <div class="item" v-for="item in orderGoods" >
                <div class="img">
                	<img :src="item.list_pic_url"/>
                </div>
                <div class="info">
                    <div class="t">
                        <span class="name">{{item.goods_name}}</span>
                        <span class="number">x{{item.number}}</span>
                    </div>
                    <div class="attr">{{item.goods_specifition_name_value||''}}</div>
                    <div class="price">￥{{item.market_price}}</div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="order-bottom">
        <div class="address">
            <div class="t">
                <span class="name">{{orderInfo.consignee}}</span>
                <span class="mobile">{{orderInfo.mobile}}</span>
            </div>
            <div class="b">{{orderInfo.full_region + orderInfo.address}}</div>
        </div>
        <div class="total">
            <div class="t">
                <span class="label">商品合计：</span>
                <span class="txt">￥{{orderInfo.goods_price}}</span>
            </div>
            <div class="t">
                <span class="label">优惠券：</span>
                <span class="txt2">-{{orderInfo.coupon_price}}</span>
            </div>
            <div class="t">
                <span class="label">运费：</span>
                <span class="txt">￥{{orderInfo.freight_price}}</span>
            </div>
        </div>
        <div class="pay-fee">
            <span class="label">实付：</span>
            <span class="txt">￥{{orderInfo.actual_price}}</span>
        </div>
    </div>
    
	</div>
</template>

<script>
		import { MessageBox } from 'mint-ui';
		
export default {
  name: 'orderDetail',
  data () {
    return {
    	orderInfo:{},
    	cancelBtnShow:false,
    	orderGoods:[],
    	handleOption:{}
    }
  },
  mounted(){
  	let that = this;   
  	let id = this.$route.query.id;
    	that.$http({
        method: 'post',
        url:that.$url+ 'order/detail',
        params:{orderId:id}
    	}).then(function (response) {
    		console.log(response)
    		that.orderInfo = response.data.orderInfo;
    		that.orderGoods =  response.data.orderGoods;
      		that.handleOption =  response.data.handleOption;
      
          //101取消订单   301已完成订单   103订单失效
	        if (response.data.orderInfo.order_status == '101' || response.data.orderInfo.order_status == '301' || response.data.orderInfo.order_status == '103') {
	          	that.cancelBtnShow = true
	      	}
		})
  },
  methods:{
  	payOrder(){
  			let that = this;
  			let id = this.$route.query.id;
  			 that.$http({
			        method: 'post',
			        url:that.$url+ 'pay/prepay',
			        params:{orderId:id || 15}
		    	}).then(function (res) {
			      if (res.errno === 0) {
				        console.log('支付页面逻辑待完善');
			      }
		    });

  	},
  	cancelOrder(){
	  		console.log('开始取消订单');
	    let that = this;
	    let id = this.$route.query.id;
	    let orderInfo = that.orderInfo;
	    console.log(orderInfo);
	
	    var order_status = orderInfo.order_status;
	    console.log(order_status);
	  
	
	    var errorMessage = '';
	    switch (order_status){
	      case 300: {
	        console.log('已发货，不能取消');
	        errorMessage = '订单已发货';
	        break;
	      }
	      case 301:{
	        console.log('已收货，不能取消');
	        errorMessage = '订单已收货';
	        break;
	      }
	      case 101:{
	        console.log('已经取消');
	        errorMessage = '订单已取消';
	        break;
	      }
	      case 102: {
	        console.log('已经删除');
	        errorMessage = '订单已删除';
	        break;
	      }
	      case 401: {
	        console.log('已经退款');
	        errorMessage = '订单已退款';
	        break;
	      }
	      case 402: {
	        console.log('已经退款退货');
	        errorMessage = '订单已退货';
	        break;
	      }
	    }
	      
	    if (errorMessage != '') {
	      console.log(errorMessage);
	      Toast(errorMessage);
	      return false;
	    }
	    
	    console.log('可以取消订单的情况');
	    MessageBox({
					  title: ' ',
					  message: '确定要取消此订单？ ',
					  showCancelButton: true
					},function(action){
							if(action == 'confirm'){
								  console.log('用户点击确定');
								  that.$http({
						        method: 'post',
						        url:that.$url+ 'order/cancelOrder',
						        params:{orderId:id}
						    	}).then(function (response) {
						    		response = {"errno":0,"data":"取消成功","errmsg":"执行成功"};
						    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
						    			MessageBox({
											  title: ' ',
											  message: '请先登录 ',
											  showCancelButton: true
											});
						    			return false;
						    		}else{
							    		MessageBox({
											  title: ' ',
											  message: response.data
											},function(action){
													that.$router.push('/views/ucenter/order');
											});
						    		}
								  })
							}
					});
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.order-info{
    padding-top: .25rem;
    background: #fff;
    height: auto;
    overflow: hidden;
    text-align: left;
}

.item-a{
    padding-left: .3125rem;
    height: .425rem;
    padding-bottom: .125rem;
    line-height: .30rem;
    font-size: .30rem;
    color: #666;
}

.item-b{
    padding-left: .3125rem;
    height: .29rem;
    line-height: .29rem;
    margin-top: .125rem;
    margin-bottom: .415rem;
    font-size: .30rem;
    color: #666;
}

.item-c{
    margin-left: .3125rem;
    border-top: 1px solid #f4f4f4;
    height: 1.03rem;
    line-height: 1.03rem;
    font-size:.29rem;
}

.item-c .l{
    float: left;
}

.item-c .r{
    height: 1.03rem;
    float: right;
    display: flex;
    align-items: center;
    padding-right: .16rem;
}

.item-c .r .btn{
    float: right;
}

.item-c .cost{
    color: #b4282d;
}

.item-c .btn{
    line-height: .66rem;
    border-radius: .05rem;
    text-align: center;
    margin: 0 .15rem;
    padding: 0 .20rem;
    height: .66rem;
}

.item-c .btn.active{
    background: #b4282d;
    color: #fff;
}

.order-goods{
    margin-top: .20rem;
    background: #fff;
    font-size:.29rem;
}

.order-goods .h{
    height: .9375rem;
    line-height: .9375rem;
    margin-left: .3125rem;
    border-bottom: 1px solid #f4f4f4;
    padding-right: .3125rem;
}

.order-goods .h .label{
    float: left;
    font-size: .30rem;
    color: #333;
}

.order-goods .h .status{
    float: right;
    font-size: .30rem;
    color: #b4282d;
}

.order-goods .item{
    display: flex;
    align-items: center;
    height: 1.92rem;
    margin-left: .3125rem;
    padding-right: .3125rem;
    border-bottom: 1px solid #f4f4f4;
}

.order-goods .item:last-child{
    border-bottom: none;
}

.order-goods .item .img{
    height: 1.4583rem;
    width: 1.4583rem;
    background: #f4f4f4;
}

.order-goods .item .img image{
    height: 1.4583rem;
    width: 1.4583rem;
}

.order-goods .item .info{
    flex: 1;
    height: 1.4583rem;
    margin-left: .20rem;
}

.order-goods .item .t{
    margin-top: .08rem;
    height: .33rem;
    line-height: .33rem;
    margin-bottom: .105rem;
}

.order-goods .item .t .name{
    display: block;
    float: left;
    height: .33rem;
    line-height: .33rem;
    color: #333;
    font-size: .30rem;
}

.order-goods .item .t .number{
    display: block;
    float: right;
    height: .33rem;
    text-align: right;
    line-height: .33rem;
    color: #333;
    font-size: .30rem;
}

.order-goods .item .attr{
    height: .29rem;
    line-height: .29rem;
    color: #666;
    margin-bottom: .25rem;
    font-size: .25rem;
}

.order-goods .item .price{
    height: .30rem;
    line-height: .30rem;
    color: #333;
    font-size: .30rem;
    text-align: left;
}

.order-bottom{
    margin-top: .20rem;
    padding-left: .3125rem;
    height: auto;
    overflow: hidden;
    background: #fff;
}

.order-bottom .address{
    height: 1.28rem;
    padding-top: .25rem;
    border-bottom: 1px solid #f4f4f4;
}

.order-bottom .address .t{
    margin-bottom: .075rem;
    font-size: 0;
    text-align: left;
}

.order-bottom .address .name{
    display: inline-block;
    height: .35rem;
    width: 1.40rem;
    line-height: .35rem;
    font-size: .25rem;
}

.order-bottom .address .mobile{
    display: inline-block;
    height: .35rem;
    line-height: .35rem;
    font-size: .25rem;
}

.order-bottom .address .b{
    height: .35rem;
    line-height: .35rem;
    font-size: .25rem;
    text-align: left;
}

.order-bottom .total{
    height: 1.36rem;
    padding-top: .20rem;
    border-bottom: 1px solid #f4f4f4;
    font-size: .29rem;
    text-align: left;
}

.order-bottom .total .t{
    height: .30rem;
    line-height: .30rem;
    margin-bottom: .075rem;
    display: flex;
}

.order-bottom .total .label{
    width: 1.40rem;
    display: inline-block;
    height: .35rem;
    line-height: .35rem;
    font-size: .25rem;
}

.order-bottom .total .txt{
    flex: 1;
    display: inline-block;
    height: .35rem;
    line-height: .35rem;
    font-size: .25rem;
}

.order-bottom .total .txt2{
    color:#33cc99;
}

.order-bottom .pay-fee{
    height: .81rem;
    line-height: .81rem;
    font-size: .29rem;
    text-align: left;
}

.order-bottom .pay-fee .label{
    display: inline-block;
    width: 1.40rem;
    color: #b4282d;
}

.order-bottom .pay-fee .txt{
    display: inline-block;
    width: 1.40rem;
    color: #b4282d;
}

</style>
