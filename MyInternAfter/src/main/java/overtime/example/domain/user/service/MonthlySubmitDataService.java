package overtime.example.domain.user.service;

public interface MonthlySubmitDataService {

	/** 次長月次資料提出（1件データ作成） */
	public int monthlySubmit(String yearmonth);
	
	/** 月次資料が提出済みかどうか（1件データ取得） */
	public Integer getMonthlySubmitted(String yearmonth);
}
