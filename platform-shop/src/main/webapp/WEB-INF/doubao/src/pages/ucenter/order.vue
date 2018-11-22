<template>
  <div class="hello">
  	<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
  	<ul class="" v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" infinite-scroll-distance="10">
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
//import headbar from '@/components/headbar.vue';
		
export default {
  name: 'order',
//components:{headbar},
  data () {
    return {
//  	headFont:'订单列表',
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
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			that.fontSize.goLogin();
    		}else{
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
