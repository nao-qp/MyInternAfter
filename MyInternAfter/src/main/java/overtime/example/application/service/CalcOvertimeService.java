package overtime.example.application.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime.example.domain.user.model.Overtime;
import overtime.example.domain.user.service.ReportService;

@Service
public class CalcOvertimeService {

	@Autowired
	private ReportService reportService;
	
	
	/**
	 * 残業時間計算（平日）
	 * 勤務パターンの開始終了時間、前残業開始時間、後残業終了時間、休憩時間から
	 * 実際の残業時間を算出し、合計と残業時間の区分ごとの変数に振り分けて
	 * 既存データに加算した値を返す。
	 * 
	 * Overtimeオブジェクト
	 * overtime　実残業時間（該当日付のみの残業時間）
	 * wdayDtUnder60以下変数　実残業時間の累計（該当日付までの累計時間）
	 *
	 * @param usersId
	 * @param workPatternsStartTime
	 * @param workPatternsEndTime
	 * @param startTime
	 * @param endTime
	 * @param restStartTime
	 * @param restEndTime
	 * @return Overtime
	 */
	public Overtime toMinutesGetOvertimeWday(Integer usersId, 
			LocalTime workPatternsStartTime, LocalTime workPatternsEndTime,
			LocalTime startTime, LocalTime endTime, LocalTime restStartTime, LocalTime restEndTime) {
		
		//////////// 平日計算 ////////////
		////前残業時間計算////
		////前残業開始時間〜勤務パターン開始時間
		Duration duraBeforeOvertimeDt = Duration.ZERO;	//前残業日中
		Duration duraBeforeOvertimeEmn = Duration.ZERO;	//前残業早朝深夜
	    
		//前残業開始時間が5:30よりも前　→早朝深夜、日中
		if (startTime.isBefore(LocalTime.of(5, 30)) ) {
			//前残業開始時間 〜 5:30（早朝深夜）
			duraBeforeOvertimeEmn = Duration.between(startTime, LocalTime.of(5, 30));
			//5:30 〜 勤務パターン開始時間（日中）
			duraBeforeOvertimeDt = Duration.between(LocalTime.of(5, 30), workPatternsStartTime);
		} else {
		//前残業開始時間が5:30以降　→日中
			//前残業開始時間 〜 勤務パターン開始時間
			duraBeforeOvertimeDt = Duration.between(startTime, workPatternsStartTime);
		}
		
		
		////後残業時間計算////
		////勤務パターン終了時間〜後残業終了時間
		Duration duraAfterOvertimeDt = Duration.ZERO;	//後残業日中
		Duration duraAfterOvertimeEmn = Duration.ZERO;	//後残業早朝深夜
	  
		//後残業終了時間が22:00よりも前　→日中
		if (endTime.isBefore(LocalTime.of(22, 0))) {
			//勤務パターン終了時間 〜 後残業終了時間
			duraAfterOvertimeDt = Duration.between(workPatternsEndTime, endTime);
		} else {
			//後残業終了時間が22:00以降　→日中、早朝深夜
			//勤務パターン終了時間 〜 22:00（日中）
			duraAfterOvertimeDt = Duration.between(workPatternsEndTime, LocalTime.of(22, 0));
			//22:00 〜 後残業終了時間（深夜）
			duraAfterOvertimeEmn = Duration.between(LocalTime.of(22, 0), endTime);
		}
		

		////日中、早朝深夜に集計////
		//平日日中
		long wdayDt = duraBeforeOvertimeDt.toMinutes() + duraAfterOvertimeDt.toMinutes();
		//平日早朝深夜
		long wdayEmn = duraBeforeOvertimeEmn.toMinutes() + duraAfterOvertimeEmn.toMinutes();
		
		
		////休憩時間を除く////
		Map<String, Integer> splitRestTimeDtEmnMap = splitTimeDtEmn(restStartTime, restEndTime);
		int actualWdayDt = (int)wdayDt - splitRestTimeDtEmnMap.get("dt");
		int actualWdayEmn = (int)wdayEmn - splitRestTimeDtEmnMap.get("emn");
		
		
		////残業時間の区分に振り分け////
		//残業時間合計、平日日中60時間未満、平日日中60時間超え、平日早朝深夜60時間未満、平日早朝深夜60時間超え
		
		//残業時間合計（休憩時間を除く実残業時間合計）
		int overtimeSum = actualWdayDt + actualWdayEmn;

		//社員の最新のreportデータの累計残業時間を取得する。
		Overtime existingTotalOvertime = reportService.GetTotalOvertime(usersId);
		//平日日中残業時間をunder60とover60に分けて既存データに加算
		Map<String, Integer> splitWdayDtMap = splitUnderOver(actualWdayDt, 
				existingTotalOvertime.getWdayDtUnder60(), existingTotalOvertime.getWdayDtOver60());
		//平日日中60時間未満
		int wdayDtUnder60 = splitWdayDtMap.get("updUnderTotal");
		//平日日中60時間超え
		int wdayDtOver60 = splitWdayDtMap.get("updOverTotal");
		
		//平日早朝深夜残業時間をunder60とover60に分けて既存データに加算
		Map<String, Integer> splitWdayEmnMap = splitUnderOver(actualWdayEmn, 
				existingTotalOvertime.getWdayEmnUnder60(), existingTotalOvertime.getWdayEmnOver60());
		//平日早朝深夜60時間未満
		int wdayEmnUnder60 = splitWdayEmnMap.get("updUnderTotal");
		//平日早朝深夜60時間超え
		int wdayEmnOver60 = splitWdayEmnMap.get("updOverTotal");
		
		////Overtimeオブジェクトに設定する
		Overtime updOvertimeWday = new Overtime();
		//平日残業時間合計（日中 + 早朝深夜）
		updOvertimeWday.setActualOvertime(overtimeSum);
		//平日日中60時間未満
		updOvertimeWday.setWdayDtUnder60(wdayDtUnder60);
		//平日日中60時間超え
		updOvertimeWday.setWdayDtOver60(wdayDtOver60);
		//平日早朝深夜60時間未満
		updOvertimeWday.setWdayEmnUnder60(wdayEmnUnder60);
		//平日早朝深夜60時間超え
		updOvertimeWday.setWdayEmnOver60(wdayEmnOver60);
		
		return updOvertimeWday;
	}
	
	
	/**
	 * 残業時間計算（休日）
	 * 残業開始時間、残業終了時間、休憩時間から
	 * 実際の残業時間を算出し、合計と残業時間の区分ごとの変数に振り分けて
	 * 既存データに加算した値を返す。
	 * 
	 * @param usersId
	 * @param startTime
	 * @param endTime
	 * @param restStartTime
	 * @param restEndTime
	 * @return Overtime
	 */
	public Overtime toMinutesGetOvertimeHday(Integer usersId, 
			LocalTime startTime, LocalTime endTime, LocalTime restStartTime, LocalTime restEndTime) {
		
		////////////休日計算 ////////////
		////休日残業時間を日中、早朝深夜に分ける////
		Map<String, Integer> splitOverTimeDtEmnMap = splitTimeDtEmn(startTime, endTime);
		int hdayDt = splitOverTimeDtEmnMap.get("dt");
		int hdayEmn = splitOverTimeDtEmnMap.get("emn");
		
		////休憩時間を除く////
		Map<String, Integer> splitRestTimeDtEmnMap = splitTimeDtEmn(startTime, endTime);
		int actualHdayDt = hdayDt - splitRestTimeDtEmnMap.get("dt");
		int actualHdayEmn = hdayEmn - splitRestTimeDtEmnMap.get("emn");
		
		////残業時間の区分に振り分け////
		//残業時間合計、休日日中60時間未満、休日日中60時間超え、休日早朝深夜60時間未満、休日早朝深夜60時間超え
		
		//残業時間合計（休憩時間を除く実残業時間合計）
		int overtimeSum = actualHdayDt + actualHdayEmn;
		
		//社員の最新のreportデータの累計残業時間を取得する。
		Overtime existingTotalOvertime = reportService.GetTotalOvertime(usersId);
		
		//休日日中残業時間をunder60とover60に分けて既存データに加算
		Map<String, Integer> splitHdayDtMap = splitUnderOver(actualHdayDt, 
				existingTotalOvertime.getHdayDtUnder60(), existingTotalOvertime.getHdayDtOver60());
		//休日日中60時間未満
		int hdayDtUnder60 = splitHdayDtMap.get("updUnderTotal");
		//休日日中60時間超え
		int hdayDtOver60 = splitHdayDtMap.get("updOverTotal");
		
		//休日早朝深夜残業時間をunder60とover60に分けて既存データに加算
		Map<String, Integer> splitHdayEmnMap = splitUnderOver(actualHdayEmn, 
				existingTotalOvertime.getHdayEmnUnder60(), existingTotalOvertime.getHdayEmnOver60());
		//休日早朝深夜60時間未満
		int hdayEmnUnder60 = splitHdayEmnMap.get("updUnderTotal");
		//休日早朝深夜60時間超え
		int hdayEmnOver60 = splitHdayEmnMap.get("updOverTotal");
		
		////Overtimeオブジェクトに設定する
		Overtime updOvertimeHday = new Overtime();
		//休日残業時間合計（日中 + 早朝深夜）
		updOvertimeHday.setActualOvertime(overtimeSum);
		//休日日中60時間未満
		updOvertimeHday.setHdayDtUnder60(hdayDtUnder60);
		//休日日中60時間超え
		updOvertimeHday.setHdayDtOver60(hdayDtOver60);
		//休日早朝深夜60時間未満
		updOvertimeHday.setHdayEmnUnder60(hdayEmnUnder60);
		//休日早朝深夜60時間超え
		updOvertimeHday.setHdayEmnOver60(hdayEmnOver60);
			
		return updOvertimeHday;
	}
		
	
	/**
	 * 土日を休日とするメソッド
	 * TODO:（今回祝日は考慮しない）
	 * 
	 * @param date
	 * @return turue or false（休日か平日か）
	 */
    public boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
    
    
    /**
     * 残業時間と既存データの60時間未満と60時間超え累計時間を受け取って、
     * 残業時間をunder60とover60に分けて、プラスした累計時間を返す。
     * 
     * @param overtime
     * @param userUnderTotal
     * @param userOverTotal
     * @return 最新の累計時間を返す。Key:updUnderTotal,updOverTotal
     */
    public Map<String, Integer> splitUnderOver(int overtime,
    								int userUnderTotal, int userOverTotal) {
    	Integer updUnderTotal = 0;
    	Integer updOverTotal = 0;
    	//日中
    	if (userUnderTotal > 3600) {
    		//既存のunderTotalが60時間を超えていたら
    		updUnderTotal = 3600;
    		updOverTotal = userOverTotal + overtime;
    	} else {
    		//既存のunderTotalが60時間以下だったら
    		updUnderTotal = userUnderTotal + overtime;
    		//プラスした結果60時間を超えた場合
    		if (updUnderTotal > 3600) {
    			updUnderTotal = 3600;
    			updOverTotal = (userUnderTotal + overtime) - 3600;
    		}
    	}
    	
    	//60時間未満、60時間超えに振り分けたをMapにセット
    	Map<String, Integer> splitUnderOverMap = new HashMap<>();
    	splitUnderOverMap.put("updUnderTotal", updUnderTotal);
    	splitUnderOverMap.put("updOverTotal", updOverTotal);
    	
    	return splitUnderOverMap;
    }

