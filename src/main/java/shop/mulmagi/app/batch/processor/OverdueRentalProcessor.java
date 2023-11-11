package shop.mulmagi.app.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Rental;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class OverdueRentalProcessor implements ItemProcessor<Rental, Rental> {

    @Override
    public Rental process(final Rental rental) throws Exception {
        if (!rental.getIsOverdue() && rentalPeriod(rental)>7) {
            rental.updateIsOverdue();
        }
        if (rental.getIsOverdue()) {
            rental.updateOverdueAmount(rentalPeriod(rental));
        }

        return rental;
    }

    private Integer rentalPeriod(Rental rental) {
        LocalDateTime createdAt = rental.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Long rentalPeriod = ChronoUnit.DAYS.between(createdAt, now);

        return Math.toIntExact(rentalPeriod)+1;
    }
}
