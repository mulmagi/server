package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

public interface UmbrellaService {

    UmbrellaResponseDto.LocationDto getLocation(Long locationId);
    UmbrellaResponseDto.RentalPageDto getRentalPage(User user, UmbrellaRequestDto.RentalPageDto request);
    UmbrellaResponseDto.ReturnPageDto getReturnPage(UmbrellaRequestDto.ReturnPageDto request);
    String rental(User user, UmbrellaRequestDto.RentalDto request);
}
