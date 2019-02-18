<template>
 	<div class='container'>
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  	
  	<div class="">
	  <div v-for="(item,index) in listArray"  @click='showList(index)' >
	    <div class="question">
	      <span>{{item.question}}</span>
	      <img :src="helpImg "  class="image"  :class="[item.show?'animation':'']"/>
	    </div>
	    <div class='answer' v-if="item.show">
	      {{item.answer}}
	    </div>
	  </div>
	  </div>
	  <returnhome :scrollshow = "scrollshow"></returnhome>
	</div> 
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import returnhome from '@/components/returnHome';
//	import headbar from '@/components/headbar.vue';	
		
export default {
  name: 'helpCenter',
	components:{returnhome},
  data () {
    return {
//  	headFont:'帮助中心',
		scrollshow:true,
    	helpImg:'../../static/images/go.png',
	    listArray: [
	      {
	        show: false,
	        question: "如何注册会员?",
	        answer: "登录微信小程序，点击登录授权使用读取微信信息，即可完成初步注册，用户完成手机绑定操作后方才完成注册;"
	      }
	    ]
    }
  },
  mounted(){
  	
  },
  methods:{
  	showList(userClickIndex){
	    this.listArray[userClickIndex].show = !this.listArray[userClickIndex].show;
  	}
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.question{
  padding:0 .30rem;
  background-color: #fff;
  margin-bottom: .05rem;  
  padding: .15rem .3rem;
  font-size:.28rem;
  overflow: hidden;
}
.question span{
	  float:left
}
.image{
  width:.16rem;
  height:.26rem;
  float: right;
}
.answer{
  font-size:.25rem;
  padding:0 .30rem 0 .50rem;
  text-align: justify;
  margin:.10rem 0;
}
.image.animation{
   transform:rotate(90deg);
}

</style>
