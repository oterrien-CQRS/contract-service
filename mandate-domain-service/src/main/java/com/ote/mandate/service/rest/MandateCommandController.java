package com.ote.mandate.service.rest;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.service.rest.payload.HeirPayload;
import com.ote.mandate.service.rest.payload.MandatePayload;
import com.ote.mandate.service.rest.payload.NotaryPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mandates")
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
    public void create(@NotNull @Valid @RequestBody MandatePayload mandate) throws Exception {

        CreateMandateCommand command = new CreateMandateCommand(mandate.getId(), mandate.getBankName(),
                mandateMapperService.convert(mandate.getContractor()),
                mandateMapperService.convert(mandate.getNotary()),
                mandateMapperService.convert(mandate.getMainHeir()),
                mandateMapperService.convert(mandate.getOtherHeirs().toArray(new HeirPayload[0])));

        mandateCommandService.apply(command);
    }

    @PutMapping(value = "/{id}/heirs", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addHeirs(@PathVariable String id,
                         @NotNull @Valid @RequestBody List<HeirPayload> heirs) throws Exception {

        AddHeirCommand command = new AddHeirCommand(id, mandateMapperService.convert(heirs.toArray(new HeirPayload[0])));
        mandateCommandService.apply(command);
    }

    @DeleteMapping(value = "/{id}/heirs", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void removeHeirs(@PathVariable String id,
                            @NotNull @Valid @RequestBody List<HeirPayload> heirs) throws Exception {

        RemoveHeirCommand command = new RemoveHeirCommand(id, mandateMapperService.convert(heirs.toArray(new HeirPayload[0])));
        mandateCommandService.apply(command);
    }

    @PutMapping(value = "/{id}/mainHeir", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void defineMainHeir(@PathVariable String id,
                               @NotNull @Valid @RequestBody HeirPayload mainHeir) throws Exception {

        DefineMainHeirCommand command = new DefineMainHeirCommand(id, mandateMapperService.convert(mainHeir));
        mandateCommandService.apply(command);
    }

    @PutMapping(value = "/{id}/notary", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void defineNotary(@PathVariable String id,
                             @NotNull @Valid @RequestBody NotaryPayload notary) throws Exception {

        DefineNotaryCommand command = new DefineNotaryCommand(id, mandateMapperService.convert(notary));
        mandateCommandService.apply(command);
    }
}