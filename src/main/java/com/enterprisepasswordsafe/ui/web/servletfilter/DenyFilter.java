/*
 * Copyright (c) 2017 Carbon Security Ltd. <opensource@carbonsecurity.co.uk>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

/*
 * Filter.java
 *
 * Created on 22 July 2003, 12:09
 */

package com.enterprisepasswordsafe.ui.web.servletfilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter to block access to protected resources.
 */

public final class DenyFilter implements Filter {
    /**
     * Initialise the filter by extracting the servlet context and storing it
     * for later use.
     *
     * @param config The configuration of the filter.
     */
    @Override
	public void init(final FilterConfig config) {
    }

    /**
     * Filter to check the user and session are valid before allowing access to
     * the main system.
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */

    @Override
	public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain next) throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;

        Logger.getAnonymousLogger().warning("Denied attempt to access "+((HttpServletRequest)request).getRequestURL());
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */

    @Override
	public void destroy() {
    	// No long-lasting resources.
    }
}
