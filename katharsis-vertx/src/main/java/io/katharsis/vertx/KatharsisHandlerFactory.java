package io.katharsis.vertx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.katharsis.dispatcher.JsonApiDispatcher;
import io.katharsis.dispatcher.JsonApiDispatcherImpl;
import io.katharsis.dispatcher.handlers.JsonApiDelete;
import io.katharsis.dispatcher.handlers.JsonApiGet;
import io.katharsis.dispatcher.handlers.JsonApiPatch;
import io.katharsis.dispatcher.handlers.JsonApiPost;
import io.katharsis.dispatcher.registry.RepositoryRegistryImpl;
import io.katharsis.dispatcher.registry.api.RepositoryRegistry;
import io.katharsis.errorhandling.mapper.ExceptionMapperRegistry;
import io.katharsis.errorhandling.mapper.ExceptionMapperRegistryBuilder;
import io.katharsis.jackson.JsonApiModuleBuilder;
import io.vertx.core.json.Json;
import lombok.NonNull;

public class KatharsisHandlerFactory {

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String apiMountPath) {

        return create(packagesToScan, apiMountPath, Json.mapper);
    }

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String webPath,
                                          @NonNull ObjectMapper objectMapper) {

        return create(packagesToScan, webPath, objectMapper, new DefaultParameterProviderFactory());
    }

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String apiMountPath,
                                          @NonNull ObjectMapper objectMapper,
                                          @NonNull ParameterProviderFactory parameterProviderFactory) {

//        QueryParamsBuilder queryParamsBuilder = new QueryParamsBuilder(new DefaultQueryParamsParser());
//        ExceptionMapperRegistry exceptionMapperRegistry = buildExceptionMapperRegistry(packagesToScan);

        objectMapper.registerModule(JsonApiModuleBuilder.create());

        RepositoryRegistry registry = RepositoryRegistryImpl.build(packagesToScan, apiMountPath);

        JsonApiDispatcher requestDispatcher = createRequestDispatcher(registry);
        return new KatharsisHandler(objectMapper, requestDispatcher, parameterProviderFactory, apiMountPath);
    }

    private static JsonApiDispatcher createRequestDispatcher(@NonNull RepositoryRegistry registry) {
        return new JsonApiDispatcherImpl(new JsonApiGet(registry),
                new JsonApiPost(registry), new JsonApiPatch(registry), new JsonApiDelete(registry));
    }

    private static ExceptionMapperRegistry buildExceptionMapperRegistry(String resourceSearchPackage) {
        ExceptionMapperRegistryBuilder mapperRegistryBuilder = new ExceptionMapperRegistryBuilder();
        try {
            return mapperRegistryBuilder.build(resourceSearchPackage);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
