package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

import java.util.List;

public interface UmbrellaService {
    UmbrellaResponseDto.LocationDataListDto getLocationData(User user, Double userLatitude, Double userLongitude);
    UmbrellaResponseDto.LocationDto getLocation(User user, Long locationId);
    UmbrellaResponseDto.RentalPageDto getRentalPage(User user, Long qrCode);
    UmbrellaResponseDto.ReturnPageDto getReturnPage(Long rentalId);
    void rental(User user, UmbrellaRequestDto.RentalDto request);
    void returnUmb(User user, UmbrellaRequestDto.ReturnDto request);
}
