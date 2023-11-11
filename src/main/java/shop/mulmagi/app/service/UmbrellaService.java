package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

public interface UmbrellaService {

    UmbrellaResponseDto.LocationDto getRentalLocation(Long locationId);
    UmbrellaResponseDto.LocationDto getReturnLocation(Long locationId);
    UmbrellaResponseDto.RentalPageDto getRentalPage(User user, UmbrellaRequestDto.RentalPageDto request);
    UmbrellaResponseDto.ReturnPageDto getReturnPage(UmbrellaRequestDto.ReturnPageDto request);
    void rental(User user, UmbrellaRequestDto.RentalDto request);
    void returnUmb(User user, UmbrellaRequestDto.ReturnDto request);
}
