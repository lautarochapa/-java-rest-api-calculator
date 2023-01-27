package com.coralogix.calculator.dao;

import com.coralogix.calculator.documents.Argument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ArgumentDAO extends ReactiveMongoRepository<Argument, String>  {

    Mono<Argument> findByIndex(String index);
}
