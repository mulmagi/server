package shop.mulmagi.app.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.enums.NotificationType;
import shop.mulmagi.app.service.NotificationService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class OverdueRentalProcessor implements ItemProcessor<Rental, Rental> {

    private final NotificationService notificationService;

    @Override
    public Rental process(final Rental rental) throws Exception {
        Integer rentalPeriod = rentalPeriod(rental);
        if (!rental.getIsOverdue() && rentalPeriod>7) {
            rental.updateIsOverdue();
        }
        if (rental.getIsOverdue()) {
            Boolean isReturn = rental.updateOverdueAmount(rentalPeriod(rental));

            if (isReturn){
                //notificationService.sendAndSaveNotification(rental.getUser(), NotificationType.OVERDUE, "보증금을 모두 소진하여 자동 반납 처리합니다.", "");
            }else {
                Integer beforeDeposit = 9000 - (rentalPeriod-8);
                Integer afterDeposit = 9000 - (rentalPeriod-7);
                String notificationBody = "보증금 | " + beforeDeposit + " 포인트 > " + afterDeposit + " 포인트";
                //notificationService.sendAndSaveNotification(rental.getUser(), NotificationType.OVERDUE, "우산이 연체되어 보증금을 차감합니다. 가까운 물막이에서 반납 부탁드립니다.", notificationBody);
            }
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
