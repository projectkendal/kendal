package kendal.api;

import java.util.Collection;

import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;
/**
* Service provider interface (SPI).
* {@link #handle} method of each service provider will be called during annotation processing.
*/
public interface KendalHandler<T> {

    /**
     *
     * @param annotationNodes kendal AST nodes representing annotation of type returned by {@link #getHandledAnnotationType()}
     * @param helper
     * @throws InvalidAnnotationException
     */
    void handle(Collection<Node> annotationNodes, AstHelper helper) throws KendalException;


    // TODO provide default implementation searching for type parameters
    Class<T> getHandledAnnotationType();
}
