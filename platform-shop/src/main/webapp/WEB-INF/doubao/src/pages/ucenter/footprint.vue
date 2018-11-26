<template>
 	<div class="footprint">
 			<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  	
  	<div class="">
	    <div class="day-item" v-for="item in footprintList" >
	      <div class="day-hd">{{item[0].add_time}}</div>
	      <div class="day-list">
	        <router-link :to="'/pages/goods/goods?id='+iitem.goods_id" class="item"  v-for = "iitem in item" >
	          <img class="img" :src="iitem.list_pic_url"/>
	          <div class="info">
	            <div class="name">{{iitem.name}}</div>
	            <!--<div class="subtitle">{{iitem.goods_brief}}</div>-->
	            <div class="price">￥{{iitem.market_price}}</div>
	          </div>
	        </router-link>
	      </div>
	    </div>
    </div>
  </div>
</template>

<script>
import { MessageBox } from 'mint-ui';
//import headbar from '@/components/headbar.vue';
		
export default {
name: 'footprint',
//components:{headbar},
  data () {
    return {
//  	headFont:'我的足迹',
    	footprintList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'footprint/list',
	        headers: {'X-Nideshop-Token':that.$cookie.getCookie('token')},
    	}).then(function (response) {
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			that.fontSize.goLogin()
    		}else{
    			console.log(response)
    			that.footprintList = response.data.data.data;
    		}
		  })
 	 }
}
</script>

 <!--Add "scoped" attribute to limit CSS to this component only--> 
<style scoped>
.footprint{
   height: auto;
  overflow: hidden;
  width: 100%;
  border-top: 1px solid #e1e1e1;
}

.day-item{
  height: auto;
  overflow: hidden;
  text-align: left;
  width: 100%;
  margin-bottom: .20rem;
}

.day-hd{
  height: .94rem;
  width: 100%;
  line-height: .94rem;
  background: #fff;
  padding-left: .30rem;
  color: #333;
  font-size: .28rem;
}

.day-list{
  width: 100%;
  height: auto;
  overflow: hidden;
  background: #fff;
  padding-left: .30rem;
  border-top: 1px solid #e1e1e1;
}

.item{
  display: block;
  height: 1.6rem;
  width: 7.20rem;
  background: #fff;
  padding: .30rem .30rem .30rem 0;
  border-bottom: 1px solid #e1e1e1;
}

.item:last-child{
  border-bottom: 1px solid #fff;
}

.item .img{
  float: left;
  width: 1.50rem;
  height: 1.50rem;
}

.item .info{
  float: left;
  width: 5.20rem;
  height: 1.50rem;
  padding-left: .20rem;
}

.item .info .name{
  font-size: .28rem;
  color: #333;
  line-height: .40rem;
}


.item .info .subtitle{
  margin-top: .08rem;
  font-size: .24rem;
  color: #888;
  line-height: .40rem;
}

.item .info .price{
  margin-top: .08rem;
  font-size: .28rem;
  color: #333;
  line-height: .40rem;
}

</style>
