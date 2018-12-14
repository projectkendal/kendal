package kendal.api;

import java.lang.annotation.Annotation;
import java.util.Collection;

import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;
/**
* Service provider interface (SPI).
* {@link #handle} method of each service provider will be called during annotation processing.
*/
public interface KendalHandler<T extends Annotation> {

    /**
     *
     * @param annotationNodes kendal AST nodes representing annotation of type returned by {@link #getHandledAnnotationType()}
     * @param helper
     * @throws InvalidAnnotationException
     */
    void handle(Collection<Node> annotationNodes, AstHelper helper) throws KendalException;

    // TODO provide default implementation searching for type parameters https://trello.com/c/RSuRWZNp/42-provide-default-implementation-searching-for-type-parameters-in-kendalhandlerjava
    /**
     * Returns annotation that given handler supports. If returns null, it means this handler's functionality is not
     * based on any annotation and it should get all AST trees roots.
     */
    Class<T> getHandledAnnotationType();
}
