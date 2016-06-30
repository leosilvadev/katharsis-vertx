package io.katharsis.vertx.examples.repository;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.repository.annotations.JsonApiAddRelations;
import io.katharsis.repository.annotations.JsonApiFindManyTargets;
import io.katharsis.repository.annotations.JsonApiFindOneTarget;
import io.katharsis.repository.annotations.JsonApiRelationshipRepository;
import io.katharsis.repository.annotations.JsonApiRemoveRelations;
import io.katharsis.repository.annotations.JsonApiSetRelation;
import io.katharsis.repository.annotations.JsonApiSetRelations;
import io.katharsis.vertx.examples.domain.Project;
import io.katharsis.vertx.examples.domain.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@JsonApiRelationshipRepository(source = Task.class, target = Project.class)
@Slf4j
public class TaskToProjectRepository implements RelationshipRepository<Task, Long, Project, Long> {

    @JsonApiSetRelation
    @Override
    public void setRelation(Task source, Long targetId, String fieldName) {
        log.info("Set relation {} {} {}", source, targetId, fieldName);
    }

    @JsonApiSetRelations
    @Override
    public void setRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        log.info("Set relationS {} {} {}", source, targetIds, fieldName);
    }

    @JsonApiAddRelations
    @Override
    public void addRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        log.info("Add relations {} {} {}", source, targetIds, fieldName);
    }

    @JsonApiRemoveRelations
    @Override
    public void removeRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        log.info("Remove relations {} {} {}", source, targetIds, fieldName);
    }

    @JsonApiFindOneTarget
    @Override
    public Project findOneTarget(Long sourceId, String fieldName, QueryParams queryParams) {
        log.info("Find one target {} {} {}", sourceId, fieldName, queryParams);
        return Project.builder().id(sourceId).name("find one target " + fieldName).build();
    }

    @JsonApiFindManyTargets
    @Override
    public Iterable<Project> findManyTargets(Long sourceId, String fieldName, QueryParams queryParams) {
        log.info("Find many targets {} {} {}", sourceId, fieldName, queryParams);
        return Arrays.asList(Project.builder().id(sourceId).name("find many targets " + fieldName).build());
    }
}
