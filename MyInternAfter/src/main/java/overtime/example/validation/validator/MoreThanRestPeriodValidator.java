package overtime.example.validation.validator;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.application.service.TimeConverterService;
import overtime.example.form.ReportForm;
import overtime.example.validation.MoreThanRestPeriod;

public class MoreThanRestPeriodValidator implements ConstraintValidator<MoreThanRestPeriod, ReportForm>{

	@Autowired
	private TimeConverterService timeConverterService;
	
	@Override
    public boolean isValid(ReportForm form, ConstraintValidatorContext context) {
        // 入力した開始、終了休憩時間が規定休憩時間以上になっているか
		// 休憩時間未入力だったら00:00を設定
		LocalTime defalutTime = LocalTime.of(0, 0);
		if (form.getRestStartTime() == null) {
			form.setRestStartTime(defalutTime);
		}
		if (form.getRestEndTime() == null) {
			form.setRestEndTime(defalutTime);
		}
		
		// 休憩開始時間 > 休憩終了時間の場合は、規定休憩時間以上になっているかはチェック不要
		// (RestStartBeforeRestEndValidatorでチェック、エラー表示)
		// 休憩開始時間==休憩終了時間の場合は、休憩時間0のためチェック対象とする
		if (form.getRestStartTime() != form.getRestEndTime()
			&& form.getRestStartTime().isAfter(form.getRestEndTime())) {
			return true;
		}
		
		// 入力した休憩時間（分）
		int inputRestPeriod = 
			timeConverterService.toMinutesGetTimeDifference(form.getRestStartTime(), form.getRestEndTime());
		// 規定休憩時間（分）
		int minRestPeriod = form.getRestPeriod();	// RestPeriod 分
		// 休憩時間が規定休憩時間以上ならOK
		if (inputRestPeriod >= minRestPeriod) {
			return true;
		} else {
			return false;
		}
    }
	
}
