package com.commit.camp_reserve.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class ApiConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Collections.singletonList(new StringHttpMessageConverter(StandardCharsets.UTF_8)));
        return restTemplate;
    }
}
