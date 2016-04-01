package io.katharsis.vertx.examples.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleAttribute {

    private String title;
    private String value;
}
