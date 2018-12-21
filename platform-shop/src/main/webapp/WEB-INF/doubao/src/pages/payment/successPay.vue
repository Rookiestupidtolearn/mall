<template>
  <div class="failPay">
  	<h4>支付成功</h4>
  	<p>订单编号  {{paycard}}</p>
  	<p>支付金额  {{paymoney}}</p>
  	<div class="buttongroup">
  		<span @click="look">查看订单</span>
  		<span @click="success">完成</span>
  	</div>
  </div>
</template>

<script>
export default {
  name: 'successPay',
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
	 	success(){
	 		this.$router.push('/pages/shoppingcar')
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
		text-align: left;
		font-size:.28rem;
		color:#2C3E50;
		width:6rem;
		margin:0 auto;
		margin-top:.15rem;
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
