package com.mjolnir.yggdrasil.config;

import com.mjolnir.yggdrasil.security.MjolnirInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Deprecated
@Configuration
public class MjolnirConfig implements WebMvcConfigurer {

    private final MjolnirInterceptor mjolnirInterceptor;

    @Autowired
    public MjolnirConfig(MjolnirInterceptor mjolnirInterceptor) {
        this.mjolnirInterceptor = mjolnirInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mjolnirInterceptor);
    }
}
