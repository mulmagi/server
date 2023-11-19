package shop.mulmagi.app.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UmbrellaConverter {
    public UmbrellaResponseDto.LocationDataListDto.LocationDataDto toLocationDataDto(Location location) {
        return UmbrellaResponseDto.LocationDataListDto.LocationDataDto.builder()
                .locationId(location.getId())
                .point(UmbrellaResponseDto.LocationDataListDto.PointDto.builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build())
                .build();
    }

    public List<UmbrellaResponseDto.LocationDataListDto.LocationDataDto> toLocationDataDtoList(List<Location> nearbyLocations) {
        return nearbyLocations.stream()
                .map(this::toLocationDataDto)
                .collect(Collectors.toList());
    }

    public UmbrellaResponseDto.LocationDataListDto toLocationDataListDto(List<Location> nearbyLocations, Boolean isRental) {
            return UmbrellaResponseDto.LocationDataListDto.builder()
                    .isRental(isRental)
                    .LocationData(this.toLocationDataDtoList(nearbyLocations))
                    .build();
    }

    public UmbrellaResponseDto.LocationDto toLocation(Boolean isRental, Location location, List<Integer> umbrellaStandNumberList){
        return UmbrellaResponseDto.LocationDto.builder()
                .locationId(location.getId())
                .isRental(isRental)
                .name(location.getName())
                .umbrellaStandNumber(umbrellaStandNumberList)
                .build();
    }

    public UmbrellaResponseDto.RentalPageDto toRentalPage(Location location, String umbrellaStandNumber, Integer userPoint, UmbrellaStand umbrellaStand){
        return UmbrellaResponseDto.RentalPageDto.builder()
                .umbrellaStandId(umbrellaStand.getId())
                .isUmbrella(umbrellaStand.getIsUmbrella())
                .isWrong(umbrellaStand.getIsWrong())
                .rentalUmbrellaStand(String.join(" ", location.getName(), umbrellaStandNumber))
                .userPoint(userPoint)
                .build();
    }

    public UmbrellaResponseDto.ReturnPageDto toReturnPage(Rental rental, String rentalStr, String returnStr){
        return UmbrellaResponseDto.ReturnPageDto.builder()
                .rentalId(rental.getId())
                .rentalTime(rental.getCreatedAt())
                .returnTime(rental.getUpdatedAt())
                .rentalUmbrellaStand(rentalStr)
                .returnUmbrellaStand(returnStr)
                .overDueAmount(rental.getOverdueAmount())
                .build();
    }
}
