package net.lodoma.lime.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationHelper
{
    public static boolean isAnnotationPresent(AnnotatedElement element, Class<? extends Annotation> annotation)
    {
        return element.isAnnotationPresent(annotation);
    }
    
    public static Annotation getAnnotation(AnnotatedElement element, Class<? extends Annotation> annotation)
    {
        return element.getAnnotation(annotation);
    }
}
