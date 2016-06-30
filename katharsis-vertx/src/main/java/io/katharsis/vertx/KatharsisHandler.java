package io.katharsis.vertx;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.katharsis.dispatcher.JsonApiDispatcher;
import io.katharsis.dispatcher.ResponseContext;
import io.katharsis.repository.RepositoryParameterProvider;
import io.katharsis.request.Request;
import io.katharsis.request.path.JsonApiPath;
import io.netty.buffer.ByteBufInputStream;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.EncodeException;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * Vertx handler to Katharsis resource controller. Vertx delegates request processing to Katharsis controller.
 */
@Slf4j
@Value
@RequiredArgsConstructor
public class KatharsisHandler implements Handler<RoutingContext> {

    private final ObjectMapper mapper;
    private final JsonApiDispatcher requestDispatcher;
    private final ParameterProviderFactory parameterProviderFactory;
    private final String apiMountPoint;

    @Override
    public void handle(RoutingContext ctx) {
        InputStream body = new ByteBufInputStream(ctx.getBody().getByteBuf());
        try {
            String httpMethod = ctx.request().method().name();

            RepositoryParameterProvider parameterProvider = parameterProviderFactory.provider(ctx);

            JsonApiPath path = JsonApiPath.parsePathFromStringUrl(ctx.request().absoluteURI(), apiMountPoint);
            Request request = new Request(path, httpMethod, body, parameterProvider);

            ResponseContext response = requestDispatcher.handle(request);

            ctx.response()
                    .setStatusCode(response.getHttpStatus())
                    .putHeader(HttpHeaders.CONTENT_TYPE, JsonApiMediaTypeHandler.APPLICATION_JSON_API)
                    .end(encode(response));

        } catch (Exception e) {
            throw new KatharsisVertxException(String.format("Exception during dispatch: %s. \nURL: %s ",
                    e.getMessage(), ctx.request().absoluteURI()), e);
        }
    }

    /**
     * Taken from io.vertx.json.Json.
     */
    protected String encode(Object obj) throws EncodeException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new EncodeException("Failed to encode as JSON: " + e.getMessage());
        }
    }

}
