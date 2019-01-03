<template>
	<div class="container">
		<!--1个物流-->
		<div class="logistics" >
	 		<p class="top"><span class="left">{{description.shipment_name}}：{{description.shipment_order}}</span><span class="right">预计{{description.arrivalTime}}送达</span></p>
	 		<div class="productInfo">
	 			<img :src="description.goods[0].img" />
	 			<p class="name">{{description.goods[0].goodsName}}</p>
	 		</div>
	 		<!--<div class="btn" >查看物流</div>-->
		</div>
		<div class="logisticsStatus">
			<p class="top"><span >物流状态</span></p>
			<div class="progress">
				<div :class="openD ? 'shanshou' : ' ' " >
					<div class="second overflow" v-for="(item,index) in description.logistics">
						<div class="left top45">
							<h4>{{(item.time).substring(5,(item.time).length-3)}}</h4>
						</div>
						<div class="middle top45">
							<img src="../../../static/images/logistics/iconcircle.png" class="iconcircle"/>
						</div>
						<div class="right top45">
							<p>{{item.description}}</p>
						</div>
					</div>
				</div>
				<div class="bottom" @click="open" v-if="openD">展开<img src="../../../static/images/logistics/xjt.png"/></div>
				<div class="bottom" @click="close" v-else>收起<img src="../../../static/images/logistics/tjt.png"/></div>
			</div>
		</div>
		<returnhome :scrollshow = "scrollshow"></returnhome>
	</div>
</template>

<script>
	import returnhome from '@/components/returnHome';
	
	export default {
	  name: 'logistics',
	   components:{returnhome},
	  data () {
	    return {
	    	scrollshow:true,
	    	openD:true,
	    	description:''
	    }
	  },
	  mounted(){
	  	let that = this;
	  	var productId = this.$route.query.id;
	  
	  	that.$http({
	        method: 'post',
	        url:that.$url+ 'order/queryLogistics.options',
	        data:{orderId:productId}
    	}).then(function (res) {
    		var res = res.data;
    		that.description  = res ;
		})
	  	
	  },
	  methods:{
	  	open(){
	  		this.openD = !this.openD;
	  	},
	  	close(){
	  		this.openD = !this.openD;
	  	}
       }
	}
</script>

<style scoped>
	.shanshou{
		height:2.34rem;
		overflow: hidden;
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
		/*height: .1rem;*/
	}
	.progress .left h4{
		font-size: .24rem;
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
   		left: .21rem;
	}
	.progress .middle .iconcircle{
		width:.1rem;
		padding:.16rem;
	}
	.progress .right{
		float:left;
		font-size:.26rem;
		color:#3c3c3c;
		padding-left: .4rem;
		width:4.6rem;
		border-left:1px dashed #cbcbcb;
	}
	.progress .final{
		color:#999999;
	}
	.progress .right p{
		font-size:.24rem;
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
