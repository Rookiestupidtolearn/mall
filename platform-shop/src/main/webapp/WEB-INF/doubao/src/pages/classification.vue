<template>
  <div class="classification">
  	<!--头部返回-->
  	<!--<headbar :headFont = "headFont"></headbar>-->
  	
  	<!--主体内容-->
  	<div class="searchTop" @click="searchRoute">
  		<mt-search v-model="value"  cancel-text="取消"  placeholder="商品搜索" class="wusearch" ></mt-search>
  	</div>
  	<div class="content">
	  	<div class="catalog" >
	  		<p  v-for ="item in categoryList"  class = "item" :class="[item.id == currentCategory.id ?  'active'  :  '' ]"  @click="switchCate(item.id)">{{item.name}}</p>
	  	</div>
	  	<div class="rightca">
	  		<a href=""><img class="topb" :src="currentCategory.wap_banner_url" alt="" /></a>
	  		<p class="front_name">{{currentCategory.front_name}}</p>
	  		<div class="hd">
            <span class="line"></span>
            <span class="txt">{{currentCategory.name}}分类</span>
            <span class="line"></span>
        </div>
        <div class="bd">
        	<router-link :to="'/pages/category/category?id='+item.id" v-for="item in currentCategory.subCategoryList">
		        <img :src="item.wap_banner_url"/>
		       	<p>{{item.name}}</p>
	        </router-link>
        </div>
	  	</div>
  	</div>
  	<!--公用底部导航-->
  	<tabbar :selected="selected" :tabs='tabs'></tabbar> 
  </div>
</template>

<script>
	import tabbar from '@/components/tabbar.vue'
//	import headbar from '@/components/headbar'
	
export default {
	components: {tabbar },
  name: 'classification',
  data () {
    return {
    	value:'',
			categoryList:[],
			currentCategory:[],
//			headFont:'分类',
		selected:"classification",
   	 tabs:[require("../../static/images/ic_menu_choice_nor.png"),require("../../static/images/ic_menu_sort_pressed.png"),
          require("../../static/images/ic_menu_shoping_nor.png"),require("../../static/images/ic_menu_me_nor.png")],
    }
  },
  mounted(){
  	var that = this;   
  	//index
  		that.$http({
        method: 'post',
        url: that.$url+'catalog/index',
    	}).then(function (response) {
		    that.categoryList = response.data.data.categoryList,
		    that.currentCategory = response.data.data.currentCategory
		  })
  },
  methods:{
  	searchRoute(){
  		this.$router.push('/pages/ucenter/search');
  	},
  	switchCate(eventId){
	    var that = this;
	    that.$http({
        method: 'post',
        url: that.$url+'catalog/current',
				params:{ id : eventId  }
    	}).then(function (response) {
		    that.currentCategory = response.data.data.currentCategory
		  })
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style scoped>
	.searchTop{
		position: fixed;
    width: 100%;
    top: 0;
    z-index: 2;
	}
	.front_name {
		position:absolute;
		top:0rem;
		text-align:center;
		color:#fff;
		font-size:.28rem;
		left:0;
		height:1.92rem;
		line-height:1.92rem;
		width:100%;
		overflow: hidden;
}

	.catalog 	.active{
		border-left:.06rem solid #ab2b2b;
		color:#ab2b2b;
		font-size:.28rem;
	}
	.bd {
	 	overflow: hidden;
	 	margin-top: .3rem;
	 	width: 5.5rem;
	 	padding-bottom:.5rem
	 }
 .bd a {
		display:block;
		float:left;
		height:2.16rem;
		width:1.44rem;
		margin-right:.34rem;
    margin-top: .06rem;
}
.bd a img {
	width:1.44rem;
	height:1.44rem;
}
.bd a p{
	text-align:center;
	font-size:.24rem;
	color:#333;
	height:.72rem;
	width:1.44rem;
	overflow: hidden;
 text-overflow: ellipsis;
 white-space: nowrap;
}
	.topb{
		height: 1.92rem;
		width: 100%;
	}
	.hd .line {
		display: inline-block;
	width:.40rem;
	height:1px;
	background:#d9d9d9;
}
.hd{
	font-size: 0.24rem;
}
.hd .txt {
font-size:.24rem;
text-align:center;
color:#333;
padding:0 .10rem;
width:auto;
	vertical-align: -webkit-baseline-middle;
}
	.rightca{
		float:left;
		border-left:1px solid #fafafa;
		height: 11rem;
		padding:0 .30rem 0 .30rem;
		width: 5.2rem;
		margin-top: 0.3rem;
		position: relative;
		overflow-y: scroll;
	}
	.item{
		text-align:center;
		line-height:.90rem;
		width:1.62rem;
		height:.90rem;
		color:#333;
		font-size:.28rem;
		overflow: hidden;
	}
		.wusearch{
			font-size:.28rem !important;
			height:100%;
		}
		.content{
			overflow: hidden;
   	 	padding-top: 1.2rem;
    	height: 11rem;
    	background-color: #fff;
		}
		.content .catalog{
			float:left;
			height:11rem;
			width:1.62rem;
			overflow-y:scroll ;
			overflow-x:hidden ;
		}
</style>
