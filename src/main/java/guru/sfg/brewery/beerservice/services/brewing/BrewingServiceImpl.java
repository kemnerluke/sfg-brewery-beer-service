package guru.sfg.brewery.beerservice.services.brewing;


import guru.sfg.brewery.beerservice.config.JmsConfig;
import guru.sfg.brewery.beerservice.domain.Beer;
import guru.sfg.brewery.beerservice.model.events.BrewBeerEvent;
import guru.sfg.brewery.beerservice.repositories.BeerRepository;
import guru.sfg.brewery.beerservice.services.inventory.BeerInventoryService;
import guru.sfg.brewery.beerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingServiceImpl implements BrewingService {

    private final BeerInventoryService beerInventoryService;
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Override
    @Transactional
    @Scheduled(fixedRate = 5000) //run every 5 seconds
    public void checkForLowInventory() {
        log.debug("Checking Beer Inventory");

        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {

            Integer invQoh = beerInventoryService.getOnhandInventory(beer.getId());

            if(beer.getMinOnHand() >= invQoh ) {
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE,
                        new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
