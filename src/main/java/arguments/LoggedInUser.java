package arguments;

import ninja.params.WithArgumentExtractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jgeerts on 10-8-15.
 */
@WithArgumentExtractor(LoggedInUserExtractor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface LoggedInUser {}