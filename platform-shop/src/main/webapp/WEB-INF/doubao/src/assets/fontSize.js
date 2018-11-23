  import { MessageBox } from 'mint-ui';
  import router from '@/router/index.js'
  import { setCookie,getCookie,delCookie } from '@/assets/cookie';
  
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
function userId(){
	if(getCookie('userId') == undefined || getCookie('userId') == null || getCookie('userId') == ''){
		return true;
	}else{
		return false;
	}
}
function userInfo(){
	if(getCookie('userInfo') == undefined || getCookie('userInfo') == null || getCookie('userInfo') == ''){
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