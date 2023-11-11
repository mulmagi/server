package shop.mulmagi.app.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.repository.RentalRepository;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueRentalWriter implements ItemWriter<Rental> {
    private final RentalRepository rentalRepository;

    @Override
    public void write(List<? extends Rental> rentals) throws Exception {
        for (Rental rental : rentals) {
            rentalRepository.save(rental);
        }
    }
}