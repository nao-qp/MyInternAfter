package overtime.example.validation.validator;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.ReportForm;
import overtime.example.validation.RestStartBeforeRestEnd;

public class RestStartBeforeRestEndValidator implements ConstraintValidator<RestStartBeforeRestEnd, ReportForm> {

	@Override
    public boolean isValid(ReportForm form, ConstraintValidatorContext context) {
        // restStartTime と restEndTime が両方とも null でない場合にチェック
        if (form.getRestStartTime() != null && form.getRestEndTime() != null) {
            // 休憩開始時間が休憩終了時間より前かチェック(開始00:00終了00:00は初期値)
            return form.getRestStartTime().isBefore(form.getRestEndTime()) ||
            			(form.getRestStartTime().equals(LocalTime.of(0, 0)) && form.getRestEndTime().equals(LocalTime.of(0, 0)));
        }

        return true;
    }
}
