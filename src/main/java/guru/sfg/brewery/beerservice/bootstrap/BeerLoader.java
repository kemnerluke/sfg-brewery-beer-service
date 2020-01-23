package guru.sfg.brewery.beerservice.bootstrap;


import guru.sfg.brewery.beerservice.domain.Beer;
import guru.sfg.brewery.beerservice.domain.Brewery;
import guru.sfg.brewery.beerservice.model.BeerStyleEnum;
import guru.sfg.brewery.beerservice.repositories.BeerRepository;
import guru.sfg.brewery.beerservice.repositories.BreweryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;



@Slf4j
@Component
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final UUID BEER_1_UUID = UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb");
    public static final UUID BEER_2_UUID = UUID.fromString("a712d914-61ea-4623-8bd0-32c0f6545bfd");
    public static final UUID BEER_3_UUID = UUID.fromString("026cc3c8-3a0c-4083-a05b-e908048c1b08");

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;


    public BeerLoader(BreweryRepository breweryRepository,
                      BeerRepository beerRepository) {
        this.breweryRepository = breweryRepository;
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBreweryData();
        loadBeerData();
        checkInventory();
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .quantityOnHand(500)
                    .id(BEER_1_UUID)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .id(BEER_2_UUID)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .id(BEER_3_UUID)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
        }
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());
        }
    }

    public void checkInventory() {
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            AtomicInteger inventory_qoh = new AtomicInteger();

//            beer.getBeerInventory().forEach(inv -> inventory_qoh.addAndGet(inv.getQuantityOnHand()));
//
//            log.debug("Inv: " + beer.getBeerName() + " : QOH = " + inventory_qoh.get());
//
//            if(beer.getMinOnHand() >= inventory_qoh.get() ) {
//                //brew beer
//                publisher.publishEvent(new BrewBeerEvent(beer));
//            }
        });
    }

}