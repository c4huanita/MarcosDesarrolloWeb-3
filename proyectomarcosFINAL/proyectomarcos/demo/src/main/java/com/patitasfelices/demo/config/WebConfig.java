package com.patitasfelices.demo.config;

import com.patitasfelices.demo.service.ImagenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ImagenService imagenService;

    public WebConfig(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Las fotos subidas se sirven en vivo desde el disco (sin reiniciar la app)
        registry.addResourceHandler("/Img/**")
                .addResourceLocations(imagenService.getDirectorio().toUri().toString(), "classpath:/static/Img/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin", "/admin/**");
    }
}
