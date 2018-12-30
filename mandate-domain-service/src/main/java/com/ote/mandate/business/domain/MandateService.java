package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.IMandateService;
import com.ote.mandate.business.exception.*;
import com.ote.mandate.business.model.Heir;
import com.ote.mandate.business.model.Mandate;
import com.ote.mandate.business.model.command.AddHeirCommand;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import com.ote.mandate.business.model.command.DefineMainHeirCommand;
import com.ote.mandate.business.model.command.DefineNotaryCommand;
import com.ote.mandate.business.model.event.MandateCreatedEvent;
import com.ote.mandate.business.model.event.MandateHeirAddedEvent;
import com.ote.mandate.business.model.event.MandateMainHeirDefinedEvent;
import com.ote.mandate.business.model.event.MandateNotaryDefinedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import com.ote.mandate.business.spi.IMandateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class MandateService implements IMandateService {

    private final IEventRepository eventRepository;
    private final IMandateRepository mandateRepository;

    MandateService(IEventRepository eventRepository, IMandateRepository mandateRepository) {
        this.eventRepository = eventRepository;
        this.mandateRepository = mandateRepository;

        // Event Subscription
        eventRepository.subscribe(MandateCreatedEvent.class, this::handle);
        eventRepository.subscribe(MandateHeirAddedEvent.class, this::handle);
        eventRepository.subscribe(MandateMainHeirDefinedEvent.class, this::handle);
        eventRepository.subscribe(MandateNotaryDefinedEvent.class, this::handle);
    }

    @Override
    public void apply(CreateMandateCommand command) throws MandateAlreadyCreatedException {

        String id = command.getId();

        if (mandateRepository.find(id).isPresent()) {
            throw new MandateAlreadyCreatedException(id);
        }

        MandateCreatedEvent event = new MandateCreatedEvent(command.getId(), command.getBankName(), command.getContractor());
        if (command.getNotary() != null) {
            event.setNotary(command.getNotary());
        }
        if (command.getMainHeir() != null) {
            event.setMainHeir(command.getMainHeir());
        }
        if (!command.getOtherHeirs().isEmpty()) {
            event.getOtherHeirs().addAll(command.getOtherHeirs());
        }
        eventRepository.storeAndPublish(event);
    }

    @Override
    public void apply(AddHeirCommand command) throws MandateNotYetCreatedException, MandateAllHeirAlreadyPresentException {

        String id = command.getId();
        Mandate mandate = getMandateIfExists(id);

        List<Heir> heirsNotYetPresent = command.getOtherHeirs().
                stream().
                filter(p -> !mandate.getOtherHeirs().contains(p)).
                collect(Collectors.toList());

        if (heirsNotYetPresent.isEmpty()) {
            throw new MandateAllHeirAlreadyPresentException(id, command.getOtherHeirs());
        }

        heirsNotYetPresent.forEach(p -> {
            MandateHeirAddedEvent event = new MandateHeirAddedEvent(id, p);
            eventRepository.storeAndPublish(event);
        });
    }

    @Override
    public void apply(DefineMainHeirCommand command) throws MandateNotYetCreatedException, MandateMainHeirIsAlreadyDefinedException {

        String id = command.getId();
        Mandate mandate = getMandateIfExists(id);

        if (mandate.getMainHeir().equals(command.getMainHeir())) {
            throw new MandateMainHeirIsAlreadyDefinedException(id, command.getMainHeir());
        }

        MandateMainHeirDefinedEvent event = new MandateMainHeirDefinedEvent(id, command.getMainHeir());
        eventRepository.storeAndPublish(event);
    }

    @Override
    public void apply(DefineNotaryCommand command) throws MandateNotYetCreatedException, MandateNotaryIsAlreadyDefinedException {

        String id = command.getId();
        Mandate mandate = getMandateIfExists(id);

        if (mandate.getNotary().equals(command.getNotary())) {
            throw new MandateNotaryIsAlreadyDefinedException(id, command.getNotary());
        }

        MandateNotaryDefinedEvent event = new MandateNotaryDefinedEvent(id, command.getNotary());
        eventRepository.storeAndPublish(event);
    }

    private Mandate getMandateIfExists(String id) throws MandateNotYetCreatedException {

        Optional<Mandate> mandateOptional = mandateRepository.find(id);

        if (!mandateOptional.isPresent()) {
            throw new MandateNotYetCreatedException(id);
        }

        return mandateOptional.get();
    }

    // region event handling
    private void handle(MandateCreatedEvent event) throws MandateAlreadyCreatedException {

        String id = event.getId();
        if (mandateRepository.find(id).isPresent()) {
            throw new MandateAlreadyCreatedException(id);
        }

        Mandate mandate = new Mandate(event.getId());
        mandate.setBankName(event.getBankName());
        mandate.setContractor(event.getContractor());
        mandate.setNotary(event.getNotary());
        mandate.setMainHeir(event.getMainHeir());
        mandate.getOtherHeirs().addAll(event.getOtherHeirs());
        mandateRepository.save(mandate);
    }

    private void handle(MandateHeirAddedEvent event) throws MandateNotYetCreatedException {
        String id = event.getId();
        Mandate mandate = getMandateIfExists(id);
        mandate.getOtherHeirs().add(event.getHeir());
        mandateRepository.save(mandate);
    }

    private void handle(MandateMainHeirDefinedEvent event) throws MandateNotYetCreatedException {
        String id = event.getId();
        Mandate mandate = getMandateIfExists(id);
        mandate.setMainHeir(event.getHeir());
        mandateRepository.save(mandate);
    }

    private void handle(MandateNotaryDefinedEvent event) throws MandateNotYetCreatedException {
        String id = event.getId();
        Mandate mandate = getMandateIfExists(id);
        mandate.setNotary(event.getNotary());
        mandateRepository.save(mandate);
    }
    // endregion
}
