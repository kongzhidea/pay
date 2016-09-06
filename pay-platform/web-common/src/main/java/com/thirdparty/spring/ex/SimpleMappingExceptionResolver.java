
package com.thirdparty.spring.ex;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;


public class SimpleMappingExceptionResolver extends AbstractHandlerExceptionResolver {
    private Log logger = LogFactory.getLog(SimpleMappingExceptionResolver.class);

    public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
    private Properties exceptionMappings;
    private Class<?>[] excludedExceptions;
    private String defaultErrorView;
    private Integer defaultStatusCode;
    private Map<String, Integer> statusCodes = new HashMap();
    private String exceptionAttribute = "exception";

    public SimpleMappingExceptionResolver() {
    }

    public void setExceptionMappings(Properties mappings) {
        this.exceptionMappings = mappings;
    }

    public void setExcludedExceptions(Class... excludedExceptions) {
        this.excludedExceptions = excludedExceptions;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    public void setStatusCodes(Properties statusCodes) {
        Enumeration enumeration = statusCodes.propertyNames();

        while (enumeration.hasMoreElements()) {
            String viewName = (String) enumeration.nextElement();
            Integer statusCode = new Integer(statusCodes.getProperty(viewName));
            this.statusCodes.put(viewName, statusCode);
        }

    }

    public void addStatusCode(String viewName, int statusCode) {
        this.statusCodes.put(viewName, Integer.valueOf(statusCode));
    }

    public Map<String, Integer> getStatusCodesAsMap() {
        return Collections.unmodifiableMap(this.statusCodes);
    }

    public void setDefaultStatusCode(int defaultStatusCode) {
        this.defaultStatusCode = Integer.valueOf(defaultStatusCode);
    }

    public void setExceptionAttribute(String exceptionAttribute) {
        this.exceptionAttribute = exceptionAttribute;
    }

    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String viewName = this.determineViewName(ex, request);
        if (viewName != null) {
            Integer statusCode = this.determineStatusCode(request, viewName);
            if (statusCode != null) {
                this.applyStatusCodeIfPossible(request, response, statusCode.intValue());
            }
            logger.error(ex.getMessage(), ex); // ！！！ 打印异常栈信息

            return this.getModelAndView(viewName, ex, request);
        } else {
            return null;
        }
    }

    protected String determineViewName(Exception ex, HttpServletRequest request) {
        String viewName = null;
        if (this.excludedExceptions != null) {
            Class[] var4 = this.excludedExceptions;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Class excludedEx = var4[var6];
                if (excludedEx.equals(ex.getClass())) {
                    return null;
                }
            }
        }

        if (this.exceptionMappings != null) {
            viewName = this.findMatchingViewName(this.exceptionMappings, ex);
        }

        if (viewName == null && this.defaultErrorView != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Resolving to default view \'" + this.defaultErrorView + "\' for exception of type [" + ex.getClass().getName() + "]");
            }

            viewName = this.defaultErrorView;
        }

        return viewName;
    }

    protected String findMatchingViewName(Properties exceptionMappings, Exception ex) {
        String viewName = null;
        String dominantMapping = null;
        int deepest = 2147483647;
        Enumeration names = exceptionMappings.propertyNames();

        while (true) {
            String exceptionMapping;
            int depth;
            do {
                do {
                    if (!names.hasMoreElements()) {
                        if (viewName != null && this.logger.isDebugEnabled()) {
                            this.logger.debug("Resolving to view \'" + viewName + "\' for exception of type [" + ex.getClass().getName() + "], based on exception mapping [" + dominantMapping + "]");
                        }

                        return viewName;
                    }

                    exceptionMapping = (String) names.nextElement();
                    depth = this.getDepth(exceptionMapping, ex);
                } while (depth < 0);
            }
            while (depth >= deepest && (depth != deepest || dominantMapping == null || exceptionMapping.length() <= dominantMapping.length()));

            deepest = depth;
            dominantMapping = exceptionMapping;
            viewName = exceptionMappings.getProperty(exceptionMapping);
        }
    }

    protected int getDepth(String exceptionMapping, Exception ex) {
        return this.getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
        return exceptionClass.getName().contains(exceptionMapping) ? depth : (exceptionClass == Throwable.class ? -1 : this.getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1));
    }

    protected Integer determineStatusCode(HttpServletRequest request, String viewName) {
        return this.statusCodes.containsKey(viewName) ? (Integer) this.statusCodes.get(viewName) : this.defaultStatusCode;
    }

    protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        if (!WebUtils.isIncludeRequest(request)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Applying HTTP status code " + statusCode);
            }

            response.setStatus(statusCode);
            request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(statusCode));
        }

    }

    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
        return this.getModelAndView(viewName, ex);
    }

    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        if (this.exceptionAttribute != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Exposing Exception as model attribute \'" + this.exceptionAttribute + "\'");
            }

            mv.addObject(this.exceptionAttribute, ex);
        }

        return mv;
    }
}
