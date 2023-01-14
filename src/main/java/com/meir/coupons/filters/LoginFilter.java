package com.meir.coupons.filters;


import com.meir.coupons.entity.UserEntity;
import com.meir.coupons.enums.ErrorType;
import com.meir.coupons.enums.UserTypes;
import com.meir.coupons.exceptions.ApplicationException;
import com.meir.coupons.repository.IUserRepository;
import com.meir.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(2)
public class LoginFilter implements Filter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(httpRequest, response);
            return;
        }

        String url = httpRequest.getRequestURL().toString();

        if (url.endsWith("/users") && httpRequest.getMethod().equals("POST")) {
            chain.doFilter(httpRequest, response);
            return;
        }

        if (url.endsWith("/login")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.endsWith("/coupons/") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.endsWith("/categories/") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.endsWith("/companies/") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.endsWith("/customers") && httpRequest.getMethod().equals("POST")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.endsWith("/coupons/size") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.contains("coupons/byPagesAndSorting") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        if (url.contains("coupons/paginationAndFilterBySort") && httpRequest.getMethod().equals("GET")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        String token = httpRequest.getHeader("Authorization");
        try {
            UserEntity userEntity = userRepository.findById(JWTUtils.validateToken(token));
            if (url.contains("companies") && userEntity.getUserType() == UserTypes.customer) {
                throw new ApplicationException(ErrorType.NO_AUTHORIZED);
            }
            chain.doFilter(httpRequest, response);
        } catch (Exception e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            int unAuthorizedError = 401;
            httpResponse.setStatus(unAuthorizedError);
        }
    }
}


//        LoggedInUserData loggedInUserData = (LoggedInUserData) contrroler.get(token);
//        if(token != null){
//            request.setAttribute("userData", loggedInUserData);
//            chain.doFilter(request, response);
//            return;
//        }