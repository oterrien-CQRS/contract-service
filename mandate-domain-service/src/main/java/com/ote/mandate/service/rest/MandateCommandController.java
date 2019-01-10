package com.ote.mandate.service.rest;

import com.ote.mandate.business.command.api.IMandateCommandHandler;
import com.ote.mandate.business.command.model.*;
import com.ote.mandate.service.rest.payload.HeirPayload;
import com.ote.mandate.service.rest.payload.MandatePayload;
import com.ote.mandate.service.rest.payload.NotaryPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/mandates")
@Validated
@Slf4j
public class MandateCommandController {

    private final IMandateCommandHandler mandateCommandService;

    private final MandateMapperService mandateMapperService;

    public MandateCommandController(@Autowired IMandateCommandHandler mandateCommandService,
                                    @Autowired MandateMapperService mandateMapperService) {
        this.mandateCommandService = mandateCommandService;
        this.mandateMapperService = mandateMapperService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> create(@NotNull @Valid @RequestBody Mono<MandatePayload> mandate) throws Exception {

        return mandate.
                map(m -> new CreateMandateCommand(m.getId(), m.getBankName(),
                        mandateMapperService.convert(m.getContractor()),
                        mandateMapperService.convert(m.getNotary()),
                        mandateMapperService.convert(m.getMainHeir()),
                        mandateMapperService.convert(m.getOtherHeirs()))).
                flatMap(cmd -> {
                    try {
                        return mandateCommandService.createMandate(Mono.just(cmd));
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    @PutMapping(value = "/{id}/heirs", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> addHeirs(@PathVariable String id,
                                  @NotNull @Valid @RequestBody Flux<HeirPayload> heirs) throws Exception {

        return heirs.
                collect(Collectors.toList()).
                map(list -> new AddHeirsCommand(id, mandateMapperService.convert(list))).
                flatMap(cmd -> {
                    try {
                        return mandateCommandService.addHeirs(Mono.just(cmd));
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    @DeleteMapping(value = "/{id}/heirs", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> removeHeirs(@PathVariable String id,
                                     @NotNull @Valid @RequestBody Flux<HeirPayload> heirs) throws Exception {

        return heirs.
                collect(Collectors.toList()).
                map(list -> new RemoveHeirsCommand(id, mandateMapperService.convert(list))).
                flatMap(cmd -> {
                    try {
                        return mandateCommandService.removeHeirs(Mono.just(cmd));
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    @PutMapping(value = "/{id}/mainHeir", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> defineMainHeir(@PathVariable String id,
                                        @NotNull @Valid @RequestBody Mono<HeirPayload> mainHeir) throws Exception {

        return mainHeir.
                map(heir -> new DefineMainHeirCommand(id, mandateMapperService.convert(heir)))
                .transform(cmd -> {
                    try {
                        return mandateCommandService.defineMainHeir(cmd);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    @PutMapping(value = "/{id}/notary", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> defineNotary(@PathVariable String id,
                                      @NotNull @Valid @RequestBody Mono<NotaryPayload> notary) throws Exception {

        return notary.
                map(n -> new DefineNotaryCommand(id, mandateMapperService.convert(n)))
                .transform(cmd -> {
                    try {
                        return mandateCommandService.defineNotary(cmd);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }
}
