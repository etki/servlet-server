package me.etki.servlet.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark parts of API that should never be used by library users. Library authors reserve the
 * right to refactor method names, type names, method signatures without any notice in any major, minor or patch
 * release.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface InternalApi {
}
