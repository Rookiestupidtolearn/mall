<template>
 	<div class="coupon-list">
 		<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  		
  		<div class="mt88">
	 		<div class="item" v-for = "item in couponList"  :style="{background: [ item.coupon_status==1 || item.coupon_status==4  ? 'linear-gradient(to right,#cfa568,#e3bf79)' : 'linear-gradient(to right,#999,#DDDDDD)']}">
	         <!-- <div class="tag">新人专享</div>  -->
	        <div class="content">
	          <div class="left">
	            <div class="name">{{item.name}}</div>
	            <!-- <div class="time">有效期至{{item.use_end_date}}</div> -->
	            <div class="time">仅限该单使用</div>
	          </div>
	          <div class="right">
	            <img v-if="item.coupon_status==1" class='mid-img' src='../../../static/images/coupon_ksy.png'/>
	            <img v-if="item.coupon_status==2" class='mid-img' src='../../../static/images/coupon_ysy.png'/>
	            <img v-if="item.coupon_status==3" class='mid-img' src='../../../static/images/coupon_gq.png'/>
	            <img v-if="item.coupon_status==4" class='mid-img' src='../../../static/images/coupon_zfz.png'/>
	          </div>
	        </div>
	        <div class="condition">抵扣￥{{item.coupon_price}}</div>
	      </div>
	     </div>
 	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import headbar from '@/components/headbar.vue';
		
export default {
  name: 'coupon',
 components:{headbar},
  data () {
    return {
    	headFont:'优惠券列表',
    	couponList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
        method: 'post',
        url:that.$url+ 'coupon/list',
    	}).then(function (response) {
    				response = {"errno":0,"data":[{"id":11,"user_coupon_id":3650,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":1,"min_transmit_num":null,"coupon_status":4,"coupon_price":2.70},{"id":11,"user_coupon_id":3461,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":6.00},{"id":11,"user_coupon_id":2596,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":11.64},{"id":11,"user_coupon_id":2595,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":2590,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2589,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2588,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2587,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2586,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2584,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2583,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2581,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":18.00},{"id":11,"user_coupon_id":2580,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2579,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2577,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":18.00},{"id":11,"user_coupon_id":2528,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":48.84},{"id":11,"user_coupon_id":2527,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":51.48},{"id":11,"user_coupon_id":2522,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2518,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":9.00},{"id":11,"user_coupon_id":2513,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.50},{"id":11,"user_coupon_id":2303,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":992,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":918,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":1.37},{"id":11,"user_coupon_id":262,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":4.01},{"id":11,"user_coupon_id":260,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":234,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":5.28},{"id":11,"user_coupon_id":233,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":5.28},{"id":11,"user_coupon_id":232,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":6.65},{"id":11,"user_coupon_id":231,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":173,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.25},{"id":11,"user_coupon_id":171,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":0.49},{"id":11,"user_coupon_id":258,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":850.08},{"id":11,"user_coupon_id":176,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":0.88},{"id":11,"user_coupon_id":177,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":0.68},{"id":11,"user_coupon_id":259,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64},{"id":11,"user_coupon_id":307,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":1.37},{"id":11,"user_coupon_id":303,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":15.84},{"id":11,"user_coupon_id":308,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":4.01},{"id":11,"user_coupon_id":310,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":0,"min_transmit_num":null,"coupon_status":3,"coupon_price":2.64}],"errmsg":"执行成功"};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
    			console.log(response)
    			that.couponList = response.data;
    		}
		  })
  },
  methods:{
  	
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.coupon-list {
height:auto;
overflow:hidden;
padding-left:.30rem;
padding-right:.30rem;
padding-top:.3rem
}
.item {
position:relative;
height:2.40rem;
width:6.9rem;
border-radius:.08rem;
margin-bottom:.30rem;
padding-top:.52rem;
overflow: hidden;
}
.content{
  margin-top: .24rem;
  margin-left: .40rem;
  margin-right: .40rem;
  text-align: left;
}
.name{
  font-size: .44rem;
  color: #fff;
  margin-bottom: .14rem;
}
.time{
  font-size: .24rem;
  color: rgba(255,255,255, 0.8);
  line-height: .30rem;
}
.content .left{
  float: left;
}
.content .right{
  width: 1.62rem;
  float: right;
}
.content .right  .mid-img {
  width: 1.00rem;
  height: 1.00rem;
}
.condition{
  position: absolute;
  width: 100%;
  bottom: 0;
  left:0;
  height: .78rem;
  background: rgba(0,0,0,.08);
  font-size: .24rem;
  line-height: .78rem;
  text-align: left;
  padding-left: .4rem;
  color: #fff;
}
.condition .icon{
  margin-left: .30rem;
  width: .24rem;
  height:.24rem;
}
</style>