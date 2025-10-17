package com.polansky.amino.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class NoCacheHtmlFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isHtmlRequest(request)) {
            // Instruct browsers and proxies not to cache HTML, so users always get the latest index.html
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
            response.setHeader("Pragma", "no-cache")
            response.setDateHeader("Expires", 0)
        }
        filterChain.doFilter(request, response)
    }

    private fun isHtmlRequest(request: HttpServletRequest): Boolean {
        val uri = request.requestURI ?: ""
        // Root path typically serves index.html from classpath:/static
        val isRoot = uri == "/" || uri.isEmpty()
        val isHtmlPath = uri.endsWith(".html", ignoreCase = true)
        val accept = request.getHeader("Accept")?.lowercase() ?: ""
        val acceptsHtml = accept.contains("text/html") || accept.contains("application/xhtml+xml")
        return isRoot || isHtmlPath || acceptsHtml
    }
}
