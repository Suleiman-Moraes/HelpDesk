package br.com.senaigo.helpdesk.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter{

	private final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("HelpDesk-API | SimpleCORSFilter loaded");
	}
	
	@Override
	public void doFilter(ServletRequest requeste, ServletResponse respons, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) respons;
		HttpServletRequest request = (HttpServletRequest) requeste;
		response.setHeader("Acess-Control-Allow-Origin", "*");
		response.setHeader("Acess-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Acess-Control-Max-Age", "3600");
		response.setHeader("Acess-Control-Allow-Headers", 
				"x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
		
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			chain.doFilter(requeste, respons);
		}
	}
	
	@Override
	public void destroy() {}
}
