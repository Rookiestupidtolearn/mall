  import { MessageBox } from 'mint-ui';
  import router from '@/router/index.js'
  import cookie from '@/assets/cookie.js' 
  
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

/*登录公用方法*/
function userId(){
	if(cookie.getCookie('userId') == undefined || cookie.getCookie('userId') == null || cookie.getCookie('userId') == ''){
		return true;
	}else{
		return false;
	}
}
function userInfo(){
	if(cookie.getCookie('userInfo') == undefined || cookie.getCookie('userInfo') == null || cookie.getCookie('userInfo') == ''){
		return true;
	}else{
		return false;
	}
}
export default{
    goLogin:function(){
    	if(userId() && userInfo()){
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
}