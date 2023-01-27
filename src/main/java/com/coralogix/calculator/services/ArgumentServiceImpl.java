package com.coralogix.calculator.services;

import com.coralogix.calculator.dao.ArgumentDAO;
import com.coralogix.calculator.documents.Argument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ArgumentServiceImpl implements ArgumentService {

    @Autowired
    private ArgumentDAO argumentDAO;

    @Override
    public Flux<Argument> findAll() {
        return argumentDAO.findAll();
    }

    @Override
    public Mono<Argument> findByIndex(String index) {
        return argumentDAO.findByIndex(index);
    }

    @Override
    public Mono<Argument> save(Argument arg) {
        return argumentDAO.save(arg);
    }

    @Override
    public Mono<Argument> updateByIndex(String index, Argument arg) {
        return argumentDAO.findByIndex(index)
                .flatMap(existingArgument -> {
                    existingArgument.setNumber(arg.getNumber());
                    return argumentDAO.save(existingArgument);
                });
    }

    @Override
    public Mono<Void> deleteByIndex(String index) {
        return argumentDAO.findByIndex(index)
                .flatMap(argumentDAO::delete);
    }

}
