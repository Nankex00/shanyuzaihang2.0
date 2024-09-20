package com.fushuhealth.recovery.common.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessage {

    @Autowired
    private MessageSource messageSource;

    public LocaleMessage(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

}
