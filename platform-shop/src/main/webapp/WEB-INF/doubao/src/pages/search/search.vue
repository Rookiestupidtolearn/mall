<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
	 	<div class="searchTop" >
	 		<form action="" target="frameFile">
	  			<mt-search v-model="value"  @keyup.enter.native="showList" @input="inputFocus" cancel-text="取消"  placeholder="商品搜索" class="wusearch" ></mt-search>
	  		</form>
	  	</div>
	  	<div class="no-search" :style="{display: [value || defaultKeyword? 'none' : 'block']}">
	  		<!--:style="{display: [value ? 'none' : 'block']}"-->
		     <div class="serach-keywords search-history" >
			    <div class="h" >
			      <span class="title">历史记录</span>
			      <img class="icon"  @click="clearHistory"  src="http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/del1-93f0a4add4.png"></image>
			    </div>
			    <div class="b">
			      <div class="item"  v-for="item in historyKeyword" @click="onKeywordTap(item)" hover-class="navigator-hover">{{item}}</div>
			    </div>
	 	 	</div>
 		</div>
 		<div class="serach-keywords search-hot" :style="{display: [value ? 'none' : 'block']}">
		    <div class="h">
		      <p class="title">热门搜索</p>
		    </div>
		    <div class="b">
		      <div class="item" hover-class="navigator-hover" v-for="item in hotKeyword">{{item}}</div>
		    </div>
	  	</div>
 		<div class="search-result"  :data-c="searchStatus"  :data-d="goodsList.length"  v-if="searchStatus && goodsList.length" >
		    <div class="sort ">
			    <div class="sort-box">
				      <div  class="item" :class="[currentSortType == 'default' ? 'active' : '']" @click="openSortFilter('defaultSort')" >
				        <span class="txt">综合</span>
				      </div>
				      <div class="item by-price"  :class="{active : currentSortType == 'price',asc : currentSortOrder == 'asc',desc : currentSortOrder !== 'asc'}" @click="openSortFilter('priceSort')" >
				        <span class="txt">价格</span>
				      </div>
				      <div class="item"  :class=" [currentSortType == 'category' ? 'active' : '']" @click="openSortFilter('categoryFilter')">
				        <span class="txt">分类</span>
				      </div>
			    </div>
			    <!--分类类别-->
			    <div class="sort-box-category" v-if="categoryFilter" >
			      	<div class="item"  :class="[item.checked ? 'active' : '']" v-for="(item,index) in filterCategory" @click="selectCategory(index)">{{item.name}}</div>
			    </div>
			    <!--商品渲染-->
			    <div class="cate-item">
				    <div class="b">
				      <router-link class="item" :class=" [(iindex + 1) % 2 == 0 ? 'item-b' : '']" :to="'/pages/goods/goods?id='+iitem.id" v-for="(iitem,iindex) in goodsList" >
				        <img class="img" :src="iitem.list_pic_url" background-size="cover"/>
				        <p class="name">{{iitem.name}}</p>
				        <p class="price">￥{{iitem.market_price}}</p>
				      </router-link>
				    </div>
				  </div>
			</div>
		 </div>
 		<div class="search-result-empty" :data-c="goodsList.length"  :data-d="searchStatus" v-if="!goodsList.length && searchStatus">
		    <img class="icon" src="http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/noSearchResult-7572a94f32.png"/>
		    <p class="text">您寻找的商品还未上架</p>
		 </div>
 	</div>
</template>

