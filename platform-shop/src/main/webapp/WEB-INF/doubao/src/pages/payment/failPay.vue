<template>
  <div class="successPay">
  	<h4>付款失败</h4>
  	<p>请在<b>1小时</b>内完成付款</p>
  	<p>否则订单将会被系统取消</p>
  	<div class="buttongroup">
  		<span @click="look">查看订单</span>
  		<span @click="pay">重新付款</span>
  	</div>
  </div>
</template>

<script>
export default {
  name: 'failPay',
  data () {
    return {
    	paymoney:'12345678910',
    	paycard:'111.11元'
    }
  },
  mounted(){
  		
	 },
	 methods:{
 		look(){
	 		
	 	},
	 	pay(){
	 		
	 	}
	 		
 	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	h4{
		font-size:.38rem;
		color:#a3585a;
		margin:.8rem 0 .5rem 0;
		font-weight: normal;
	}
	p{
		font-size:.28rem;
		color:#2C3E50;
		width:6rem;
		margin:0 auto;
		margin-top:.15rem;
	}
	p b{
		font-size:.34rem;
		font-weight: normal;
	}
	.buttongroup{
		margin-top:.6rem;
	}
	.buttongroup span{
		display: inline-block;
		width:1.8rem;
		height:.65rem;
		line-height:.65rem;
		border:1px solid #838383;
		font-size:.28rem;
	}
</style>
