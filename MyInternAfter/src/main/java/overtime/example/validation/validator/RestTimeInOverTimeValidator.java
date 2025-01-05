package overtime.example.validation.validator;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.ReportForm;
import overtime.example.validation.RestTimeInOverTime;

public class RestTimeInOverTimeValidator implements ConstraintValidator<RestTimeInOverTime, ReportForm>{

	@Override
    public boolean isValid(ReportForm form, ConstraintValidatorContext context) {
		// 入力した休憩時間が残業時間内にあるか

		// 休憩開始終了時間が00:00の場合はチェックしない
		// form.getRestStartTime(),form.getRestEndTime()は未入力の場合、00:00がセットされている
		if (form.getRestStartTime().equals(LocalTime.of(0, 0)) && form.getRestEndTime().equals(LocalTime.of(0, 0))) {
			return true;
		}
		
		// form.getStartTime()とform.getEndTime()は未入力の場合、nullがセットされている。
		// nullの場合、WorkPatternsStartTime,WorkPatternsEndTimeを設定する
		// 比較用変数
		LocalTime StartTime;
		LocalTime EndTime;
		// 開始
		if (form.getStartTime() == null) {
			StartTime = form.getWorkPatternsStartTime();
		} else {
			StartTime = form.getStartTime();
		}
		//　終了
		if (form.getEndTime() == null) {
			EndTime = form.getWorkPatternsEndTime();
		} else {
			EndTime = form.getEndTime();
		}
		
		// 休憩開始時間が残業時間内かどうか
		// 前残業内かどうか
		if (form.getRestStartTime().isAfter(StartTime) 
				&& form.getRestEndTime().isBefore(form.getWorkPatternsStartTime())) {
			return true;
		}
		// 後残業内かどうか
		if (form.getRestStartTime().isAfter(form.getWorkPatternsEndTime())
				&& form.getRestEndTime().isBefore(EndTime)){	
			return true;
		}

		return false;
	}
}