<script>
//	import headbar from '@/components/headbar.vue'
export default {
	  name: 'search',
//	  components:{headbar},
	  data () {
	    return {
    		value:'',
    		searchStatus:false,
//  		headFont:'搜索',
    		historyKeyword: [],
    		defaultKeyword: {},
   			hotKeyword: [],
   			currentSortType: 'default',
    		currentSortOrder: '',
    		page: 1,
    		size: 20,
    		categoryFilter: false,
    		goodsList:[],
    		filterCategory: [],
    		currentSortType: 'id',
    		currentSortOrder: 'desc',
    		categoryId: 0
	    }
	  },
	  mounted(){
			this.getKeyWordList();
	  },
	  methods:{
	  	onKeywordTap(keyword){
	  		this.getSearchResult(keyword);
	  	},
	  	getSearchResult(keyword) {
		    this.value = keyword;
		    this.page = 1;
		    this.categoryId = 0;
		    this.goodsList = [];
		
		    this.showList();
		  },
	  	selectCategory(currentIndex){
		    let filterCategory = this.filterCategory;
		    let currentCategory = null;
		    for (let key in filterCategory) {
		      if (key == currentIndex) {
		        filterCategory[key].selected = true;
		        currentCategory = filterCategory[key];
		      } else {
		        filterCategory[key].selected = false;
		      }
		    }
		      this.filterCategory = filterCategory;
		      this.categoryFilter = false;
		      this.categoryId = currentCategory.id;
		      this.page = 1;
		      this.goodsList = [];
		      this.showList();
	  	},
	  	openSortFilter(currentId){
		    switch (currentId) {
		      case 'categoryFilter':
		          this.categoryFilter =  !this.categoryFilter;
		          this.currentSortOrder = this.currentSortOrder;
		        	break;
		      case 'priceSort':
		        let tmpSortOrder = 'asc';
		        if (this.currentSortOrder == 'asc') {
		          tmpSortOrder = 'desc';
		        }
		        console.log(tmpSortOrder);
		          this.currentSortType =  'price';
		          this.currentSortOrder =   tmpSortOrder;
		          this.categoryFilter =   false;
		          this.showList();
		        break;
		      default:
		        //综合排序
		         this.currentSortType =  'default';
		         this.currentSortOrder = 'desc';
		         this.categoryFilter = false;
		         this.showList();
		    }
	  	},
	  	getKeyWordList(){
	  		let that = this;
	  		that.$http({
	        method: 'post',
	        url: that.$url+'search/index',
	    	}).then(function (response) {
	    		var  response = response.data;
    			that.historyKeyword = response.data.historyKeywordList,
      			that.defaultKeyword = response.data.defaultKeyword,
      			that.hotKeyword =  response.data.hotKeywordList
			})
	  	},
	  	clearHistory(){
	  		let that = this;
	  		 this.historyKeyword =  [],
	  		   this.$http({
			        method: 'post',
			        url: this.$url+'search/clearhistory',
		    	}).then(function (response) {
		    			console.log(response.data);
		    			console.log('清除成功');
				})
	  	},
	  	inputFocus(){
	  		var that = this;
      		that.searchStatus =  false,
			   that.$http({
			        method: 'post',
			        url: that.$url+'search/helper',
			        params:{keyword:that.value}
		    	}).then(function (response) {
		    			console.log(response.data);
				})
	  	},
	  	showList(){
	  		let that = this;
//	  		获取商品列表
	  		that.$http({
		        method: 'post',
		        url: that.$url+'goods/list',
		        params:{
		        	keyword:that.value,
		        	page:that.page,
		        	size:that.size,
		        	sort:that.currentSortType,
		        	order:that.currentSortOrder,
		        	categoryId:that.categoryId
		        }
	    	}).then(function (response) {
	    		var response = response.data;
	    		 that.searchStatus = true;
		          that.categoryFilter = false;
		          that.goodsList = response.data.goodsList;
		          that.filterCategory = response.data.filterCategory;
		          that.page = response.data.currentPage;
		          that.size =  response.data.numsPerPage
			})
	    	that.getKeyWordList();
	  	}
	  	
	 }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wusearch{
	font-size:.28rem !important;
	height:100%;
}
.no-search{
    height: auto;
    overflow: hidden;
}
.serach-keywords{
    background: #fff;
    width: 7.50rem;
    height: auto;
    overflow: hidden;
    margin-bottom: .20rem;
}
.serach-keywords .h{
    padding: 0 .3125rem;
    height: .93rem;
    line-height: .93rem;
    width: 100%;
    color: #999;
    font-size: .29rem;
}
.serach-keywords .title{
    display: block;
    width: 1.20rem;
    float: left;
    color:#333;
}
.serach-keywords .icon{
    margin-top: .19rem;
    float: right;
    display: block;
    margin-right: .51rem;
    height: .55rem;
    width: .55rem;
}
.serach-keywords .b{
    width: 7.50rem;
    height: auto;
    overflow: hidden;
    padding-left: .3125rem;
    font-size: 0;
    text-align: left;
}
.serach-keywords .item{
    display: inline-block;
    width: auto;
    height: .48rem;
    line-height: .48rem;
    padding:0 .15rem;
    border: 1px solid #999;
    margin: 0 .3125rem .3125rem 0;
    font-size: .24rem;
    color: #333;
}
.serach-keywords .item.active{
    color: #b4282d;
    border: 1px solid #b4282d;
}
.shelper-list{
    width: 7.50rem;
    height: auto;
    /*overflow: hidden;*/
    background: #fff;
    padding: 0 .3125rem;
}
.shelper-list .item{
    height: .93rem;
    width: 6.875rem;
    line-height: .93rem;
    font-size: .24rem;
    color: #333;
    border-bottom: 1px solid #f4f4f4;
}
.sort{
    position: fixed;
    top: 1rem;
    background: #fff;
    width: 100%;
    height: .78rem;
}
.sort-box{
	display: flex;
    background: #fff;
    width: 100%;
    height: .78rem;
    overflow: hidden;
    padding: 0 .30rem;
    border-bottom: 1px solid #d9d9d9;
}
.sort-box .item{
    height: .78rem;
    line-height: .78rem;
    text-align: center;
    color: #333;
    font-size: .30rem;
    flex:1;
}
.sort-box .item .txt{
    display: block;
    width: 100%;
    height: 100%;
    color: #333;
}
.sort-box .item.active .txt{
    color: #b4282d;
}
.sort-box .item.by-price{
    background: url(//yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/no-3127092a69.png) 1.55rem center no-repeat;
    background-size: .15rem .21rem;
}
.sort-box .item.by-price.active.asc{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/up-636b92c0a5.png) 1.55rem center no-repeat;
    background-size: .15rem .21rem;
}
.sort-box .item.by-price.active.desc{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/down-95e035f3e5.png) 1.55rem center no-repeat;
    background-size: .15rem .21rem;
}
.sort-box-category{
    background: #fff;
    width: 100%;
    height: auto;
    overflow: hidden;
    padding: .40rem .40rem 0 0;
    border-bottom: 1px solid #d9d9d9;
}
.sort-box-category .item{
    height: .54rem;
    line-height: .54rem;
    text-align: center;
    float: left;
    padding: 0 .16rem;
    margin: 0 0 .40rem .40rem;
    border: 1px solid #666;
    color: #333;
    font-size: .24rem;
}
.sort-box-category .item.active{
    color: #b4282d;
    border: 1px solid #b4282d;
}

