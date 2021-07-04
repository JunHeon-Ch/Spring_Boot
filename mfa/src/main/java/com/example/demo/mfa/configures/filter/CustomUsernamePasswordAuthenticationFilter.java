package com.example.demo.mfa.configures.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;
    private SessionAuthenticationStrategy sessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy();
    private boolean continueChainBeforeSuccessfulAuthentication = false;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");

    public CustomUsernamePasswordAuthenticationFilter() {
        super();
    }

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    @Override
    public void setContinueChainBeforeSuccessfulAuthentication(boolean continueChainBeforeSuccessfulAuthentication) {
        this.continueChainBeforeSuccessfulAuthentication = continueChainBeforeSuccessfulAuthentication;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = this.obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.trim();
        String password = this.obtainPassword(request);
        password = (password != null) ? password : "";
        String otp = this.obtainOtp(request);
        otp = (otp != null) ? otp : "";
        otp = otp.trim();

        Object object = request.getSession().getAttribute("mfa");
        boolean mfa = (object == null) ? false : (boolean) object;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        if(mfa) {
            usernamePasswordAuthenticationToken.setDetails(otp);
        }

        return super.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    private String obtainOtp(HttpServletRequest request) {
        return (String) request.getParameter("otp");
    }
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("password");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("username");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if(!requiresAuthentication(httpServletRequest, httpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = attemptAuthentication(httpServletRequest, httpServletResponse);
            if(authentication == null) {
                return;
            }

            this.sessionAuthenticationStrategy.onAuthentication(authentication, httpServletRequest, httpServletResponse);

            if(this.continueChainBeforeSuccessfulAuthentication) {
                chain.doFilter(request, response);
            }

            successfulAuthentication(httpServletRequest, httpServletResponse, chain, authentication);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            unsuccessfulAuthentication(httpServletRequest, httpServletResponse, internalAuthenticationServiceException);
        } catch (AuthenticationException authenticationException) {
            unsuccessfulAuthentication(httpServletRequest, httpServletResponse, authenticationException);
        }
    }
}
