package kendal.api;

import com.sun.tools.javac.util.Name;

import kendal.api.impl.AstValidatorImpl;
import kendal.model.Node;

/*
*  Interface for AST modification helper class
*  Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
*
* */
public interface AstHelper {
    // MODIFICATION METHODS
    void addVariableDeclarationToClass(Node clazz, Node variableDeclaration);

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidatorImpl getAstValidator();

    // UTILS
    Name nameFromString(String name);
}
