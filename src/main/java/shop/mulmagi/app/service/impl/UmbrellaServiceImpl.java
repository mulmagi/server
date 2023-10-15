package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.converter.UmbrellaConverter;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.repository.LocationRepository;
import shop.mulmagi.app.repository.UmbrellaStandRepository;
import shop.mulmagi.app.service.UmbrellaService;
import shop.mulmagi.app.web.dto.UmbrellaRequestDto;
import shop.mulmagi.app.web.dto.UmbrellaResponseDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UmbrellaServiceImpl implements UmbrellaService {

    private final LocationRepository locationRepository;
    private final UmbrellaStandRepository umbrellaStandRepository;
    private final UmbrellaConverter umbrellaConverter;

    @Override
    public UmbrellaResponseDto.LocationDto getLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NoSuchElementException("Location not found."));

        List<Integer> umbrellaStandNumberList = umbrellaStandRepository.findNumbersByLocationAndIsWrong(locationId, false);

        return umbrellaConverter.toLocation(location, umbrellaStandNumberList);
    }

    @Override
    public UmbrellaResponseDto.RentalPageDto getRentalPage(User user, UmbrellaRequestDto.RentalPageDto request){
        UmbrellaStand umbrellaStand = umbrellaStandRepository.findByQrCode(request.getQrCode());
        String umbrellaStandNumber = String.valueOf(umbrellaStand.getNumber());

        Location location = umbrellaStand.getLocation();

        Integer userPoint = user.getPoint();

        return umbrellaConverter.toRentalPage(location, umbrellaStandNumber, userPoint);
    }
}
