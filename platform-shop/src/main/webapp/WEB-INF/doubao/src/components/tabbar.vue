<template>
		<div class="container">
		  	<!--底部导航栏-->
				<mt-tabbar v-model="message" fixed>
	        <mt-tab-item id="home">
	            <img slot="icon" :src="this.atabs[0]">
	            首页
	        </mt-tab-item>
	        <mt-tab-item id="classification">
	            <img slot="icon" v-bind:src="this.atabs[1]">
	            分类
	        </mt-tab-item>
	        <mt-tab-item id="shoppingcar">
	            <img slot="icon" v-bind:src="this.atabs[2]">
	            购物车
	        </mt-tab-item>
	        <mt-tab-item id="ucenter">
	            <img slot="icon" v-bind:src="this.atabs[3]">
	            我的
	        </mt-tab-item>
				</mt-tabbar>
		</div>
</template>

<script>
	
export default {
  name: 'tabbar',
  data () {
    return {
    		message:this.selected,
			  atabs:this.tabs
    }
  },
   props:{
   			selected: String,
        tabs:Array,
    },
    watch: {
        message: function (val, oldVal) {
            switch(val){
                case 'home':
                    this.$router.push('/');
                break;
                case 'classification':
                    this.$router.push('/classification');
                break;
                case 'shoppingcar':
                    this.$router.push('/shoppingcar');
                break;
                case 'ucenter':
                    this.$router.push('/ucenter');
                break;
            }
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style>
	.mint-tabbar > .mint-tab-item.is-selected{
		color:#b4282d;
	}
	.mint-tabbar.is-fixed{
		height:1.2rem;
	}
</style>
