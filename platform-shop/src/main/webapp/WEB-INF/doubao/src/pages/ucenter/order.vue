<template>
  <div class="hello">
  	<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  		
  	<ul class="mt88" v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" infinite-scroll-distance="10">
 			 <li v-for="(item,index) in orderList">
				<router-link  :to = "'/views/ucenter/orderDetail?id='+item.id" class="order" >
	            <div class="h">
	                <div class="l">订单编号：{{item.order_sn}}</div>
	                <div class="r">{{item.order_status_text}}</div>
	            </div>
	            <div class="b">
	                <div class="l">实付：￥{{item.actual_price}}</div>
	                <div class="r">
	                	<mt-button type="danger"  size="small" @click.prevent="payOrder(index)"  :style="{ display: [ item.handleOption.pay ? 'block' : 'none']}">去付款</mt-button>
	                </div>
	            </div>
	        </router-link>
	        </li>
        </ul>
  </div>
</template>

<script>
import { InfiniteScroll } from 'mint-ui';
import { MessageBox } from 'mint-ui';
import headbar from '@/components/headbar.vue';
		
export default {
  name: 'order',
  components:{headbar},
  data () {
    return {
    	headFont:'订单列表',
    	orderList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
        method: 'post',
        url:that.$url+ 'order/list',
        params:{
        	page:1,
        	size:10
        }
    	}).then(function (response) {
    		response = {"errno":0,"data":{"count":17,"numsPerPage":10,"totalPages":2,"currentPage":1,"data":[{"id":131,"order_sn":"20181107142642873146360","user_id":28,"order_status":0,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":90.00,"integral":0,"integral_money":0.00,"order_price":90.00,"goods_price":90.00,"add_time":"2018-11-07 14:26:43","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":3685,"parent_id":null,"coupon_price":0.00,"callback_status":null,"goodsCount":1,"order_status_text":"未付款","handleOption":{"cancel":true,"confirm":false,"delivery":false,"buy":false,"pay":true,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":130,"order_sn":"20181107131002467713534","user_id":28,"order_status":0,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":6.30,"integral":0,"integral_money":0.00,"order_price":9.00,"goods_price":9.00,"add_time":"2018-11-07 13:10:02","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":3650,"parent_id":null,"coupon_price":2.70,"callback_status":null,"goodsCount":1,"order_status_text":"未付款","handleOption":{"cancel":true,"confirm":false,"delivery":false,"buy":false,"pay":true,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":128,"order_sn":"20181031101501587439398","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":48.77,"integral":0,"integral_money":0.00,"order_price":0.00,"goods_price":0.00,"add_time":"2018-10-31 10:15:02","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":3606,"parent_id":null,"coupon_price":-48.77,"callback_status":null,"goodsCount":2,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":123,"order_sn":"20181029150623647287038","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":1000.00,"integral":0,"integral_money":0.00,"order_price":1000.00,"goods_price":1000.00,"add_time":"2018-10-29 15:06:24","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":3520,"parent_id":null,"coupon_price":0.00,"callback_status":null,"goodsCount":1,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":120,"order_sn":"20181029142250418788931","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":14.00,"integral":0,"integral_money":0.00,"order_price":20.00,"goods_price":20.00,"add_time":"2018-10-29 14:22:50","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":3461,"parent_id":null,"coupon_price":6.00,"callback_status":null,"goodsCount":1,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":76,"order_sn":"20181022161429349696076","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":29.00,"integral":0,"integral_money":0.00,"order_price":29.00,"goods_price":29.00,"add_time":"2018-10-22 16:14:29","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":920,"parent_id":null,"coupon_price":0.00,"callback_status":null,"goodsCount":1,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":75,"order_sn":"20181022161356997090589","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":1288.00,"integral":0,"integral_money":0.00,"order_price":1288.00,"goods_price":1288.00,"add_time":"2018-10-22 16:13:57","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":919,"parent_id":null,"coupon_price":0.00,"callback_status":null,"goodsCount":1,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":74,"order_sn":"20181022161328780335594","user_id":28,"order_status":103,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":4.85,"integral":0,"integral_money":0.00,"order_price":6.22,"goods_price":6.22,"add_time":"2018-10-22 16:13:29","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":918,"parent_id":null,"coupon_price":1.37,"callback_status":null,"goodsCount":1,"order_status_text":"订单失效","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":false,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":73,"order_sn":"20181022161315761672181","user_id":28,"order_status":101,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":5.36,"integral":0,"integral_money":0.00,"order_price":8.00,"goods_price":8.00,"add_time":"2018-10-22 16:13:16","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":310,"parent_id":null,"coupon_price":2.64,"callback_status":null,"goodsCount":1,"order_status_text":"已取消","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":true,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null},{"id":46,"order_sn":"20181019135939722762824","user_id":28,"order_status":101,"shipping_status":0,"pay_status":0,"consignee":"张然","country":null,"province":"北京市","city":"市辖区","district":"朝阳区","address":"金隅泰和园2403","mobile":"18310528362","postscript":null,"shipping_id":0,"shipping_code":null,"shipping_name":null,"shipping_no":null,"pay_id":null,"pay_name":null,"shipping_fee":0.00,"actual_price":5.36,"integral":0,"integral_money":0.00,"order_price":8.00,"goods_price":8.00,"add_time":"2018-10-19 13:59:40","confirm_time":null,"pay_time":null,"freight_price":0,"coupon_id":260,"parent_id":null,"coupon_price":2.64,"callback_status":null,"goodsCount":1,"order_status_text":"已取消","handleOption":{"cancel":false,"confirm":false,"delivery":false,"buy":true,"pay":false,"comment":false,"delete":false,"return":false},"full_cut_price":null,"full_region":"北京市 市辖区 朝阳区 ","order_type":null}],"filterCategory":null,"goodsList":null},"errmsg":"执行成功"};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
	    		console.log(response)
	    		that.orderList = response.data.data;
    		}
		  })
  },
  methods:{
  	payOrder(orderIndex){
	      let order = this.orderList[orderIndex];
	    	this.$router.push( '/pages/pay/pay?orderId=' + order.id + '&actualPrice=' + order.actual_price);
  	},
  	loadMore() {
		  this.loading = true;
		  setTimeout(() => {
		    let last = this.orderList[this.orderList.length - 1];
		    for (let i = 1; i <= 10; i++) {
		      this.orderList.push(last + i);
		    }
		    this.loading = false;
		  }, 2500);
		}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.order .b {
height:1.03rem;
line-height:1.03rem;
margin-left:.3125rem;
padding-right:.3125rem;
border-top:1px solid #f4f4f4;
font-size:.30rem;
color:#333;
}
.order .b .l {
float:left;
}
.order .b .r {
float:right;
margin-top: .15rem;
}
.order {
	display: block;
margin-top:.20rem;
background:#fff;
}
.order .h {
height:.833rem;
line-height:.833rem;
margin-left:.3125rem;
padding-right:.3125rem;
border-bottom:1px solid #f4f4f4;
font-size:.30rem;
color:#333;
}
.order .h .l{
	float: left;
	font-size:.26rem;
}
.order .h .r {
float:right;
color:#b4282d;
font-size:.24rem;
}

</style>
