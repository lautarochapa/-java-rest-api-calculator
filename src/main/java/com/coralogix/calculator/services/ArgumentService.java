package com.coralogix.calculator.services;

import com.coralogix.calculator.dao.ArgumentDAO;
import com.coralogix.calculator.documents.Argument;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArgumentService {

    public Flux<Argument> findAll();
    public Mono<Argument> findByIndex(String index);
    public Mono<Argument> save(Argument arg);

    public Mono<Argument> updateByIndex(String index, Argument arg);
    public Mono<Void> deleteByIndex(String index);

}
