<template>
  <div class="hello">
  	<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
  		<div class="showList" v-if="orderList.length>0" >
  			<ul class="nav_list">
  				<li v-for="(item,index) in items" @click="selectStyle (item, index)"  :class="activeClass == index ? 'list_choice' : '' ">{{item.value}} </li>
  			</ul>
	  		<ul class="" v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" infinite-scroll-distance="10">
	 			 <li v-for="(item,index) in orderList" >
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
        <div v-else class="noData">没有更多数据了</div>
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
    	orderList:[],
    	activeClass:0,
    	items: [
　　	{value:'全部'},
　　	{value:'待付款'},
　　	{value:'待收货'},
　　	{value:'已完成'},
					{value:'已取消'},
　	]
    }
  },
  mounted(){
  	
  	var that = this;    
    	that.$http({
        method: 'post',
        url:that.$url+ 'order/list.options',
        data:{
        	page:1,
        	size:10
        }
    	}).then(function (response) {
    		if(response.data.errno != 401){
	    			that.orderList = response.data.data.data;
	    	}
		  })
  },
  methods:{
  	selectStyle (item, index) {
  			this.activeClass = index;
　},
  	payOrder(orderIndex){
	      let order = this.orderList[orderIndex];
	    	this.$router.push( '/pages/pay/pay?orderId=' + order.id + '&actualPrice=' + order.actual_price);
  	},
  	loadMore() {
//		  this.loading = true;
//		  setTimeout(() => {
//		    let last = this.orderList[this.orderList.length - 1];
//		    for (let i = 1; i <= 10; i++) {
//		      this.orderList.push(last + i);
//		    }
//		    this.loading = false;
//		  }, 2500);
		}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.nav_list{
		font-size:.29rem;
		overflow: hidden;
		background-color:#fff;
	}
	.nav_list li{
		float:left;
		width:20%;
		padding:.15rem 0;
	}
	.nav_list li.list_choice{
		border-bottom:3px solid #33CC99;
	}
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
.noData{
	font-size:.29rem;
	margin-top:1rem;
}
</style>
