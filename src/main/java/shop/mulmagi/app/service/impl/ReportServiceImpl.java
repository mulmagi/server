package shop.mulmagi.app.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.Location;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.repository.LocationRepository;
import shop.mulmagi.app.repository.RentalRepository;
import shop.mulmagi.app.repository.UmbrellaStandRepository;
import shop.mulmagi.app.repository.UserRepository;
import shop.mulmagi.app.service.ReportService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	private final UmbrellaStandRepository umbrellaStandRepository;
	private final RentalRepository rentalRepository;
	private final LocationRepository locationRepository;
	private final UserRepository userRepository;

	@Override
	public void receiveReport(User user, Long id){
		UmbrellaStand umbrellaStand = umbrellaStandRepository.findById(id).orElseThrow();
		Rental rental = rentalRepository.findByReturnUmbrellaStand(umbrellaStand);
		Location location = umbrellaStand.getLocation();

		LocalDateTime rentalStartTime = rental.getCreatedAt();
		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(rentalStartTime.toLocalTime(), now.toLocalTime());

		if(diff.toMinutes() <= 5){ //5분내로 반납후 고장신고할 경우
			user.returnPoint(1000); //9000은 return시 기본 반환?
			userRepository.save(user);
		}

		location.receiveReport();
		rental.receiveReport();
		umbrellaStand.receiveReport();

		locationRepository.save(location);
		rentalRepository.save(rental);
		umbrellaStandRepository.save(umbrellaStand);
	}

	@Override
	public void solveReport(Long id){
		UmbrellaStand umbrellaStand = umbrellaStandRepository.findById(id).orElseThrow();
		Rental rental = rentalRepository.findByReturnUmbrellaStand(umbrellaStand);
		Location location = umbrellaStand.getLocation();

		location.solveReport();
		rental.solveReport();
		umbrellaStand.solveReport();

		locationRepository.save(location);
		rentalRepository.save(rental);
		umbrellaStandRepository.save(umbrellaStand);
	}
}
