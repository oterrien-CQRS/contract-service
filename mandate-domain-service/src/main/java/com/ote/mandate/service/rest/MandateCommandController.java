package com.ote.mandate.service.rest;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.model.command.AddHeirCommand;
import com.ote.mandate.business.model.command.CreateMandateCommand;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/mandates")
@Validated
@Slf4j
public class MandateCommandController {

    private final IMandateCommandService mandateCommandService;

    private final MandateMapperService mandateMapperService;

    public MandateCommandController(@Autowired IMandateCommandService mandateCommandService,
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
                map(list -> new AddHeirCommand(id, mandateMapperService.convert(list))).
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
                                     @NotNull @Valid @RequestBody List<HeirPayload> heirs) throws Exception {

       /* RemoveHeirCommand command = new RemoveHeirCommand(id, mandateMapperService.convert(heirs.toArray(new HeirPayload[0])));
        return mandateCommandService.removeHeirs(command);*/
        return Mono.just(true);
    }

    @PutMapping(value = "/{id}/mainHeir", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> defineMainHeir(@PathVariable String id,
                                        @NotNull @Valid @RequestBody HeirPayload mainHeir) throws Exception {

        /*DefineMainHeirCommand command = new DefineMainHeirCommand(id, mandateMapperService.convert(mainHeir));
        return mandateCommandService.defineMainHeir(command);*/
        return Mono.just(true);
    }

    @PutMapping(value = "/{id}/notary", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> defineNotary(@PathVariable String id,
                                      @NotNull @Valid @RequestBody NotaryPayload notary) throws Exception {

       /* DefineNotaryCommand command = new DefineNotaryCommand(id, mandateMapperService.convert(notary));
        return mandateCommandService.defineNotary(command);*/
        return Mono.just(true);
    }
}
