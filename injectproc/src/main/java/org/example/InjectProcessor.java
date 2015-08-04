package org.example;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

@SupportedAnnotationTypes({
    InjectProcessor.AT_INJECT,
    InjectProcessor.AT_AUTOWIRED
})
public class InjectProcessor extends AbstractProcessor {

  static final String AT_INJECT = "javax.inject.Inject";
  static final String AT_AUTOWIRED = "org.springframework.beans.factory.annotation.Autowired";

  private Messager messager;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    try {
      messager = processingEnv.getMessager();
      for (TypeElement annotation : annotations) {
        for (Element elem : env.getElementsAnnotatedWith(annotation)) {
          checkInjections(elem, AT_AUTOWIRED);
        }
      }
    }
    catch (Exception e) {
      messager.printMessage(ERROR, "InjectionProcessor encountered an error: '" + e.getMessage() + "'.");
    }
    return false;
  }

  private void checkInjections(Element elem, String expectedAnnotation) {
    for (AnnotationMirror mirror : elem.getAnnotationMirrors()) {
      String actualAnnotation = getTypePathOfAnnotation(mirror);
      if (!expectedAnnotation.equals(actualAnnotation)) {
        String errMessage = "Wrong annotation type " + actualAnnotation + ", use " + expectedAnnotation + " instead.";
        messager.printMessage(ERROR, errMessage, elem, mirror);
        return;
      }
    }
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  private String getTypePathOfAnnotation(AnnotationMirror mirror) {
    return mirror.getAnnotationType().toString();
  }
}
