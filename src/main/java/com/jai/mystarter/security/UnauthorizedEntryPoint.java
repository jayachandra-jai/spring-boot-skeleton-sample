package com.jai.mystarter.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		System.out.println(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}