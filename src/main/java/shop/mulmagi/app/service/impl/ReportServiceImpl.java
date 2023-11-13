package shop.mulmagi.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mulmagi.app.domain.UmbrellaStand;
import shop.mulmagi.app.repository.UmbrellaStandRepository;
import shop.mulmagi.app.service.ReportService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	private UmbrellaStandRepository umbrellaStandRepository;

	@Override
	public void receiveReport(Long id){
		UmbrellaStand umbrellaStand = umbrellaStandRepository.findById(id).orElseThrow();
		umbrellaStand.receiveReport();
		umbrellaStandRepository.save(umbrellaStand);
	}

	@Override
	public void solveReport(Long id){
		UmbrellaStand umbrellaStand = umbrellaStandRepository.findById(id).orElseThrow();
		umbrellaStand.solveReport();
		umbrellaStandRepository.save(umbrellaStand);
	}
}
