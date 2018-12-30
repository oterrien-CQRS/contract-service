package com.ote.mandate;

import com.ote.mandate.business.api.IMandateService;
import com.ote.mandate.business.api.MandateServiceProvider;
import com.ote.mandate.business.model.Contractor;
import com.ote.mandate.business.model.Heir;
import com.ote.mandate.business.model.Notary;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import com.ote.mandate.business.spi.IMandateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MandateServiceTest {

    @Spy
    private EventPublisherMock eventPublisher = new EventPublisherMock();

    @Mock
    private IMandateRepository mandateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createCommandShouldRaiseEventAndCreateMandate() throws Exception {

        Mockito.when(mandateRepository.find(Mockito.anyString())).thenReturn(Optional.empty());

        IMandateService mandateService = MandateServiceProvider.getInstance().getFactory().createService(eventPublisher, eventPublisher, mandateRepository);

        CreateMandateCommand command = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), new Notary("Maitre"), new Heir("Maryline"), Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.apply(command);

        Mockito.verify(mandateRepository, Mockito.atLeastOnce()).save(Mockito.any());

    }
}
