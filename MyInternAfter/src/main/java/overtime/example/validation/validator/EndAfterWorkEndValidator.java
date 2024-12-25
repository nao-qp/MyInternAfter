package overtime.example.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.RequestForm;
import overtime.example.validation.EndAfterWorkEnd;

public class EndAfterWorkEndValidator implements ConstraintValidator<EndAfterWorkEnd, RequestForm> {

	@Override
    public boolean isValid(RequestForm form, ConstraintValidatorContext context) {
        // endTime と workPatternsEndTime が両方とも null でない場合にチェック
        if (form.getEndTime() != null && form.getWorkPatternsEndTime() != null) {
            // 終了時間が勤務パターンの終了時間より後かチェック
            return form.getEndTime().isAfter(form.getWorkPatternsEndTime()) &&
            		!(form.getEndTime().equals(form.getWorkPatternsEndTime()));
        }

        return true;
    }
}
