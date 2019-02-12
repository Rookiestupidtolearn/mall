	import router from '@/router'
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
		},
		/*与android与ios的交互  给原生返回链接*/
		interactive:function(e){
			var appHref = window.location.href;
			var comHref = window.location.origin;
			if(appHref.indexOf('device')>-1){
				var device = appHref.substring(appHref.indexOf('device')).split('=')[1].split('&')[0];
			}
	    	if(device == 'android'){
	    			window.android.productDetail(comHref +'/#'+e); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref +'/#'+ e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		router.push(e);
	    	}
		}
	}
