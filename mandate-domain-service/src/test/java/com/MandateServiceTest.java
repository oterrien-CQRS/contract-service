package com;

import com.ote.framework.IEvent;
import com.ote.framework.IEventHandler;
import com.ote.mandate.business.api.IMandateService;
import com.ote.mandate.business.api.MandateServiceProvider;
import com.ote.mandate.business.model.Contractor;
import com.ote.mandate.business.model.Heir;
import com.ote.mandate.business.model.Notary;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import com.ote.mandate.business.spi.IEventRepository;
import com.ote.mandate.business.spi.IMandateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class MandateServiceTest {

    @Spy
    private IEventRepository eventRepository = new IEventRepository() {

        private Map<String, Set<IEventHandler>> observer = new HashMap<>();

        @Override
        public void storeAndPublish(IEvent event) {
            System.out.println("Storing event " + event);
            System.out.println("Publishing event " + event);

            observer.entrySet().stream().
                    filter(p -> event.getClass().getTypeName().equals(p.getKey())).
                    findAny().
                    map(p -> p.getValue()).
                    ifPresent(p -> {
                        p.parallelStream().
                                forEach(
                                        h -> {
                                            try {
                                                System.out.println("Calling " + h.toString());
                                                h.handle(event);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );
                    });
        }

        @Override
        public <TE extends IEvent> void subscribe(Class<TE> eventClass, IEventHandler<TE> consumer) {
            observer.computeIfAbsent(eventClass.getTypeName(), k -> new HashSet<>()).add(consumer);
        }
    };

    @Mock
    private IMandateRepository mandateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createCommandShouldRaiseEventAndCreateMandate() throws Exception {

        Mockito.when(mandateRepository.find(Mockito.anyString())).thenReturn(Optional.empty());

        IMandateService mandateService = MandateServiceProvider.getInstance().getFactory().createService(eventRepository, mandateRepository);

        CreateMandateCommand command = new CreateMandateCommand("411455", "Socgen", new Contractor("Olivier"), new Notary("Maitre"), new Heir("Maryline"), Arrays.asList(new Heir("Baptiste"), new Heir("Emma")));
        mandateService.apply(command);

        Mockito.verify(mandateRepository, Mockito.atLeastOnce()).save(Mockito.any());

    }
}
