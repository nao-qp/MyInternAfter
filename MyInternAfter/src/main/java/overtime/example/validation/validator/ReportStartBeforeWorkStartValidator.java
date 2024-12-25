package overtime.example.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.ReportForm;
import overtime.example.validation.ReportStartBeforeWorkStart;

public class ReportStartBeforeWorkStartValidator implements ConstraintValidator<ReportStartBeforeWorkStart, ReportForm>{

	@Override
    public boolean isValid(ReportForm form, ConstraintValidatorContext context) {
        // startTime と workPatternsStartTime が両方とも null でない場合にチェック
        if (form.getStartTime() != null && form.getWorkPatternsStartTime() != null) {
            // 開始時間が勤務パターンの開始時間より前かチェック
            return form.getStartTime().isBefore(form.getWorkPatternsStartTime()) && 
            				!(form.getStartTime().equals(form.getWorkPatternsStartTime()));
        }

        return true;
    }
}
