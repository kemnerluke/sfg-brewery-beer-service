package guru.sfg.brewery.beerservice.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderValidationResult {
    private Boolean isValid;
    private UUID beerOrderId;
}
