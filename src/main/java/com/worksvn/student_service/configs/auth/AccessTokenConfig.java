package com.worksvn.student_service.configs.auth;

import com.worksvn.common.components.communication.ISHost;
import com.worksvn.common.modules.auth.core.CustomUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Configuration
public class AccessTokenConfig {
    @Value("${application.internal-service.authentication.oauth2.client-id}")
    private String clientID;
    @Value("${application.internal-service.authentication.oauth2.secret}")
    private String clientSecret;
    @Value("${application.oauth2.server.fixed.enable:false}")
    private boolean enableFixedOAuth2Server;
    @Value("${application.oauth2.server.fixed.host}")
    private String fixedOAuth2ServerHost;

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        return new JwtAccessTokenConverter() {
            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                // covert custom claims to pojo object
                OAuth2Authentication authentication = super.extractAuthentication(map);
                Authentication userAuthentication = authentication.getUserAuthentication();
                if (userAuthentication != null) {
                    CustomUserDetail userDetail = new CustomUserDetail(map);
                    userAuthentication = new UsernamePasswordAuthenticationToken(userDetail,
                            userAuthentication.getCredentials(), userDetail.getAuthorities());
                }
                return new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication);
            }
        };
    }

    @Bean
    ResourceServerTokenServices resourceServerTokenServices(JwtAccessTokenConverter jwtAccessTokenConverter,
                                                            RestTemplate restTemplate,
                                                            RestTemplate serviceDiscoveryRestTemplate) {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(clientID);
        remoteTokenServices.setClientSecret(clientSecret);
        RestTemplate selectedRestTemplate;
        if (enableFixedOAuth2Server) {
            ISHost.AUTH_SERVICE.update(fixedOAuth2ServerHost, false);
            selectedRestTemplate = restTemplate;
        } else {
            selectedRestTemplate = serviceDiscoveryRestTemplate;
        }
        selectedRestTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
        remoteTokenServices.setRestTemplate(selectedRestTemplate);
        remoteTokenServices.setCheckTokenEndpointUrl(ISHost.AUTH_SERVICE.getValue() + "oauth/check_token");
        remoteTokenServices.setAccessTokenConverter(jwtAccessTokenConverter);
        return remoteTokenServices;
    }

    @Bean
    public AuthenticationManager authenticationManager(ResourceServerTokenServices resourceServerTokenServices) {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(resourceServerTokenServices);
        return authenticationManager;
    }
}
