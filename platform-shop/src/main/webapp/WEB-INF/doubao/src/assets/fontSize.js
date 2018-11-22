  import { MessageBox } from 'mint-ui';
  import router from '@/router/index.js'
  
  var html = document.getElementsByTagName("html")[0];
    function getFontSize(){
        var width=document.documentElement.clientWidth
            ||document.body.clientWidth+"px";
        var fontSize=(100/750)*width;
        if(width>=750){
            fontSize="100";
        }
        return fontSize;
    }
    html.style.fontSize=getFontSize()+"px";
    window.onresize=function(){
        setTimeout(function(){
            html.style.fontSize=getFontSize()+"px";
        },100)
    };

/*去登录*/
export default{
    goLogin:function(){
    	MessageBox({
			  title: ' ',
			  message: '请先登录 ',
			  showCancelButton: true
		},function(params){
				if(params == 'confirm'){
						router.push('/pages/register/register');
				}
		});
    }
}
