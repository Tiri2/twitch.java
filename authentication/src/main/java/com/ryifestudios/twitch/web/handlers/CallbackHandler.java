package com.ryifestudios.twitch.web.handlers;

import com.ryifestudios.twitch.models.AccessToken;
import com.ryifestudios.twitch.web.responses.AuthorizationResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class CallbackHandler implements Handler {

    private final AuthorizationResponse response;

    public CallbackHandler(AuthorizationResponse r) {
        this.response = r;
    }

    @Override
    public void handle(@NotNull Context context) {

        response.setAuthorizedCode(context.queryParam("code"));

        context.result("success");

        response.setPending(false);
    }
}
