package kendal.handlers;

import java.util.Collection;

import kendal.annotations.Clone;
import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.model.Node;

public class CloneHandler implements KendalHandler<Clone> {
    @Override
    public void handle(Collection<Node> annotationNodes, AstHelper helper) {
        System.out.println("Here we are!");
    }

    @Override
    public Class<Clone> getHandledAnnotationType() {
        return Clone.class;
    }
}
