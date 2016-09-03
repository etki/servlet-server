package me.etki.servlet.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Should be put in front of API that is not stable and is subject to change dramatically
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
public @interface Experimental {
}
