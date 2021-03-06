package com.platform.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

public class MyRequestFilter implements Filter {
	
	private FilterConfig config = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}

	@Override
	 
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest hreq = (HttpServletRequest) req;
			HttpServletResponse hresp = (HttpServletResponse) resp;
			
			String responseHeafer = hreq.getHeader("Access-Control-Request-Headers");
			String responseOrigin = hreq.getHeader("Origin");
			String responseMets = hreq.getHeader("Access-Control-Allow-Methods");
			
			//跨域
			hresp.setHeader("Access-Control-Allow-Origin", responseOrigin);
			hresp.setHeader("Access-Control-Allow-Credentials","true");
			//跨域 Header
			hresp.setHeader("Access-Control-Allow-Methods", responseMets);
			hresp.setHeader("Access-Control-Allow-Headers", responseHeafer);
			// 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
			// 配置options的请求返回
			if (hreq.getMethod().equals("OPTIONS")) {
				hresp.setStatus(HttpStatus.SC_OK);
				// hresp.setContentLength(0);
				hresp.getWriter().write("OPTIONS returns OK");
	            return;
	        }
			// Filter 只是链式处理，请求依然转发到目的地址。
			chain.doFilter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
	}

}
