package guru.sfg.brewery.beerservice.model.events;

import guru.sfg.brewery.beerservice.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderRequest {
    private BeerOrderDto beerOrder;
}
