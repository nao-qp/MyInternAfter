package overtime.example.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import overtime.example.validation.validator.StartAndEndIsNullValidator;

@Target({ ElementType.TYPE })  // クラスレベルに適用
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAndEndIsNullValidator.class)  // バリデーターとしてStartAndEndIsNullValidatorを指定
public @interface StartAndEndIsNull {
	String message() default "";  // デフォルトメッセージ

    Class<?>[] groups() default {};  // グループ

    Class<? extends Payload>[] payload() default {};  // ペイロード
}
