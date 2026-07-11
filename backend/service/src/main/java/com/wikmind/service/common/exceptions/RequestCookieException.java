package com.wikmind.service.common.exceptions;

import org.springframework.web.bind.ServletRequestBindingException;

public class RequestCookieException extends ServletRequestBindingException {
    private final String cookieName;

    public RequestCookieException(String cookieName) {
        super(String.format("The required cookie '%s' is missing from the request header.", cookieName));
        this.cookieName = cookieName;
    }

    public String getCookieName() {
        return cookieName;
    }

}
