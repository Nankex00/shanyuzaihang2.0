package com.fushuhealth.recovery.h5.config;

import com.fushuhealth.recovery.common.util.DateUtil;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {

    @Value("${spring.profiles.active:NA}")
    private String activeProfile;

    @Value("${spring.application.name:NA}")
    private String appName;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<Long, LocalDate> longToDate = new AbstractConverter<Long, LocalDate>() {
            @Override
            protected LocalDate convert(Long source) {
                String ymd = DateUtil.getYMD(source);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(ymd, format);
                return localDate;
            }
        };
        modelMapper.addConverter(longToDate);
        return modelMapper;
    }
}
