package com.imooc.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.imooc.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.imooc.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletException;

/**
 * 开启Mvc,自动注入spring容器，WebMvcConfigurerAdapter：配置视图解析器
 * WebMvcConfigurerAdapter已过时，推荐使用了实现接口WebMvcConfigurer
 * 当一个类实现了这个接口（ApplicationContextAware)之后，这个类就可以方便获得ApplicationContext中所有的bean
 */
@Configuration
//等价于<mvc:annotation-driven/>
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer, ApplicationContextAware {
    //spring容器
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        //这里的作用是只要路径有/upload就解析成后面file：的路径
        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/projectdev/image/upload/");
    }

    /**
     * 定义默认的请求处理器
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 创建viewResolver:视图解析器
     *
     * @return
     */
    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置Spring容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置视图解析的后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 创建文件上传解析器
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        //1024*1024*20=20M
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    /**
     * kaptcha参数
     */
    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.image.char.string}")
    private String cString;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;
    @Value("${kaptcha.noise.color}")
    private String nColor;
    @Value("${kaptcha.textproducer.char.length}")
    private String clength;
    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    /**
     * 由于web.xml不生效了，需要在这里配置kaptcha验证码Servlet
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),
                "/Kaptcha");
        servlet.addInitParameter("kaptcha.border",border);
        servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);
        servlet.addInitParameter("kaptcha.image.width",width);
        servlet.addInitParameter("kaptcha.image.char.string",cString);
        servlet.addInitParameter("kaptcha.image.height",height);
        servlet.addInitParameter("kaptcha.textproducer.font.size",fsize);
        servlet.addInitParameter("kaptcha.noise.color",nColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length",clength);
        servlet.addInitParameter("kaptcha.textproducer.font.names",fnames);
        return servlet;
    }
    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptPath = "/shopadmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截的路径
        loginIR.addPathPatterns(interceptPath);
        //还可以注册其他的拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截的路径
        permissionIR.addPathPatterns(interceptPath);
        //配置不拦截的路径
        /**shoplist page **/
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
        /**shopregister page **/
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
        /**shopmenage page**/
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
    }
}
