package overtime.example.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.service.MonthlySubmitDataService;
import overtime.example.repository.MonthlySubmitDataMapper;

@Service
public class MonthlySubmitDataServiceImpl implements MonthlySubmitDataService {

	@Autowired
	private MonthlySubmitDataMapper mapper;

	/** 次長月次資料提出（1件データ作成） */
	@Override
	public int monthlySubmit(String yearmonth) {
		return mapper.insertOne(yearmonth);
	}
	
	/** 月次資料が提出済みかどうか（1件データ取得） */
	public Integer getMonthlySubmitted(String yearmonth) {
		return mapper.findOne(yearmonth);
	}
}
