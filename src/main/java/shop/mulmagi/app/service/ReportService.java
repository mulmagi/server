package shop.mulmagi.app.service;

import shop.mulmagi.app.domain.User;

public interface ReportService {

	public void receiveReport(User user, Long id);
	public void solveReport(Long id);
}
