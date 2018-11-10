package kendal.api;

import kendal.api.exceptions.InvalidAnnotationException;
import kendal.model.Node;

import java.util.Collection;
/**
* Service provider interface (SPI).
* {@link #handle} method of each service provider will be called during annotation processing.
*
*/
public interface KendalHandler<T> {

    /**
     *
     * @param annotationNodes kendal AST nodes representing annotation of type returned by {@link #getHandledAnnotationType()}
     * @param helper
     * @throws InvalidAnnotationException
     */
    void handle(Collection<Node> annotationNodes, AstHelper helper) throws InvalidAnnotationException;


    // TODO provide default implementation searching for type parameters
    Class<T> getHandledAnnotationType();
}
