package kr.apartribebackend.global.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientRedirectUrlHolder {

    private static final ConcurrentHashMap<String, String> redirectUrlHolder = new ConcurrentHashMap<>();

    public void setRedirectUrl(String redirectUrl) {
        redirectUrlHolder.put("redirect_url", redirectUrl);
    }

    public String getRedirectUrl() {
        return redirectUrlHolder.get("redirect_url");
    }

}
