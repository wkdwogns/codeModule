package com.nexon;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.nexon.common.session.AuthInterceptor;
import com.nexon.common.util.RequestBodyXSSFileter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/page/admin/**")
                .addPathPatterns("/mapi/**")
                .excludePathPatterns("/health/check")
                .excludePathPatterns("/mapi/login")
                .excludePathPatterns("/mapi/logout")
                .excludePathPatterns("/page/admin/login");
    }

    @Bean
    public FilterRegistrationBean getRequestBodyXSSFileterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RequestBodyXSSFileter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssEscapeServletFilter());

        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");    //filter를 거칠 url patterns
        return registrationBean;
    }

}
