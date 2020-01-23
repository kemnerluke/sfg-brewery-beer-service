package guru.sfg.brewery.beerservice.services.brewing;


import guru.sfg.brewery.beerservice.config.JmsConfig;
import guru.sfg.brewery.beerservice.domain.Beer;
import guru.sfg.brewery.beerservice.model.BeerDto;
import guru.sfg.brewery.beerservice.model.events.BrewBeerEvent;
import guru.sfg.brewery.beerservice.model.events.NewInventoryEvent;
import guru.sfg.brewery.beerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Component
public class BrewBeerListener {

    private final JmsTemplate jmsTemplate;
    private final BeerRepository beerRepository;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent brewBeerEvent){

        BeerDto dto = brewBeerEvent.getBeerDto();

        Beer beer = beerRepository.getOne(dto.getId());
        //Brewing some beer
        dto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(dto);

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}
