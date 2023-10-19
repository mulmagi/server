package shop.mulmagi.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mulmagi.app.converter.UmbrellaConverter;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.exception.CustomExceptions;
import shop.mulmagi.app.repository.LocationRepository;
import shop.mulmagi.app.repository.RentalRepository;
import shop.mulmagi.app.repository.UmbrellaStandRepository;
import shop.mulmagi.app.repository.UserRepository;
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
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

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

        return umbrellaConverter.toRentalPage(location, umbrellaStandNumber, userPoint, umbrellaStand);
    }

    @Override
    public UmbrellaResponseDto.ReturnPageDto getReturnPage(UmbrellaRequestDto.ReturnPageDto request){
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NoSuchElementException("Rental not found."));

        UmbrellaStand rentalUmbrellaStand = rental.getRentalUmbrellaStand();
        Location rentalLocation = rentalUmbrellaStand.getLocation();
        String rentalUmbrellaStandNumber = String.valueOf(rentalUmbrellaStand.getNumber());
        String rentalStr = String.join(" ", rentalLocation.getName(), rentalUmbrellaStandNumber);

        UmbrellaStand returnUmbrellaStand = rental.getReturnUmbrellaStand();
        Location returnLocation = returnUmbrellaStand.getLocation();
        String returnUmbrellaStandNumber = String.valueOf(returnUmbrellaStand.getNumber());
        String returnStr = String.join(" ", returnLocation.getName(), returnUmbrellaStandNumber);

        return umbrellaConverter.toReturnPAge(rental, rentalStr, returnStr);
    }

    @Override
    @Transactional
    public void rental(User user, UmbrellaRequestDto.RentalDto request) throws CustomExceptions.Exception {
        UmbrellaStand umbrellaStand = umbrellaStandRepository.findById(request.getUmbrellaStandId())
                .orElseThrow(() -> new NoSuchElementException("Umbrella stand not found."));

        if (user.getIsRental()) {
            throw new CustomExceptions.Exception("이미 대여 중인 사용자입니다.");
        } else if (user.getPoint() < 10000) {
            throw new CustomExceptions.Exception("충전이 필요합니다.");
        } else if (!umbrellaStand.getIsUmbrella()) {
            throw new CustomExceptions.Exception("빈 우산꽂이입니다.");
        } else if (umbrellaStand.getIsWrong()){
            throw new CustomExceptions.Exception("고장난 우산입니다.");
        }

        umbrellaStand.updateRental();
        user.updateRental();
        user.updatePoint();

        Rental rental = umbrellaConverter.toRental(user, umbrellaStand);

        rentalRepository.save(rental);
        userRepository.save(user);
        umbrellaStandRepository.save(umbrellaStand);
    }

    @Override
    @Transactional
    public void returnUmb(User user, UmbrellaRequestDto.ReturnDto request) throws CustomExceptions.Exception {
        UmbrellaStand umbrellaStand = umbrellaStandRepository.findByQrCode(request.getQrCode());

        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NoSuchElementException("Rental not found."));

        if (umbrellaStand.getIsUmbrella()) {
            throw new CustomExceptions.Exception("빈 우산꽂이를 이용하세요.");
        } else if (!user.getIsRental()){
            throw new CustomExceptions.Exception("우산을 대여 중이지 않은 사용자입니다.");
        } else if (rental.getIsReturn()){
            throw new CustomExceptions.Exception("이미 반납된 우산입니다.");
        }

        umbrellaStand.updateReturn();
        user.updateReturn();
        user.returnPoint(9000 - rental.getOverDueAmount());
        rental.updateReturn(umbrellaStand);

        rentalRepository.save(rental);
        userRepository.save(user);
        umbrellaStandRepository.save(umbrellaStand);
    }
}
