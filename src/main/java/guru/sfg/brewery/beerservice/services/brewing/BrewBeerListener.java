package guru.sfg.brewery.beerservice.services.brewing;


import guru.sfg.brewery.beerservice.config.JmsConfig;
import guru.sfg.brewery.beerservice.domain.Beer;
import guru.sfg.brewery.beerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class BrewBeerListener {

   // private final JmsTemplate jmsTemplate;
    private final BeerRepository beerRepository;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    @Scheduled(fixedRate = 5000)//every 5 seconds
    public void listen(){

        Beer beer= (Beer) rabbitTemplate.receiveAndConvert(JmsConfig.BREWING_REQUEST_QUEUE);


//        BrewBeerEvent brewBeerEvent= (BrewBeerEvent) rabbitTemplate.receiveAndConvert(JmsConfig.BREWING_REQUEST_QUEUE);
//        BeerDto beerDto = brewBeerEvent.getBeerDto();

        Beer beerZero = beerRepository.getOne(beer.getId());
        //Brewing some beer
        beer.setQuantityOnHand(beer.getQuantityToBrew());

//        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beer);

//        log.debug("new inventory equals" + newInventoryEvent.toString() );

        log.debug("Brewed beer " + beer.getMinOnHand() + " : QOH: " +beer.getQuantityOnHand());
        //jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
//       rabbitTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
        rabbitTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, beer);


    }
}