    /**
     * 休憩時間を早朝深夜と日中に分ける
     * 休日残業時間を早朝深夜と日中に分ける
     * 
     * @param startTime
     * @param endTime
     * @return 早朝深夜と日中に分けられた時間Map Key:"dt","emn"
     */
    public Map<String, Integer> splitTimeDtEmn(LocalTime startTime, LocalTime endTime) {
    	
    	Duration duraTimeDt = Duration.ZERO;	//日中の休憩時間
		Duration duraTimeEmn = Duration.ZERO;	//早朝深夜の休憩時間
    	
    	//開始時間が5:30よりも前
    	if (startTime.isBefore(LocalTime.of(5, 30))) {
    		//終了時間が5:30よりも前
    		if (endTime.isBefore(LocalTime.of(5, 30))) {
    			//早朝深夜（開始時間〜終了時間）
    			duraTimeEmn = Duration.between(startTime, endTime);
    		} else {
    			//早朝深夜（開始時間〜5:30）
    			duraTimeEmn = Duration.between(startTime, LocalTime.of(5, 30));
    			//日中（5:30〜終了時間）
    			duraTimeDt = Duration.between(LocalTime.of(5, 30), endTime);
    		}
    		
    	} else if (endTime.isBefore(LocalTime.of(22, 0))){
    	//開始時間が5:30以降で、22:00よりも前
    		//終了時間が22:00よりも前
    		if (endTime.isBefore(LocalTime.of(22, 0))) {
    			//日中（開始時間〜終了時間）
    			duraTimeDt = Duration.between(startTime, endTime);
    		} else {
    			//日中（開始時間〜22:00）
    			duraTimeDt = Duration.between(startTime, LocalTime.of(22, 0));
    			//早朝深夜（22:00〜終了時間）
    			duraTimeEmn = Duration.between(LocalTime.of(22, 0), endTime);
    		}
    		
    	} else {
    	//開始時間が22時よりも後
    		//早朝深夜（開始時間〜終了時間）
    		duraTimeEmn = Duration.between(startTime, endTime);
    	}
    	
    	//int型に変換
    	//日中
    	long longTimeDt = duraTimeDt.getSeconds();	//秒
    	int timeDt = (int)longTimeDt / 60;	//分
    	//早朝深夜
    	long longTimeEmn = duraTimeEmn.getSeconds();	//秒
    	int timeEmn = (int)longTimeEmn / 60;	//分
    	
    	//日中、早朝深夜に振り分けた値をMapにセット
    	Map<String, Integer> splitTimeDtEmnMap = new HashMap<>();
    	splitTimeDtEmnMap.put("dt", timeDt);
    	splitTimeDtEmnMap.put("emn", timeEmn);
    	
    	return splitTimeDtEmnMap;
    }
    
}
