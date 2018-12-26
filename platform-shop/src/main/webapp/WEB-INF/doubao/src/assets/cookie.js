// 设置 cookie
	export default{
		setCookieTime:function(cookieName, userName){ //有时间的cookie失效
			var exp = new Date(); 
			exp.setTime(exp.getTime() + 30*60*1000); //毫秒
			document.cookie = cookieName + "="+ escape (userName) + ";expires=" + exp.toGMTString(); 
		},
		setCookie:function(cookieName, userName){
			 document.cookie = cookieName + '=' + escape(userName) ;
		},
		getCookie:function(cookieName){
			 if (document.cookie.length > 0) {

		        let c_start = document.cookie.indexOf(cookieName + '=');
		
		        if (c_start != -1) {
		            c_start = c_start + cookieName.length + 1;
		            let c_end = document.cookie.indexOf(';', c_start);
		            if (c_end == -1) c_end = document.cookie.length;
		            return unescape(document.cookie.substring(c_start, c_end));
		        };
		    };
		
		    return '';
		},
		delCookie:function(cookieName){
			this.setCookie(cookieName, '', -1) //调用了当前方法
		}
	}
