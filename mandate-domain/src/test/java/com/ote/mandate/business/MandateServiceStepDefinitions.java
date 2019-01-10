package com.ote.mandate.business;

import com.ote.mandate.business.aggregate.*;
import com.ote.mandate.business.command.api.IMandateCommandHandler;
import com.ote.mandate.business.command.api.MandateCommandHandlerProvider;
import com.ote.mandate.business.command.model.*;
import com.ote.mandate.business.event.model.MandateCreatedEvent;
import com.ote.mandate.business.event.model.MandateHeirAddedEvent;
import com.ote.mandate.business.event.model.MandateMainHeirDefinedEvent;
import com.ote.mandate.business.event.model.MandateNotaryDefinedEvent;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.assertj.core.api.Assertions;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MandateServiceStepDefinitions {

    public static final String MAIN_HEIR = "MAIN_HEIR";
    public static final String CLIENT_NAME = "CLIENT_NAME";
    public static final String OTHER_HEIRS = "OTHER_HEIRS";
    public static final String NOTARY = "NOTARY";
    public static final String MANDATE_ID = "MANDATE_ID";
    public static final String RESULT = "RESULT";

    private EventRepositoryMock eventRepository = new EventRepositoryMock();

    private IMandateCommandHandler mandateService = MandateCommandHandlerProvider.getInstance().getHandlerFactory().createService(eventRepository);

    private final ScenarioContext scenarioContext = new ScenarioContext();

    @After
    public void tearDown() {
        eventRepository.clean();
        scenarioContext.clean();
    }

    // Region <<GIVEN>>
    @Given("I am \"(.*)\"")
    public void aClientNamed(String name) {
        scenarioContext.put(CLIENT_NAME, name, ScenarioContext.Type.INPUT);
    }

    @Given("I have designed \"(.*)\" as my main heir")
    public void hisMainHeirIs(String mainHeir) {
        scenarioContext.put(MAIN_HEIR, mainHeir, ScenarioContext.Type.INPUT);
    }

    @Given("I have designed following people as other heirs:")
    public void histOtherHeirsAre(List<String> otherHeirs) {
        scenarioContext.put(OTHER_HEIRS, String.join(",", otherHeirs), ScenarioContext.Type.INPUT);
    }

    @Given("my notary is \"(.*)\"")
    public void hisNotaryIs(String notary) {
        scenarioContext.put(NOTARY, notary, ScenarioContext.Type.INPUT);
    }

    @Given("I have signed a succession mandate with id \"(.*)\"")
    public void itsSuccessionMandateWithId(String id) {
        scenarioContext.put(MANDATE_ID, id, ScenarioContext.Type.INPUT);
        Optional<String> clientName = scenarioContext.get(CLIENT_NAME, ScenarioContext.Type.INPUT);
        Assertions.assertThat(clientName).isPresent();

        MandateCreatedEvent event = new MandateCreatedEvent(id, "mock", new Contractor(clientName.get()));
        eventRepository.initWith(event);
    }

    @Given("\"(.*)\" is set with \"(.*)\"")
    public void givenFieldEqual(String field, String value) {

        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();

        switch (field) {
            case "Notary": {
                scenarioContext.put(NOTARY, value, ScenarioContext.Type.INPUT);
                MandateNotaryDefinedEvent event = new MandateNotaryDefinedEvent(id.get(), new Notary(value));
                eventRepository.initWith(event);
                break;
            }
            case "MainHeir": {
                scenarioContext.put(MAIN_HEIR, value, ScenarioContext.Type.INPUT);
                MandateMainHeirDefinedEvent event = new MandateMainHeirDefinedEvent(id.get(), new Heir(value));
                eventRepository.initWith(event);
                break;
            }
            case "OtherHeirs": {
                scenarioContext.put(OTHER_HEIRS, value, ScenarioContext.Type.INPUT);
                List<String> newHeirs = Stream.of(value.split(",")).map(p -> p.trim()).collect(Collectors.toList());
                eventRepository.
                        initWith(newHeirs.stream().map(p -> new MandateHeirAddedEvent(id.get(), new Heir(p))).toArray(MandateHeirAddedEvent[]::new));
                break;
            }
            default:
                throw new NotImplementedException("Field " + field + " is not yet mapped");
        }
    }
    // endregion

    // region <<WHEN>>
    @When("I want to sign succession contract with my bank")
    public void thisClientWantsToSignSuccessionContractWithHisBank() throws Exception {

        String id = UUID.randomUUID().toString();

        Optional<String> clientName = scenarioContext.get(CLIENT_NAME, ScenarioContext.Type.INPUT);
        Assertions.assertThat(clientName).isPresent();

        Optional<String> notary = scenarioContext.get(NOTARY, ScenarioContext.Type.INPUT);
        Assertions.assertThat(notary).isPresent();

        Optional<String> mainHeir = scenarioContext.get(MAIN_HEIR, ScenarioContext.Type.INPUT);
        Assertions.assertThat(mainHeir).isPresent();

        Optional<String> otherHeirs = scenarioContext.get(OTHER_HEIRS, ScenarioContext.Type.INPUT);
        Assertions.assertThat(otherHeirs).isPresent();

        CreateMandateCommand command = new CreateMandateCommand(id, "Bank",
                new Contractor(clientName.get()),
                new Notary(notary.get()),
                new Heir(mainHeir.get()),
                Stream.of(otherHeirs.get().split(",")).map(p -> p.trim()).map(p -> new Heir(p)).collect(Collectors.toSet()));

        mandateService.createMandate(Mono.just(command)).
                subscribe(b -> scenarioContext.put(RESULT, Boolean.toString(b), ScenarioContext.Type.OUTPUT));
    }


    @When("I want to amend the \"(.*)\" to \"(.*)\"")
    public void thisClientSetTheFIELDTo(String field, String value) throws Exception {

        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();

        switch (field) {
            case "Notary": {
                DefineNotaryCommand command = new DefineNotaryCommand(id.get(), new Notary(value));
                mandateService.defineNotary(Mono.just(command)).
                        subscribe(b -> scenarioContext.put(RESULT, Boolean.toString(b), ScenarioContext.Type.OUTPUT));
                break;
            }
            case "MainHeir": {
                DefineMainHeirCommand command = new DefineMainHeirCommand(id.get(), new Heir(value));
                mandateService.defineMainHeir(Mono.just(command)).
                        subscribe(b -> scenarioContext.put(RESULT, Boolean.toString(b), ScenarioContext.Type.OUTPUT));
                break;
            }
            case "OtherHeirs": {
                Optional<String> otherHeirsOpt = scenarioContext.get(OTHER_HEIRS, ScenarioContext.Type.INPUT);
                List<String> oldHeirs = Stream.of(otherHeirsOpt.get().split(",")).map(p -> p.trim()).collect(Collectors.toList());
                List<String> newHeirs = Stream.of(value.split(",")).map(p -> p.trim()).collect(Collectors.toList());

                AtomicBoolean result = new AtomicBoolean();
                {
                    Set<Heir> heirToAdd = newHeirs.stream().filter(p -> !oldHeirs.contains(p)).map(p -> new Heir(p)).collect(Collectors.toSet());
                    if (!heirToAdd.isEmpty()) {
                        AddHeirsCommand command = new AddHeirsCommand(id.get(), heirToAdd);
                        mandateService.addHeirs(Mono.just(command)).
                                subscribe(b -> result.set(b));
                    }
                }
                {
                    Set<Heir> heirToRemove = oldHeirs.stream().filter(p -> !newHeirs.contains(p)).map(p -> new Heir(p)).collect(Collectors.toSet());
                    if (!heirToRemove.isEmpty()) {
                        RemoveHeirsCommand command = new RemoveHeirsCommand(id.get(), heirToRemove);
                        mandateService.removeHeirs(Mono.just(command)).
                                subscribe(b -> result.set(b));
                    }
                }
                scenarioContext.put(RESULT, Boolean.toString(result.get()), ScenarioContext.Type.OUTPUT);

                break;
            }
            default:
                throw new NotImplementedException("Field " + field + " is not yet mapped");
        }
    }
    // endregion

    // region <<THEN>>
    @Then("a mandate is created")
    public void thenMandateIsCreated() {

        Optional<String> result = scenarioContext.get(RESULT, ScenarioContext.Type.OUTPUT);
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.map(p -> Boolean.parseBoolean(p)).get()).isTrue();
    }


    @Then("mandate's id is not null")
    public void thenMandateIsNonNull() {

        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();
    }


    @Then("\"(.*)\" is equal to \"(.*)\"")
    public void thenMandateIsAmendedInConsequence(String field, String value) {

        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();

        StepVerifier.create(eventRepository.findAll(Mono.just(id.get())).
                collectList()).
                assertNext(events -> {
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        Field[] fields = mandate.getClass().getDeclaredFields();
                        Optional<Field> f = Stream.of(fields).
                                filter(fi -> fi.getName().equalsIgnoreCase(field)).
                                peek(p -> p.setAccessible(true)).
                                findAny();
                        Assertions.assertThat(f).isPresent();
                        Object v = f.get().get(mandate);

                        if (f.get().getName().equalsIgnoreCase("bankName")) {
                            Assertions.assertThat((String) v).isEqualTo(value);
                        } else if (f.get().getName().equalsIgnoreCase("notary")) {
                            Assertions.assertThat(((Notary) v).getName()).isEqualTo(value);
                        } else if (f.get().getName().equalsIgnoreCase("mainHeir")) {
                            Assertions.assertThat(((Heir) v).getName()).isEqualTo(value);
                        } else if (f.get().getName().equalsIgnoreCase("contractor")) {
                            Assertions.assertThat(((Contractor) v).getName()).isEqualTo(value);
                        } else if (f.get().getName().equalsIgnoreCase("otherHeirs")) {
                            List<Heir> otherHeirs = (List<Heir>) v;
                            List<String> newHeirs = Stream.of(value.split(",")).map(p -> p.trim()).collect(Collectors.toList());
                            otherHeirs.stream().allMatch(p -> newHeirs.contains(p));
                            newHeirs.stream().allMatch(p -> otherHeirs.contains(p));
                        } else {
                            throw new NotImplementedException(v.getClass().getName() + " is not yet mapped");
                        }
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();

    }

    // endregion
}
