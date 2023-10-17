package shop.mulmagi.app.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UmbrellaConverter {

    public UmbrellaResponseDto.LocationDto toLocation(Location location, List<Integer> umbrellaStandNumberList){
        return UmbrellaResponseDto.LocationDto.builder()
                .locationId(location.getId())
                .name(location.getName())
                .umbrellaCount(location.getUmbrellaCount())
                .umbrellaNumber(umbrellaStandNumberList)
                .build();
    }

    public UmbrellaResponseDto.RentalPageDto toRentalPage(Location location, String umbrellaStandNumber, Integer userPoint, UmbrellaStand umbrellaStand){
        return UmbrellaResponseDto.RentalPageDto.builder()
                .isUmbrella(umbrellaStand.getIsUmbrella())
                .isWrong(umbrellaStand.getIsWrong())
                .rentalUmbrellaStand(String.join(" ", location.getName(), umbrellaStandNumber))
                .userPoint(userPoint)
                .build();
    }

    public UmbrellaResponseDto.ReturnPageDto toReturnPAge(Rental rental, String rentalStr, String returnStr){
        return UmbrellaResponseDto.ReturnPageDto.builder()
                .rentalId(rental.getId())
                .rentalTime(rental.getCreatedAt())
                .returnTime(rental.getUpdatedAt())
                .rentalUmbrellaStand(rentalStr)
                .returnUmbrellaStand(returnStr)
                .overDueAmount(rental.getOverDueAmount())
                .build();
    }

    public Rental toRental(User user, UmbrellaStand umbrellaStand){
        return Rental.builder()
                .user(user)
                .rentalUmbrellaStand(umbrellaStand)
                .isOverdue(false)
                .isReturn(false)
                .isWrong(false)
                .overDueAmount(0)
                .build();
    }
}
