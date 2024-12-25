package overtime.example.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.ReportForm;
import overtime.example.validation.ReportStartAndEndIsNull;

public class ReportStartAndEndIsNullValidator implements ConstraintValidator<ReportStartAndEndIsNull, ReportForm>{

	@Override
    public boolean isValid(ReportForm form, ConstraintValidatorContext context) {
        // startTime と endTime が両方とも null かどうかチェック
        if (form.getStartTime() == null && form.getEndTime() == null) {
        	return false;
        }

        return true;
    }
}
