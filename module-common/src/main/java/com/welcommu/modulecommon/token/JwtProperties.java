package com.welcommu.modulecommon.token;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private long accessTokenTtl;
    private long refreshTokenTtl;

    public long getAccessTokenTtl() {
        return accessTokenTtl;
    }
    public void setAccessTokenTtl(long accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    public long getRefreshTokenTtl() {
        return refreshTokenTtl;
    }
    public void setRefreshTokenTtl(long refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
    }
}
