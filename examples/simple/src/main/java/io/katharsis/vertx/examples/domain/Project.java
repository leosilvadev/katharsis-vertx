package io.katharsis.vertx.examples.domain;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = "projects")
public class Project {

    @JsonApiId
    private Long id;
    private String name;
    private List<SimpleAttribute> simpleAttributes = new ArrayList<>();

}
