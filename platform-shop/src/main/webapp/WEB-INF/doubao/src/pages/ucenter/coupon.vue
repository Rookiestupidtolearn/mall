<template>
 	<div class="coupon-list">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
  		<div class="">
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
//	import headbar from '@/components/headbar.vue';
		
export default {
  name: 'coupon',
// components:{headbar},
  data () {
    return {
//  	headFont:'优惠券列表',
    	couponList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
        method: 'post',
        url:that.$url+ 'coupon/list',
    	}).then(function (response) {
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			that.fontSize.goLogin()
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