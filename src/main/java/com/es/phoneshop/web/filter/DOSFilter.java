package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.impl.DosProtectionServiceImpl;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DOSFilter implements Filter {
    private static final int HTTP_STATUS_TOO_MANY_REQUESTS = 429;

    private static DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = DosProtectionServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).sendError(HTTP_STATUS_TOO_MANY_REQUESTS);
        }
    }

    @Override
    public void destroy() {
    }
}
