package overtime.example.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import overtime.example.form.RequestForm;
import overtime.example.validation.StartAndEndIsNull;

public class StartAndEndIsNullValidator implements ConstraintValidator<StartAndEndIsNull, RequestForm>{

	@Override
    public boolean isValid(RequestForm form, ConstraintValidatorContext context) {
        // startTime と endTime が両方とも null かどうかチェック
        if (form.getStartTime() == null && form.getEndTime() == null) {
        	return false;
        }

        return true;
    }
}
