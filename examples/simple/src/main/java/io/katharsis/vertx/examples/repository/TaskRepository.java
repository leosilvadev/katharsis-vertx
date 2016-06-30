package io.katharsis.vertx.examples.repository;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.repository.annotations.JsonApiDelete;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindAllWithIds;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;
import io.katharsis.repository.annotations.JsonApiSave;
import io.katharsis.vertx.examples.domain.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@JsonApiResourceRepository(value = Task.class)
@Slf4j
public class TaskRepository implements ResourceRepository<Task, Long> {

    @JsonApiFindOne
    @Override
    public Task findOne(Long aLong, QueryParams queryParams) {
        log.info("Find one {} {}", aLong, queryParams);
        return Task.builder()
                .id(aLong).name("Some task " + aLong)
                .build();
    }

    @JsonApiFindAll
    @Override
    public Iterable<Task> findAll(QueryParams queryParams) {
        log.info("find all {}", queryParams);
        return findAll(null, queryParams);
    }

    @JsonApiFindAllWithIds
    @Override
    public Iterable<Task> findAll(Iterable<Long> longs, QueryParams queryParams) {
        log.info("find all {} {}", longs, queryParams);
        return Arrays.asList(Task.builder().id(1L).name("First task").build());
    }

    @JsonApiDelete
    @Override
    public void delete(Long aLong) {
        log.info("Delete Task {}", aLong);
    }

    @JsonApiSave
    @Override
    public <S extends Task> S save(S entity) {
        log.info("Save task {}", entity);
        return entity;
    }
}
