<template>
 	<div class="container">
 			<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
 		<div class="list1 " @click="showList">{{values}}<img src='../../../static/images/go.png'  class="imageO animation" /></div>
 		<div class="bg" :style="{display:[ resultShow ? 'none' : 'block']}"></div>
 		<div class="pickerO" :style="{display:[ resultShow ? 'none' : 'block']}">
 			<p class="top"><span @click="cancel">取消</span><span class='sure' @click="suredo">确定</span></p>
	  		<mt-picker :slots="slots" @change="onValuesChange"></mt-picker>
	  	</div>
	  <div class="fb-body">
	    <textarea v-model="introduct"  class="content" @input="contentInput()"  placeholder="对我们网站、商品、服务，你还有什么建议吗？你还希望在严选上买到什么？请告诉我们..." maxlength="500" auto-focus="true" />
	    <div class="text-count">{{introductLength}}/500</div>
	  </div>
	  <div class="fb-mobile">
	    <div class="label">手机号码</div>
	    <div class="mobile-box">
	      <input class="mobile"  v-model="inputValue" oninput="if(value.length > 11)value = value.slice(0, 11)"  type="number"  placeholder="方便我们与你联系"  />
	    </div>
	  </div>
		  <div class="fb-btn" @click="sbmitFeedback">提交</div>
	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Picker } from 'mint-ui';
	import { Toast } from 'mint-ui';
//	import headbar from '@/components/headbar.vue';
		
	export default {
	  name: 'feedback',
//	  components:{headbar},
	  data () {
	    return {
//	    	headFont:'问题反馈',
	    	values:'请选择反馈类型',
	    	values2:'',
	    	introduct:'',
	    	index:'',
	    	introductLength:'0',
		    contentLength:0,
		    inputValue:'',
		    resultShow:true,
		    slots: [{
		          flex: 1,
		          values: ['商品相关', '物流状况', '客户服务', '优惠活动', '功能异常', '产品建议', '其他'],
		          className: 'slot1',
		          textAlign: 'center'
		        }]
	    }
	  },
	  methods:{
	  	onValuesChange(picker, values) {
	  		this.values2=values[0];
	   },
	   cancel(){
	   		this.resultShow = true;
	   },
	   showList(){
	   		this.resultShow = false;
	   },
	   suredo(){
	   		this.values = this.values2;
	   		this.resultShow = true;
	   },
	   contentInput(){
	   		this.introductLength = this.introduct.length;
	   },
	   sbmitFeedback(){
	   	    let  that  = this;
		    if (this.values == '请选择反馈类型'){
		      Toast('请选择反馈类型');
		      return false;
		    }
		
		    if (this.introductLength == '0') {
		      Toast('请输入反馈内容');
		      return false;
		    }
		
		    if (this.inputValue == '') {
		      Toast('请输入手机号码');
		      return false;
		    }else{
		      if(this.inputValue.length != 11){
		        Toast('手机号格式有误');
		        return false;
		      }
		    }
		    that.$http({
			        method: 'post',
			        url:that.$url+ 'feedback/save',
			        data:{
			        	content:this.introduct,
			        	index:this.values,
			        	mobile:this.inputValue
			        }
		    	}).then(function (response) {
		    		if(response.data.errno == '1' ){
		    			that.$toast(response.data.errmsg);
		    		}else if(response.data.code == 500 ){
		    			that.$toast(response.data.msg);
		    		}
			 })
	   }
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--因加scoped会picker样式不可用-->
<style>
	.bg{
		width:100%;
		height:100%;
		position:fixed;
		top:0;
		background-color:rgba(0,0,0,0.5);
		z-index:1;
	}
	.list1{
		font-size:.3rem;
		overflow: hidden;
		text-align: left;
		background-color:#fff;
		padding:0.3rem .6rem;
	}
	.imageO{
	  width:.16rem;
	  height:.26rem;
	  float: right;
	}
	.pickerO{
		position: relative;
		z-index: 3;
	}
	.pickerO .top{
		position: fixed;
		bottom:3.4rem;
		overflow: hidden;
		font-size:.32rem;
		z-index: 2;
		width: 6rem;
	    margin: 0 auto;
	    left: 50%;
	    margin-left: -3rem;
	}
	.pickerO span{
		float: left;
	}
	.pickerO span.sure{
		float: right;
		color: royalblue;
	}
	.picker-items{
		position: fixed !important;
		bottom: 0;
		width:100%;	
		background-color: #fff;
		padding:.25rem 0 !important;
	}
	.picker-slot-wrapper{
		text-align: center;
	}
	.picker-item{
		text-align: center;
		font-size:.26rem;
	}
	.picker{
	  width:100%;
	  overflow:hidden;
	}
	.fb-type{
	  height: 1.04rem;
	  width: 100%;
	  background: #fff;
	  margin-bottom: .20rem;
	  display: flex;
	  flex-direction: row;
	  align-items: center;
	  padding-left: .30rem;
	  padding-right: .30rem;
	}
	
	.fb-type .type-label{
	  height: .36rem;
	  color: #333;
	  font-size: .28rem;
	}
	
	.fb-type .type-icon{
	  height: .36rem;
	  width: .36rem;
	}
	
	.fb-body{
	    background: #fff;
	    height: 3.74rem;
	    margin-top: .1rem;
	    width: 7.5rem;
	}
	textarea{
		outline: none;
	}
	.fb-body .content{
	      width: 7.2rem;
		    height: 100%;
		    color: #333;
		    line-height: .40rem;
		    font-size: .28rem;
	}
	
	.fb-body .text-count{
	  padding-top: .17rem;
	  line-height: .30rem;
	  float: right;
	  color: #666;
	  font-size: .26rem;
	  position: relative;
	  top: -.9rem;
	  right: .3rem;
	}
	
	.fb-mobile{
	  height: 1.62rem;
	  width: 100%;
	}
	
	.fb-mobile .label{
	  height: .58rem;
	  width: 7.2rem;
	  padding-top: .14rem;
	  color: #7f7f7f;
	  font-size: .24rem;
	  padding-left: .30rem;
	  text-align: left;
	}
	
	.fb-mobile .mobile-box{
	  height: 1.04rem;
	  color: #333;
	  padding-left: .30rem;
	  padding-right: .30rem;
	  font-size: .24rem;
	  background: #fff;
	  position: relative;
	}
	
	.fb-mobile .mobile{
		outline: none;
		width:6.5rem;
	  position: absolute;
	  top: .27rem;
	  left: .30rem;
	  height: .50rem;
	  color: #333;
	  line-height: .50rem;
	  font-size: .28rem;
	}
	
	.clear-icon{
	  position: absolute;
	  top: 43rem;
	  right: 30rem;
	  width: 28rem;
	  height: 28rem;
	}
	
	.fb-btn{
	  width: 100%;
	  height: .98rem;
	  line-height: .98rem;
	  background: #b4282d;
	  position: fixed;
	  bottom: 0;
	  left: 0;
	  border-radius: 0;
	  color: #fff;
	  font-size: .28rem;
	}
	.imageO.animation{
	   transform:rotate(90deg);
	}
</style>
