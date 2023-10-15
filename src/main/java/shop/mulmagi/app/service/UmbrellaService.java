package shop.mulmagi.app.service;

import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

public interface UmbrellaService {

    UmbrellaResponseDto.LocationDto getLocation(Long locationId);
}
