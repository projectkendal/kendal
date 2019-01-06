package kendal.api.inheritance;

/**
 * Java does not allow using parameters of annotation to define other parameters of the same annotation.
 * But sometimes we want to, so here we introduce annotation that will be replaced with value of another parameter.
 * It can be used only inside value expression for {@link Attribute}.
 * Some expressions using @AttrReference will cause the compiler to break compilation before annotation processing.
 * In such case @AttrReference will never work. This feature is currently considered a proof of concept showing that
 * it is possible to access other attributes of annotation to define value of attribute.
 */
public @interface AttrReference {

    String value();
}
