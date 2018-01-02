package uy.com.agm.gaston.modelo.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Size(max = 9)
@Pattern(regexp = "[0-9]*")
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
// Para que no aparezca el mensaje de validaci�n por defecto
@ReportAsSingleViolation
public @interface Telefono {
	String message() default "{javax.validation.constraints.Telefono.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}