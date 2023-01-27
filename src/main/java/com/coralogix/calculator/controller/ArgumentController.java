package com.coralogix.calculator.controller;

import com.coralogix.calculator.documents.Argument;
import com.coralogix.calculator.model.WorldTimeServiceResponse;
import com.coralogix.calculator.services.ArgumentService;
import com.coralogix.calculator.services.WorldTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.RestTemplate;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/arguments")
public class ArgumentController {

    @Autowired
    private ArgumentService service;

    @Autowired
    private WorldTimeService worldTimeService;


    private static final Logger logger = LoggerFactory.getLogger("CustomLogger");

    @PostMapping("/register")
    public Mono<ResponseEntity<Argument>> registerArgument(Argument arg){

        try{
            logger.debug("Registrando el Argumento: " + arg.getIndex() + " con valor: " + arg.getNumber());

            Mono<ResponseEntity<Argument>> resp = service.save(arg)
                    .map(c -> ResponseEntity.created(URI.create("/api/arguments/".concat(c.getIndex())))
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(c));

            logger.debug("Argumento: " + arg.getIndex() + " con valor: " + arg.getNumber() + " Registrado con exito");
            return resp;

        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar registrar el Argumento: " + arg.getIndex() + " con valor: " + arg.getNumber() + " | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }

    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Argument>>> showArguments(){
        try{
            logger.debug("Buscando todos los argumentos");
            Flux<Argument> arguments = service.findAll();
            Mono<ResponseEntity<Flux<Argument>>> resp = Mono.just(
                    ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(arguments)
            );

            logger.debug("se encontraron: "+ arguments.count() +"  Argumentos de forma exitosa" );
            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Recuperar los Argumentos | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }

    @GetMapping("/{index}")
    public Mono<ResponseEntity<Argument>> getArgument(@PathVariable String index){

        try{
            logger.debug("Buscando el Argumento con index: " + index);
            Mono<Argument> arg = service.findByIndex(index);
            Mono<ResponseEntity<Argument>> resp = arg
                    .map(c -> ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(c))
                    .defaultIfEmpty(ResponseEntity.notFound().build());

            logger.debug("se encontro el Argumento con index: "+ index);
            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Recuperar el Argumento con index: " + index + " | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }

    @GetMapping("/withTime/{index}")
    public Mono<ResponseEntity<Map<String, Object>>> getArgumentWithTime(@PathVariable String index){

        try{

            logger.debug("Buscando la informacion del tiempo");
            WorldTimeServiceResponse time = worldTimeService.getData();
            logger.debug("Informacion del tiempo recuperada con exito");

            logger.debug("Buscando el Argumento con index: " + index);
            Map<String, Object> response = new HashMap<>();

            Mono<Argument> found = service.findByIndex(index);

            Mono<ResponseEntity<Map<String, Object>>> resp =  found.map(c -> {
                response.put("number", c.getNumber());
                response.put("index", c.getIndex());
                response.put("datetime", time.getDatetime());
                response.put("timezone", time.getTimezone());
                return ResponseEntity
                        .created(URI.create("/api/arguments/".concat(c.getIndex())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(response);
            }).onErrorResume(t -> {
                return Mono.just(t).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                        .flatMapMany(Flux::fromIterable)
                        .map(fieldError -> "El campo: " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> {
                            response.put("errors", list);
                            response.put("timestamp", new Date());
                            response.put("status", HttpStatus.BAD_REQUEST.value());

                            return Mono.just(ResponseEntity.badRequest().body(response));
                        });
            });

            logger.debug("Se Recupero correctamente el Argumento con index: "+ index);

            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Recuperar el Argumento con index: " + index + " | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }

    @GetMapping("/withTime")
    public Mono<ResponseEntity<Flux<Map<String, Object>>>> getArgumentsWithTime() {
        try {
            logger.debug("Buscando la informacion del tiempo");
            WorldTimeServiceResponse time = worldTimeService.getData();
            logger.debug("Informacion del tiempo recuperada con exito");

            logger.debug("Buscando todos los Argumentos");
            Flux<Argument> found = service.findAll();

            Mono<ResponseEntity<Flux<Map<String, Object>>>> resp = found.flatMap(c -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("number", c.getNumber());
                        response.put("index", c.getIndex());
                        response.put("datetime", time.getDatetime());
                        response.put("timezone", time.getTimezone());
                        return Mono.just(response);
                    }).collectList()
                    .flatMap(list -> Mono.just(ResponseEntity.ok().body(Flux.fromIterable(list))));

            logger.debug("Se recuperaron correctamente todos los Argumentos");

            return resp;
        } catch (Exception ex) {
            logger.error("Ocurrio un error al intentar recuperar todos los Argumentos | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }


    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> saveArgument(@RequestBody Mono<Argument> monoArg){

        try{

            Map<String, Object> response = new HashMap<>();
            logger.debug("Creando un nuevo Argumento");
            Mono<ResponseEntity<Map<String, Object>>> resp =  monoArg.flatMap(arg -> {
                return service.save(arg).map(c -> {
                    response.put("argument", c);
                    response.put("message", "argumento guardado con exito");
                    response.put("timestamp", new Date());
                    return ResponseEntity
                            .created(URI.create("/api/arguments/".concat(c.getIndex())))
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(response);
                });
            }).onErrorResume(t -> {
                return Mono.just(t).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                        .flatMapMany(Flux::fromIterable)
                        .map(fieldError -> "El campo: " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> {
                            response.put("errors", list);
                            response.put("timestamp", new Date());
                            response.put("status", HttpStatus.BAD_REQUEST.value());

                            return Mono.just(ResponseEntity.badRequest().body(response));
                        });
            });

            logger.debug("Se Creo correctamente el nuevo Argumento");

            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Crear el nuevo Argumento | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }

    }

    @PutMapping("/{index}")
    public Mono<ResponseEntity<Argument>> editArgument(@RequestBody Argument arg, @PathVariable String index){
        try{
            logger.debug("Editando el Argumento con index: " + index + " nuevo Valor: " + arg.getNumber());
            Mono<ResponseEntity<Argument>> resp =  service.findByIndex(index).flatMap(c -> {
                        c.setNumber(arg.getNumber());
                        return service.updateByIndex(index, c);
                    })
                    .map(c -> ResponseEntity
                            .created(URI.create("/api/arguments/".concat(c.getIndex())))
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(c))
                    .defaultIfEmpty(ResponseEntity.notFound().build());

            logger.debug("Se Edito correctamente el Argumento con index: "+ index);

            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Editar el Argumento con index: " + index + " | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }

    @DeleteMapping("/{index}")
    public Mono<ResponseEntity<Void>> deleteArgument(@PathVariable String index){
        try{
            logger.debug("Eliminando el Argumento con index: " + index);
            Mono<ResponseEntity<Void>> resp =  service.deleteByIndex(index).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                    .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));

            logger.debug("Se Elimino correctamente el Argumento con index: "+ index);

            return resp;
        }catch (Exception ex){
            logger.error("Ocurrio un error al intentar Eliminar el Argumento con index: " + index + " | Detalle del error: " + ex.getMessage());
            return Mono.empty();
        }
    }


}
