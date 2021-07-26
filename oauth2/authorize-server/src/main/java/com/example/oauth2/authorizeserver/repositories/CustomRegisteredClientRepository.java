package com.example.oauth2.authorizeserver.repositories;

import com.example.oauth2.authorizeserver.models.entities.OauthClientEntity;
import com.example.oauth2.authorizeserver.services.OauthClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Arrays;

@Slf4j
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final OauthClientService oauthClientService;

    @Autowired
    public CustomRegisteredClientRepository(OauthClientService oauthClientService) {
        this.oauthClientService = oauthClientService;
    }

    @Override
    public RegisteredClient findById(String id) {
        return buildRegisteredClient(oauthClientService.getOauthClientById(id));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return buildRegisteredClient(oauthClientService.getOauthClientByClientId(clientId));
    }

    private RegisteredClient buildRegisteredClient(OauthClientEntity oauthClientEntity) {
        RegisteredClient.Builder registeredClientBuilder = RegisteredClient.withId(oauthClientEntity.getId())
                .clientId(oauthClientEntity.getClientId())
                .clientSecret(oauthClientEntity.getClientSecret())
                .clientAuthenticationMethod(new ClientAuthenticationMethod(oauthClientEntity.getAuthenticationMethods()));

        Arrays.stream(oauthClientEntity.getAuthorizedGrantTypes().split(",")).forEach(
                grantType -> registeredClientBuilder.authorizationGrantType(new AuthorizationGrantType(grantType.trim()))
        );
        Arrays.stream(oauthClientEntity.getRedirectedUris().split(",")).forEach(
                redirectedUri -> registeredClientBuilder.redirectUri(redirectedUri.trim())
        );
        Arrays.stream(oauthClientEntity.getScopes().split(",")).forEach(
                scope -> registeredClientBuilder.scope(scope.trim())
        );
        return registeredClientBuilder.build();
    }
}
