package com.hh.smscat.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Owen Pan on 2017-06-30.
 */
@Configuration
public class BeanConfig {
    @Bean
    public Integer getHelloInteger(){
        return 110;
    }


}
