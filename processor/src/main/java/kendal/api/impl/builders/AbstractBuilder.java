package kendal.api.impl.builders;

import com.sun.tools.javac.tree.TreeMaker;

import kendal.api.AstUtils;

abstract class AbstractBuilder {
    final AstUtils astUtils;
    final TreeMaker treeMaker;

    AbstractBuilder(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

}
