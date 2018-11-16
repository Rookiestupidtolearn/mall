<template>
 	<div class="container">
 		
 	<div class="no-course" v-if="couponList.length <= 0" >
 		<img src="../../../static/images/my_course_empty.png"/>
 		<p class="desc">您还没有优惠券~</p>
 	</div>
 	
	  <div v-else class="coupon-list">
	    <div v-for="item in couponList" >
	    <div  @click='tapCoupon(item.enabled)' class="item" :style="{background: [item.enabled==1?'linear-gradient(to right,#cfa568,#e3bf79)':'linear-gradient(to right,#999,#DDDDDD)']}">
	        <div class="content">
	          <div class="left">
	            <div class="name">{{item.name}}</div>
	            <div class="time">仅限该单使用</div>
	          </div>
	         <div class="right">
	            <img v-if="item.enabled==1" class='mid-img' src='../../../static/images/coupon_ksy.png'/>
	  			<img v-if="item.enabled==0" class='mid-img' src='../../../static/images/coupon_bky.png'/>
	          </div>
	        </div>
	        <div class="condition">
	          <p class="txt">抵扣￥{{item.coupon_price}}</p>
	        </div>
	      </div>
	      </div>
	    </div>
	    </div>
  </div>
</template>

<script>
		
	export default {
	  name: 'selCoupon',
	  data () {
	    return {
	    	couponList: '',
    		buyType: ''
	    }
	  },
	  mounted(){
	  		let that = this;
	  		this.buyType = this.$route.query.buyType;
    		 that.$http({
			   	method:'post',
			   	url:that.$url+'coupon/listByGoods',
			   params:{ type: this.buyType }
		   }).then(function (res) {
		   		res = {"data":{"errno":0,"data":[{"id":11,"user_coupon_id":3703,"name":"平台抵扣券","type_money":0.00,"send_type":8,"min_amount":0.00,"max_amount":0.00,"send_start_date":"2018.10.12","send_end_date":"2018.10.12","use_start_date":"2018.10.12","use_end_date":"2018年10月12日","min_goods_amount":0.00,"coupon_txt":null,"user_id":"28","coupon_number":"1","enabled":1,"min_transmit_num":null,"coupon_status":1,"coupon_price":7.00}],"errmsg":"执行成功"}};
		   		console.log(res);
			    if (res.data.errno == 0) {
			        that.couponList = res.data.data;
			    }
		   });	    
	  },
	  methods:{
	  	tapCoupon(enabled){
		    if (enabled==0) {
		      return
		    }
		    app.globalData.userCoupon = 'USE_COUPON'
		    app.globalData.courseCouponCode = item
		    wx.navigateBack({
		    })
	  	}
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.no-course img {
	margin-top:1.16rem;
	width:5.97rem;
	height:4.67rem;
	margin-bottom:.60rem;
}

.no-course .desc {
	font-size:.30rem;
	line-height:.40rem;
	color:#333;
	font-weight:bold;
}

.not-use {
  margin: .30rem auto .30rem;
  width: 6.90rem;
  height: .88rem;
  background-color: #f76a1d;
  border-radius: .05rem;
  font-size: .32rem;
  font-weight: normal;
  font-stretch: normal;
  line-height: 0px;
  letter-spacing: 0px;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.coupon-form{
  height: 1.10rem;
  width: 100%;
  background: #fff;
  padding-left: .30rem;
  padding-right: .30rem;
  padding-top: .20rem;
  display: flex;
}

.input-box{
  flex: 1;
  height: .70rem;
  color: #333;
  font-size: .24rem;
  background: #fff;
  position: relative;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: .04rem;
  margin-right: .30rem;
}

.input-box .coupon-sn{
  position: absolute;
  top: .10rem;
  left: .30rem;
  height: .50rem;
  width: 100%;
  color: #000;
  line-height: .50rem;
  font-size: .24rem;
}

.clear-icon{
  position: absolute;
  top: .21rem;
  right: .30rem;
  width: .28rem;
  height: .28rem;
}

.add-btn{
  display: flex;
  justify-content: center;
  align-items: center;
  height: .70rem;
  border:none;
  width: 1.68rem;
  background: #b4282d;
  border-radius: 0;
  line-height: .70rem;
  color: #fff;
  font-size: .28rem;
}

.add-btn.disabled{
  background: #ccc;
}

.help{
  height: .72rem;
  line-height: .72rem;
  text-align: right;
  padding-right: .30rem;
  background: url(https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/15032866437ca8.png) 590rem center no-repeat;
  background-size: .28rem;
  color: #999;
  font-size: .24rem;
}

.coupon-list{
  height: auto;
  overflow: hidden;
  padding-left: .30rem;
  padding-right: .30rem;
}

.item{
  position: relative;
  height: 2.90rem;
  width: 100%;
  border-radius: .08rem;
  margin-bottom: .30rem;
}

.tag{
  height: .32rem;
  background: #A48143;
  padding-left: .16rem;
  padding-right: .16rem;
  position: absolute;
  left: .20rem;
  color: #fff;
  top: .20rem;
  font-size: .20rem;
  text-align: center;
  line-height: .32rem;
}


.content{
  margin-top: .24rem;
  margin-left: .40rem;
  display: flex;
  margin-right: .40rem;
  flex-direction: row;
  align-items: center;
  padding-top: .7rem;
}

.content .left{
  flex: 1;
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

.content .right{
  width: 1.62rem;
  display: flex;
  justify-content: center;
  align-items: center;
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
  display: flex;
  flex-direction: row;
}

.condition .txt{
  display: block;
  height: .30rem;
  flex: 1;
  overflow: hidden;
  font-size: .24rem;
  line-height: .30rem;
  color: #fff;
  text-align: left;
	padding-left: 0.4rem;
	padding-top: 0.25rem;
}


.condition .icon{
  margin-left: .30rem;
  width: .24rem;
  height: .24rem;
}

</style>
