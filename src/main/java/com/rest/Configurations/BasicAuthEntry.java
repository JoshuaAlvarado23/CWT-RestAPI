package com.rest.configurations;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthEntry extends BasicAuthenticationEntryPoint {
	
	@Override
	public void afterPropertiesSet() {
		setRealmName("BasicAuth");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.setContentType("application/json");
		response.setStatus(401);
		response.getOutputStream().print("{\"Status "+response.getStatus() +"\":\""+authException.getMessage()+"\"}");
	}
}
