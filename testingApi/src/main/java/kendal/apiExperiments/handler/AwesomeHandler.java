package kendal.apiExperiments.handler;

import java.util.Collection;

import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.model.Node;

public class AwesomeHandler implements KendalHandler<Awesome> {

    @Override
    public void handle(Collection<Node> handledNodes, AstHelper helper) {
        // some logic
    }
}
