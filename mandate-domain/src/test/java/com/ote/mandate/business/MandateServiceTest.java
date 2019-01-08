package com.ote.mandate.business;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.api.MandateServiceProvider;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.*;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.business.model.event.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class MandateServiceTest {

    @Spy
    private EventRepositoryMock eventRepository = new EventRepositoryMock();

    private IMandateCommandService mandateService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mandateService = MandateServiceProvider.getInstance().getFactory().createService(eventRepository);
    }

    @After
    public void tearDown() {
        eventRepository.clean();
    }

    // region <<Nominal cases>>
    @Test
    public void createCommandShouldRaiseEvent() throws Exception {

        String id = "411455";

        CreateMandateCommand command = new CreateMandateCommand(id, "Socgen", new Contractor("Olivier"), new Notary("Maitre Gallibert"), new Heir("Maryline"), new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.createMandate(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(1);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNotNull();
                        assertions.assertThat(mandate.getNotary().getName()).isEqualTo("Maitre Gallibert");
                        assertions.assertThat(mandate.getMainHeir()).isNotNull();
                        assertions.assertThat(mandate.getMainHeir().getName()).isEqualTo("Maryline");
                        assertions.assertThat(mandate.getOtherHeirs()).isNotEmpty();
                        assertions.assertThat(mandate.getOtherHeirs().stream().map(p -> p.getName()).collect(Collectors.toList())).contains("Baptiste", "Emma");
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void defineMainHeirCommandShouldRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));

        eventRepository.initWith(event);

        DefineMainHeirCommand command = new DefineMainHeirCommand(id, new Heir("Maryline"));

        StepVerifier.create(mandateService.defineMainHeir(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(2);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateMainHeirDefinedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNotNull();
                        assertions.assertThat(mandate.getMainHeir().getName()).isEqualTo("Maryline");
                        assertions.assertThat(mandate.getOtherHeirs()).isNullOrEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void defineNotaryCommandShouldRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));

        eventRepository.initWith(event);

        DefineNotaryCommand command = new DefineNotaryCommand(id, new Notary("Maitre Gallibert"));

        StepVerifier.create(mandateService.defineNotary(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(2);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateNotaryDefinedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getNotary()).isNotNull();
                        assertions.assertThat(mandate.getNotary().getName()).isEqualTo("Maitre Gallibert");
                        assertions.assertThat(mandate.getOtherHeirs()).isNullOrEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void addHeirsCommandShouldRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));

        eventRepository.initWith(event);

        AddHeirsCommand command = new AddHeirsCommand(id, new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.addHeirs(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(3);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateHeirAddedEvent.class);
                    assertions.assertThat(events.get(2)).isInstanceOf(MandateHeirAddedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isNotEmpty();
                        assertions.assertThat(mandate.getOtherHeirs().stream().map(p -> p.getName()).collect(Collectors.toList())).contains("Baptiste", "Emma");
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void removeHeirsCommandShouldRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.getOtherHeirs().addAll(Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));

        eventRepository.initWith(event);

        RemoveHeirsCommand command = new RemoveHeirsCommand(id, new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.removeHeirs(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(3);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateHeirRemovedEvent.class);
                    assertions.assertThat(events.get(2)).isInstanceOf(MandateHeirRemovedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isNullOrEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();

    }
    // endregion

    // region <<Event is not or partially raised>>
    @Test
    public void defineExistingMainHeirCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.setMainHeir(new Heir("Maryline"));

        eventRepository.initWith(event);

        DefineMainHeirCommand command = new DefineMainHeirCommand(id, new Heir("Maryline"));

        StepVerifier.create(mandateService.defineMainHeir(Mono.just(command))).
                expectNext(false).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(1);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNotNull();
                        assertions.assertThat(mandate.getMainHeir().getName()).isEqualTo("Maryline");
                        assertions.assertThat(mandate.getOtherHeirs()).isNullOrEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void defineExistingNotaryCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.setNotary(new Notary("Maitre Gallibert"));

        eventRepository.initWith(event);

        DefineNotaryCommand command = new DefineNotaryCommand(id, new Notary("Maitre Gallibert"));

        StepVerifier.create(mandateService.defineNotary(Mono.just(command))).
                expectNext(false).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(1);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNotNull();
                        assertions.assertThat(mandate.getNotary().getName()).isEqualTo("Maitre Gallibert");
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isNullOrEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void addExistingHeirCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.getOtherHeirs().add(new Heir("Baptiste"));

        eventRepository.initWith(event);

        AddHeirsCommand command = new AddHeirsCommand(id, new Heir("Baptiste"));

        StepVerifier.create(mandateService.addHeirs(Mono.just(command))).
                expectNext(false).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(1);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isNotEmpty();
                        assertions.assertThat(mandate.getOtherHeirs().stream().map(p -> p.getName()).collect(Collectors.toSet())).contains("Baptiste");
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void addPartialExistingHeirCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.getOtherHeirs().add(new Heir("Baptiste"));

        eventRepository.initWith(event);

        AddHeirsCommand command = new AddHeirsCommand(id, new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.addHeirs(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(2);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateHeirAddedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isNotEmpty();
                        assertions.assertThat(mandate.getOtherHeirs().stream().map(p -> p.getName()).collect(Collectors.toSet())).contains("Baptiste", "Emma");
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }


    @Test
    public void removeNonExistingHeirCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));

        eventRepository.initWith(event);

        RemoveHeirsCommand command = new RemoveHeirsCommand(id, new Heir("Baptiste"));

        StepVerifier.create(mandateService.removeHeirs(Mono.just(command))).
                expectNext(false).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(1);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }

    @Test
    public void removePartialExistingHeirCommandShouldNotRaiseEvent() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.getOtherHeirs().add(new Heir("Baptiste"));

        eventRepository.initWith(event);

        RemoveHeirsCommand command = new RemoveHeirsCommand(id, new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.removeHeirs(Mono.just(command))).
                expectNext(true).
                expectComplete().
                verify();

        SoftAssertions assertions = new SoftAssertions();

        StepVerifier.create(eventRepository.findAll(Mono.just(id)).
                collectList()).
                assertNext(events -> {
                    assertions.assertThat(events.size()).isEqualTo(2);
                    assertions.assertThat(events.get(0)).isInstanceOf(MandateCreatedEvent.class);
                    assertions.assertThat(events.get(1)).isInstanceOf(MandateHeirRemovedEvent.class);
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        assertions.assertThat(mandate).isNotNull();
                        assertions.assertThat(mandate.getId()).isEqualTo(id);
                        assertions.assertThat(mandate.getBankName()).isEqualTo("Socgen");
                        assertions.assertThat(mandate.getContractor()).isNotNull();
                        assertions.assertThat(mandate.getContractor().getName()).isEqualTo("Olivier");
                        assertions.assertThat(mandate.getNotary()).isNull();
                        assertions.assertThat(mandate.getMainHeir()).isNull();
                        assertions.assertThat(mandate.getOtherHeirs()).isEmpty();
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

        assertions.assertAll();
    }
    // endregion

    // region <<Exception cases>>
    @Test
    public void createCommandShouldRaiseErrorWhenEventsListIsNotEmpty() throws Exception {

        String id = "411455";
        MandateCreatedEvent event = new MandateCreatedEvent(id, "Socgen", new Contractor("Olivier"));
        event.getOtherHeirs().add(new Heir("Baptiste"));

        eventRepository.initWith(event);

        CreateMandateCommand command = new CreateMandateCommand(id, "Socgen", new Contractor("Olivier"), new Notary("Maitre Gallibert"), new Heir("Maryline"), new Heir("Baptiste"), new Heir("Emma"));

        StepVerifier.create(mandateService.createMandate(Mono.just(command))).
                expectError(MandateAlreadyCreatedException.class).
                verify();
    }

    @Test
    public void defineMainHeirCommandShouldRaiseErrorWhenEventsListIsEmpty() throws Exception {

        String id = "411455";

        DefineMainHeirCommand command = new DefineMainHeirCommand(id, new Heir("Maryline"));

        StepVerifier.create(mandateService.defineMainHeir(Mono.just(command))).
                expectError(MandateNotYetCreatedException.class).
                verify();
    }


    @Test
    public void defineNotaryCommandShouldRaiseErrorWhenEventsListIsEmpty() throws Exception {

        String id = "411455";

        DefineNotaryCommand command = new DefineNotaryCommand(id, new Notary("Maitre Galibert"));

        StepVerifier.create(mandateService.defineNotary(Mono.just(command))).
                expectError(MandateNotYetCreatedException.class).
                verify();
    }

    @Test
    public void addHeirsCommandShouldRaiseErrorWhenEventsListIsEmpty() throws Exception {

        String id = "411455";

        AddHeirsCommand command = new AddHeirsCommand(id, new Heir("Baptiste"));

        StepVerifier.create(mandateService.addHeirs(Mono.just(command))).
                expectError(MandateNotYetCreatedException.class).
                verify();
    }

    @Test
    public void removeHeirsCommandShouldRaiseErrorWhenEventsListIsEmpty() throws Exception {

        String id = "411455";

        RemoveHeirsCommand command = new RemoveHeirsCommand(id, new Heir("Baptiste"));

        StepVerifier.create(mandateService.removeHeirs(Mono.just(command))).
                expectError(MandateNotYetCreatedException.class).
                verify();
    }

    // endregion

    /*
    @Test
    public void defineNotaryCommandShouldRaiseEvent() throws Exception {

        CreateMandateCommand command1 = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), null, null, Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.createMandate(command1);

        DefineNotaryCommand command2 = new DefineNotaryCommand("411455", new Notary("Maitre Galibert"));
        mandateService.defineNotary(command2);

        Mandate mandate = new MandateProjector().apply(eventRepository.findAll("411455"));
        Assertions.assertThat(mandate.getNotary().getName()).isEqualTo("Maitre Galibert");
    }

    @Test(expected = MalformedCommandException.class)
    public void invalidCommand() throws Exception {

        try {
            CreateMandateCommand command1 = new CreateMandateCommand(null, "Socgen", new Contractor(null), null, null, Arrays.asList(new Heir(null), new Heir("Emma")));
            mandateService.createMandate(command1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/
}
