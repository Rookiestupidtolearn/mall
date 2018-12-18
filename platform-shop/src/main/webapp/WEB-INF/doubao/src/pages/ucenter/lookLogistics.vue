<template>
	<div class="container">
		<!--1个物流-->
		<div class="logistics" >
	 		<p class="top"><span class="left">{{description.shipment_name}}：{{description.shipment_order}}</span><span class="right">预计明天送达</span></p>
	 		<div class="productInfo">
	 			<img src="../../../static/images/detail_kefu.png" />
	 			<p class="name">绿豆糕 80克绿豆糕 80克绿糕 80克绿糕 80克绿豆糕克绿豆绿豆糕 80克糕 80克 （4枚入）</p>
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
	</div>
</template>

<script>
	export default {
	  name: 'logistics',
	  data () {
	    return {
	    	openD:true,
	    	description:[]
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
    		var res = {"order_key":"jd201812161613328621091","last_modify_time":"2018-12-17 14:27:05","shipment_order":"82455833754","logistics":[{"description":"您提交了订单，请等待系统确认(客户)","time":"2018-12-16 16:18:01"},{"description":"您的订单已经进入京东北京46号仓准备出库(系统)","time":"2018-12-16 16:22:48"},{"description":"您的订单预计12月17日09:00-15:00送达您手中(系统)","time":"2018-12-16 16:22:49"},{"description":"您的订单已经打印完毕(fanguanwei)","time":"2018-12-16 16:25:26"},{"description":"您的订单已经拣货完成(李真珂)","time":"2018-12-16 16:35:23"},{"description":"扫描员已经扫描(秦学雷)","time":"2018-12-16 16:38:46"},{"description":"打包成功(京东打包员)","time":"2018-12-16 16:46:09"},{"description":"您的订单在京东【北京园区-TJINB】分拣完成(李蒙)","time":"2018-12-16 16:51:06"},{"description":"您的订单在京东【北京园区-TJINB】发货完成，准备送往京东【北京通州分拣中心】(李蒙)","time":"2018-12-16 16:51:11"},{"description":"您的订单在京东【北京通州分拣中心】分拣完成(曹宝立)","time":"2018-12-16 19:27:36"},{"description":"您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京华润营业部】(曹宝立)","time":"2018-12-16 23:35:11"},{"description":"您的订单已到达【北京华润营业部】(颜立宁)","time":"2018-12-17 06:53:34"},{"description":"我是您的专属配送员董能光，您的订单已到达北京华润营业部，配送小哥会尽快为您配送，请您留意，如需要更改配送时间或者未如期到达，均可给我打电话18500096780，很高兴为您服务！(董能光)","time":"2018-12-17 08:30:01"},{"description":"您的订单已由本人签收（已在配送员处采用无纸化方式签收本订单）。感谢您在京东购物，欢迎再次光临。(董能光)","time":"2018-12-17 10:15:32"}],"shipment_name":"京东","status":"transit"};
//  		var res = res.data;
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
