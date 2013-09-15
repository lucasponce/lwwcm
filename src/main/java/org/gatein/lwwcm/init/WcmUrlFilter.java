package org.gatein.lwwcm.init;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/*
    Filter to redirect PortalURL with parameters
    @WebFilter(value = "/*")
 */
public class WcmUrlFilter implements Filter {

    private static final Logger log = Logger.getLogger(WcmUrlFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Init filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hReq = (HttpServletRequest)request;
        String subUrl = hReq.getRequestURI().substring(hReq.getContextPath().length());
        if (subUrl.startsWith("/rs") || subUrl.startsWith("/css") || subUrl.startsWith("/js") || subUrl.startsWith("/fonts")) {
            chain.doFilter(request, response);
        } else {
            hReq.getServletContext().getContext("/portal").getRequestDispatcher("/classic/sitemap").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        log.info("Destroy filter");
    }
}
