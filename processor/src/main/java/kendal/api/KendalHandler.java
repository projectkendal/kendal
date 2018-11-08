package kendal.api;

import kendal.model.Node;

import java.util.Collection;
/*
* Service provider interface (SPI).
* {@link #handle} method of each service provider will be called during annotation processing.
*
* */
public interface KendalHandler<T> {


    void handle(Collection<Node> annotatedNodes, AstHelper helper);

    Class<T> getHandledAnnotationType();
}
