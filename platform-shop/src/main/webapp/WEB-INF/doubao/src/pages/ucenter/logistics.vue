<template>
	<div class="container">
		<!--多个物流-->
	 	<div class="logistics" v-for="(item,index) in description">
	 		<p class="top"><span class="left">{{item.shipmentName}}：{{item.shipmentOrder}}</span><span class="right">预计{{item.arrivalTime}}送达</span></p>
	 		<div class="productInfo">
	 			<img :src="item.goodUrl" />
	 			<p class="name">{{item.goodName}}</p>
	 		</div>
	 		<div class="btn" @click="looklog(item.orderId)">查看物流</div>
		</div>
	</div>
</template>

<script>
	export default {
	  name: 'logistics',
	  data () {
	    return {
	    	description:'',
	    	openD:true
	    }
	  },
	  mounted(){
	  	var productId = this.$route.query.id;
	  	this.loginst(productId);
	  },
	  methods:{
	  	loginst(productId){
	  		let that = this;
	  		that.$http({
	        method: 'post',
	        url:that.$url+ 'order/queryOrderLogistics.options',
	        data:{orderId:productId}
	    	}).then(function (res) {
	    		if(res.data.code==500){
	    			that.$toast(res.data.msg);
	    		}
	    		var res = res.data.orderLogistics;
	    		that.description  = res ;
			})
	  	},
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