.cate-item{
    margin-top: .18rem;
    height: 12rem;
    overflow-y: scroll;
}

.cate-item .h{
    height: 1.45rem;
    width: 7.50rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.cate-item .h .name{
    display: block;
    height: .35rem;
    margin-bottom: .18rem;
    font-size: .30rem;
    color: #333;
}

.cate-item .h .desc{
    display: block;
    height: .24rem;
    font-size: .24rem;
    color: #999;
}

.cate-item .b{
  width: 7.40rem;
	padding: .05rem;
  height: auto;
  /*overflow: hidden;*/
}

.cate-item .list-filter{
    height: .80rem;
    width: 100%;
    background: #fff;
    margin-bottom: .0625rem;
}

.cate-item .b .item{
  float: left;
  background: #fff;
  width: 3.65rem;
  margin-bottom: .0625rem;
  padding-bottom: .33rem;
  height: auto;
  overflow: hidden;
  text-align: center;
}
.cate-item .b .item-b{
  margin-left: .0625rem;
}
.cate-item .b .item:last-child{
	margin-bottom:3.5rem !important;
}
.cate-item .item .img{
  width: 3.02rem;
}
.cate-item .item .name{
  display: block;
    width: 3.65rem;
    text-align: center;
    overflow: hidden;
    font-size: .30rem;
    color: #333;
    margin-bottom: .15rem;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.cate-item .item .price{
  display: block;
  width: 3.65rem;
  height: .30rem;
  text-align: center;
  font-size: .30rem;
  color: #b4282d;
}

.search-result-empty{
    width: 100%;
    height: 100%;
    padding-top: 3.00rem;
}

.search-result-empty .icon{
    margin: 0 auto;
    display: block;
    width: 2.40rem;
    height: 2.40rem;
}

.search-result-empty .text{
    display: block;
    width: 100%;
    height: .40rem;
    font-size: .28rem;
    text-align: center;
    color: #999;
}
</style>
