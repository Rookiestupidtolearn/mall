<template>
	<div class="container">
		<!--多个物流-->
	 	<div class="logistics">
	 		<p class="top"><span class="left">韵达快递：20181115555</span><span class="right">预计明天送达</span></p>
	 		<div class="productInfo">
	 			<img src="../../../static/images/detail_kefu.png" />
	 			<p class="name">绿豆糕 80克绿豆糕 80克绿糕 80克绿糕 80克绿豆糕克绿豆绿豆糕 80克糕 80克 （4枚入）</p>
	 		</div>
	 		<div class="btn" @click="looklog(1)">查看物流</div>
		</div>
		<div class="logistics">
	 		<p class="top"><span class="left">韵达快递：20181115555</span><span class="right">预计明天送达</span></p>
	 		<div class="productInfo">
	 			<img src="../../../static/images/detail_kefu.png" />
	 			<p class="name">绿豆糕 80克绿豆糕 80克绿糕 80克绿糕 80克绿豆糕克绿豆绿豆糕 80克糕 80克 （4枚入）</p>
	 		</div>
	 		<div class="btn" @click="looklog(2)">查看物流</div>
		</div>
		<!--1个物流-->
		 <!-- <div class="logistics" >
	 		<p class="top"><span class="left">韵达快递：20181115555</span><span class="right">预计明天送达</span></p>
	 		<div class="productInfo">
	 			<img src="../../../static/images/detail_kefu.png" />
	 			<p class="name">绿豆糕 80克绿豆糕 80克绿糕 80克绿糕 80克绿豆糕克绿豆绿豆糕 80克糕 80克 （4枚入）</p>
	 		</div>
		</div>
		<div class="logisticsStatus">
			<p class="top"><span >物流状态</span></p>
			<div class="progress">
				<div :class="openD ? 'shanshou' : ' ' " >
					<div class="first overflow">
						<div class="left"></div>
						<div class="middle">
							<img src="../../../static/images/logistics/w_sl.png"/>
						</div>
						<div class="right final">【收货地址】北京市东城区七楼</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/w_ld.png"/>
						</div>
						<div class="right top45">
							<h4>已签收</h4>
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/w_w.png"/>
						</div>
						<div class="right top45">
							<h4>派送中</h4>
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/w_y.png"/>
						</div>
						<div class="right top45">
							<h4>运输中</h4>
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/iconcircle.png" class="iconcircle"/>
						</div>
						<div class="right top45">
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/w_l.png"/>
						</div>
						<div class="right top45">
							<h4>已揽件</h4>
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
					<div class="second overflow">
						<div class="left top45">
							<h4>12:45</h4>
							<h6>11-16</h6>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/w_f.png"/>
						</div>
						<div class="right top45">
							<h4>已发货</h4>
							<p>浙江省杭州市釜山物流公司  已收件</p>
						</div>
					</div>
				</div>
				<div class="bottom" @click="open" v-if="openD">展开<img src="../../../static/images/logistics/xjt.png"/></div>
				<div class="bottom" @click="close" v-else>收起<img src="../../../static/images/logistics/tjt.png"/></div>
			</div>
		</div>-->
	</div>
</template>

<script>
	export default {
	  name: 'logistics',
	  data () {
	    return {
	    	openD:true
	    }
	  },
	  mounted(){
	  	
	  },
	  methods:{
	  	open(){
	  		this.openD = !this.openD;
	  	},
	  	close(){
	  		this.openD = !this.openD;
	  	},
	  	looklog(e){
	  		this.$router.push('/pages/ucenter/lookLogistics?id='+e)
	  	}
       }
	}
</script>

<style scoped>
	.shanshou{
		height:2.24rem;
		overflow: hidden
	}
	.overflow{
		overflow: hidden;
	}
	.progress .bottom{
		font-size:.26rem;
		text-align: center;
		padding:.4rem 0;
	}
	.progress .bottom img{
		width:.26rem;
	}
	.progress{
		padding:0 .35rem;
	}
	.progress .top45{
		padding-top:.45rem;
	}
	.progress h4,.progress h6{
		font-weight: normal;
	}
	.progress .first{
		margin-top:.3rem;
	}
	.progress .left{
		float: left;
		width:.8rem;
		height: .1rem;
	}
	.progress .left h4{
		font-size: .28rem;
		color:#3c3c3c;
	}
	.progress .left h6{
		font-size:.22rem;
		color:#999999;
	}
	.progress .middle {
		float:left;
		height:.45rem;
	}
	.progress .middle img{
		width:.4rem;
		vertical-align: text-top;
		position: relative;
   		left: .2rem;
	}
	.progress .middle .iconcircle{
		width:.08rem;
		padding:.16rem;
	}
	.progress .right{
		float:left;
		font-size:.26rem;
		color:#3c3c3c;
		padding-left: .4rem;
		border-left:1px dashed #cbcbcb;
	}
	.progress .final{
		color:#999999;
	}
	.progress .right p{
		font-size:.22rem;
		color:#999999;
		margin-top: .12rem;
	}
	.logisticsStatus{
		background-color:#fff;
		text-align: left;
	}
	.logisticsStatus .top{
		font-size:.28rem;
		color:#3b3c3c;
		padding:.23rem .35rem;
		border-bottom:1px solid #e8e8e8;
	}
	.btn{
		display: inline-block;
	    height: auto;
	    padding: .09rem .19rem;
	    font-size: .26rem;
	    color: #666666;
	    -webkit-border-radius: 2rem;
	    background-color: initial;
	    border: 1px solid #d8d8d8;
	}
	.productInfo{
		overflow: hidden;
		font-size: .26rem;
		padding:.27rem .35rem;
	}
	.productInfo img{
		float: left;
		width:1rem;
	}
	.productInfo .name{
		float: left;
		font-size: .25rem;
		text-align: left;
		width:5.2rem;
		color:#3b3c3c;
		margin-left: .17rem;
		line-height: 20px;
	}
	.logistics{
		background-color:#fff;
		margin-bottom: .18rem;
		padding-bottom: .35rem;
		font-size: 0;
	}
	.logistics .top{
		font-size: .26rem;
		overflow: hidden;
		padding:.27rem .35rem;
		border-bottom:1px solid #e8e8e8;
	}
	.logistics .top .left{
		float:left;
		color:#FC6E1A;
	}
	.logistics .top .right{
		float:right;
		color:#3b3c3c;
	}
</style>
