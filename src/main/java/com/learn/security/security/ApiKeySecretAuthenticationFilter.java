package com.learn.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class ApiKeySecretAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String HEADER_API_KEY = "API-Key";
    public static final String HEADER_API_SECRET = "API-Secret";

    public ApiKeySecretAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = getUsername(request);
        String password = getPassword(request);
        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password : "";
        if (StringUtils.hasText(username)) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return null;
        }
    }

    // Create a new implementation if API key & secret found in the Header then do the authentication
    // if not it will fall through into next filter (Basic HTTP Filter)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!requiresAuthentication(httpRequest, httpResponse)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Authentication authenticationResult = attemptAuthentication(httpRequest, httpResponse);
            if (authenticationResult == null) {
                chain.doFilter(request, response);
                return;
            }
            successfulAuthentication(httpRequest, httpResponse, chain, authenticationResult);
        } catch (InternalAuthenticationServiceException failed) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(httpRequest, httpResponse, failed);
        } catch (AuthenticationException ex) {
            // Authentication failed
            unsuccessfulAuthentication(httpRequest, httpResponse, ex);
        }
    }

    // Create a new implementation of successful authentication by preventing to redirect into root (/)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        securityContextHolderStrategy.setContext(context);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }

        // This part of code will redirect into root (/)
        // this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    // Create a new implementation of unsuccessful authentication by send response http 401 status code
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.getContextHolderStrategy().clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.logger.debug("Sending 401 Unauthorized error");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

        // This part of code will send response 401 into the http client
        // this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private String getUsername(HttpServletRequest request) {
        return request.getHeader(HEADER_API_KEY);
    }

    private String getPassword(HttpServletRequest request) {
        return request.getHeader(HEADER_API_SECRET);
    }

}
