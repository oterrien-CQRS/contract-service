package com.ote.mandate.business;

import com.ote.mandate.business.api.IMandateCommandService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MandateServiceTest {

    @Spy
    private EventRepositoryMock eventRepository = new EventRepositoryMock();

    private IMandateCommandService mandateService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        eventRepository.clean();
    }
/*
    @Test
    public void createCommandShouldRaiseEventAndCreateMandate() throws Exception {

        CreateMandateCommand command = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), new Notary("Maitre"), new Heir("Maryline"), Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.createMandate(command);

        Mandate mandate = new MandateProjector().project(eventRepository.findAll("411455"));

        Assertions.assertThat(mandate).isNotNull();
    }

    @Test
    public void defineMainHeirCommandShouldRaiseEvent() throws Exception {

        CreateMandateCommand command1 = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), new Notary("Maitre"), null, Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.createMandate(command1);

        DefineMainHeirCommand command2 = new DefineMainHeirCommand("411455", new Heir("Maryline"));
        mandateService.defineMainHeir(command2);

        Mandate mandate = new MandateProjector().project(eventRepository.findAll("411455"));
        Assertions.assertThat(mandate.getMainHeir().getName()).isEqualTo("Maryline");
    }

    @Test
    public void defineNotaryCommandShouldRaiseEvent() throws Exception {

        CreateMandateCommand command1 = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), null, null, Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.createMandate(command1);

        DefineNotaryCommand command2 = new DefineNotaryCommand("411455", new Notary("Maitre Galibert"));
        mandateService.defineNotary(command2);

        Mandate mandate = new MandateProjector().project(eventRepository.findAll("411455"));
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
