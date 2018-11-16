<template>
 	<div class="container">
 		<div class='list' v-for="item in userAccountDetail" >
		    <p class='ExchangeName'>{{ item.tranType }}</p>
		    <p class='boxMoney red' v-if=" item.tarnAmount>0 ">{{ item.tarnAmount }}</p>
		    <p class='boxMoney green' v-if=" item.tarnAmount<0 ">{{ item.tarnAmount }}</p>
		    <p>{{ item.createTime }}</p>
	 	 </div>
 	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
		
		
export default {
  name: 'coupon',
  data () {
    return {
    	userAccountDetail:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'user/userAccountDetail',
    	}).then(function (res) {
      		 res = {"data":{"code":1,"data":[{"id":29,"shopUserId":28,"tranFlag":0,"tranType":"充值","tarnAmount":1.00,"currentAmount":556.00,"createTime":1539840765000,"tradeNo":"201810181332107070084"},{"id":27,"shopUserId":28,"tranFlag":1,"tranType":"充值","tarnAmount":555.00,"currentAmount":555.00,"createTime":1539840637000,"tradeNo":"201810181330222227881"}]}};
      		 var res = res.data;
    		if(res.errno == '401' || res.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
    			for(var i=0; i<res.data.length; i++){
		          res.data[i].createTime = that.timestampToTime(res.data[i].createTime);
		          if (res.data[i].tranFlag == 1){
		            res.data[i].tarnAmount = '+' + res.data[i].tarnAmount;
		          }else{
		            res.data[i].tarnAmount = '-' + res.data[i].tarnAmount;
		          }
		        }
    			that.userAccountDetail = res.data;
    		}
		  })
  },
  methods:{
  	 	//时间戳转换成日期
	  timestampToTime:function (timestamp) {
	    var date = new Date(timestamp);
	    var Y = date.getFullYear() + '-';
	    var M = this.addTo(date.getMonth() + 1) + '-';
	    var D = this.addTo(date.getDate()) + ' ';
	    var h = date.getHours() + ':';
	    var m = date.getMinutes() + ':';
	    var s = date.getSeconds();
	    return Y + M + D + h + m + s;
	  },//月，日格式化
		addTo:function (e) {
	    if (e < 10) {
	      return '0' + e;
	    } else {
	      return e;
	    }
	  },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.list { 
	  padding: .20rem .50rem;
	  background-color: #fff;
	  margin-bottom:.05rem;
	  font-size:.29rem;
	  text-align: left;
	}
	.list .ExchangeName{
	  font-size:.30rem;
	}
	.list p.boxMoney{
	  text-align:right;
	  font-size:.30rem;
	}
	.container {
	  height:100%;
	  padding-top:.30rem;
	}
	.red{
	  color:#cc6666
	}
	.green{
	  color:#33cc99
	}
</style>
