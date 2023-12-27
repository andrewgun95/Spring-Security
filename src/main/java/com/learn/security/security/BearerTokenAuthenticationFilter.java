package com.learn.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
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


// Bearer Token is authentication by supplying a Credential Token (contain a User Information) on the Authorization Header
public class BearerTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String TOKEN_DELIMITER = ":";

    public BearerTokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            String username = getUsernameFromToken(token);
            String password = getPasswordFromToken(token);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        securityContextHolderStrategy.setContext(context);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.getContextHolderStrategy().clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.logger.debug("Sending 401 Unauthorized error");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    private String getUsernameFromToken(String token) throws AuthenticationException {
        String[] result = token.split(TOKEN_DELIMITER);
        if (result.length > 0) {
            return result[0];
        } else {
            throw new AccountExpiredException("Illegal Token : Username is Not Found");
        }
    }

    private String getPasswordFromToken(String token) throws AuthenticationException {
        String[] result = token.split(TOKEN_DELIMITER);
        if (result.length > 1) {
            return result[1];
        } else {
            throw new AccountExpiredException("Illegal Token : Password is Not Found");
        }
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return "";
    }
}
