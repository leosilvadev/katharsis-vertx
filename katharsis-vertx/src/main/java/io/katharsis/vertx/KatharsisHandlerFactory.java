package io.katharsis.vertx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.katharsis.dispatcher.RequestDispatcher;
import io.katharsis.dispatcher.registry.ControllerRegistry;
import io.katharsis.dispatcher.registry.ControllerRegistryBuilder;
import io.katharsis.errorhandling.mapper.ExceptionMapperRegistry;
import io.katharsis.errorhandling.mapper.ExceptionMapperRegistryBuilder;
import io.katharsis.jackson.JsonApiModuleBuilder;
import io.katharsis.locator.SampleJsonServiceLocator;
import io.katharsis.queryParams.DefaultQueryParamsParser;
import io.katharsis.queryParams.QueryParamsBuilder;
import io.katharsis.request.path.PathBuilder;
import io.katharsis.resource.field.ResourceFieldNameTransformer;
import io.katharsis.resource.information.ResourceInformationBuilder;
import io.katharsis.resource.registry.ResourceRegistry;
import io.katharsis.resource.registry.ResourceRegistryBuilder;
import io.katharsis.utils.parser.TypeParser;
import io.vertx.core.json.Json;
import lombok.NonNull;

public class KatharsisHandlerFactory {

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String webPath) {

        return create(packagesToScan, webPath, Json.mapper);
    }

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String webPath,
                                          @NonNull ObjectMapper objectMapper) {

        return create(packagesToScan, webPath, objectMapper, new DefaultParameterProviderFactory());
    }

    public static KatharsisHandler create(@NonNull String packagesToScan,
                                          @NonNull String webPath,
                                          @NonNull ObjectMapper objectMapper,
                                          @NonNull ParameterProviderFactory parameterProviderFactory) {

        QueryParamsBuilder queryParamsBuilder = new QueryParamsBuilder(new DefaultQueryParamsParser());
        ExceptionMapperRegistry exceptionMapperRegistry = buildExceptionMapperRegistry(packagesToScan);
        ResourceRegistry resourceRegistry = buildRegistry(packagesToScan, webPath);

        JsonApiModuleBuilder jsonApiModuleBuilder = new JsonApiModuleBuilder();
        objectMapper.registerModule(jsonApiModuleBuilder.build(resourceRegistry));

        RequestDispatcher requestDispatcher = createRequestDispatcher(objectMapper, exceptionMapperRegistry, resourceRegistry);

        PathBuilder pathBuilder = new PathBuilder(resourceRegistry);

        return new KatharsisHandler(objectMapper, queryParamsBuilder, webPath,
                pathBuilder, parameterProviderFactory, requestDispatcher);
    }

    private static RequestDispatcher createRequestDispatcher(@NonNull ObjectMapper objectMapper,
                                                             @NonNull ExceptionMapperRegistry exceptionMapperRegistry,
                                                             @NonNull ResourceRegistry resourceRegistry) {
        TypeParser typeParser = new TypeParser();
        ControllerRegistryBuilder controllerRegistryBuilder =
                new ControllerRegistryBuilder(resourceRegistry, typeParser, objectMapper);

        ControllerRegistry controllerRegistry = controllerRegistryBuilder.build();
        return new RequestDispatcher(controllerRegistry, exceptionMapperRegistry);
    }

    public static ResourceRegistry buildRegistry(@NonNull String packageToScan, @NonNull String webPath) {
        ResourceRegistryBuilder registryBuilder = new ResourceRegistryBuilder(new SampleJsonServiceLocator(),
                new ResourceInformationBuilder(new ResourceFieldNameTransformer()));

        return registryBuilder.build(packageToScan, webPath);
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
