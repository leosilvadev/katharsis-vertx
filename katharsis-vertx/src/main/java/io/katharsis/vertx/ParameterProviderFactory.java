package io.katharsis.vertx;

import io.katharsis.repository.RepositoryParameterProvider;
import io.vertx.ext.web.RoutingContext;

/**
 * Factory to build parameter providers for each routing context.
 */
public interface ParameterProviderFactory {

    RepositoryParameterProvider provider(RoutingContext ctx);

}
