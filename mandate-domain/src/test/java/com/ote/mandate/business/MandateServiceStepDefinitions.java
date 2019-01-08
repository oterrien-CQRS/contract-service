package com.ote.mandate.business;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.api.MandateServiceProvider;
import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Notary;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class MandateServiceStepDefinitions {

    public static final String MAIN_HEIR = "MAIN_HEIR";
    public static final String CLIENT_NAME = "CLIENT_NAME";
    public static final String OTHER_HEIR = "OTHER_HEIR";
    public static final String NOTARY = "NOTARY";
    public static final String MANDATE_ID = "MANDATE_ID";
    public static final String RESULT = "RESULT";

    private EventRepositoryMock eventRepository = new EventRepositoryMock();

    private IMandateCommandService mandateService = MandateServiceProvider.getInstance().getFactory().createService(eventRepository);

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
        scenarioContext.put(OTHER_HEIR, otherHeirs, ScenarioContext.Type.INPUT);
    }

    @Given("my notary is \"(.*)\"")
    public void hisNotaryIs(String notary) {
        scenarioContext.put(NOTARY, notary, ScenarioContext.Type.INPUT);
    }

    @Given("I have signed a succession mandate with id \"(.*)\"")
    public void itsSuccessionMandateWithId(String id) {
        scenarioContext.put(MANDATE_ID, id, ScenarioContext.Type.INPUT);
    }

    @Given("\"(.*)\" is set with \"(.*)\"")
    public void givenFieldEqual(String field, String value) {
        scenarioContext.put("FIELD_" + field, value, ScenarioContext.Type.INPUT);
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

        Optional<List<String>> otherHeirs = scenarioContext.get(OTHER_HEIR, ScenarioContext.Type.INPUT);
        Assertions.assertThat(otherHeirs).isPresent();

        CreateMandateCommand command = new CreateMandateCommand(id, "Bank",
                new Contractor(clientName.get()),
                new Notary(notary.get()),
                new Heir(mainHeir.get()),
                otherHeirs.get().stream().map(p -> new Heir(p)).collect(Collectors.toSet()));

        mandateService.createMandate(Mono.just(command)).
                subscribe(b -> scenarioContext.put(RESULT, b, ScenarioContext.Type.OUTPUT));
    }


    @When("I want to amend the \"(.*)\" to \"(.*)\"")
    public void thisClientSetTheFIELDTo(String field, String value) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
    // endregion

    // region <<THEN>>
    @Then("a mandate is created")
    public void thenMandateIsCreated() {

        Optional<Boolean> result = scenarioContext.get(RESULT, ScenarioContext.Type.OUTPUT);
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get()).isTrue();
    }


    @Then("mandate's id is not null")
    public void thenMandateIsNonNull() {

        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();
    }


    @Then("\"(.*)\" is equal to \"(.*)\"")
    public void thenMandateIsAmendedInConsequence(String field, String value) {

/*        Optional<String> id = scenarioContext.get(MANDATE_ID, ScenarioContext.Type.INPUT);
        Assertions.assertThat(id).isPresent();

        SoftAssertions assertions = new SoftAssertions();
        StepVerifier.create(eventRepository.findAll(Mono.just(id.get())).
                collectList()).
                assertNext(events -> {
                    try (MandateProjector projector = new MandateProjector()) {
                        Mandate mandate = projector.apply(events);
                        Field[] fields = mandate.getClass().getFields();
                        Optional<Field> f = Stream.of(fields).filter(fi -> fi.getName().equals(field)).peek(p -> p.setAccessible(true)).
                                findAny();
                        Assertions.assertThat(f).isPresent();
                        String v = (String) f.get().get(mandate);
                        Assertions.assertThat(v).isEqualTo(value);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                verifyComplete();*/
    }

    // endregion
}
