package kendal.handlers;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.AstHelper;
import kendal.api.KendalHandler;

import java.util.Collection;

public abstract class TypescriptFieldsHandler<T> implements KendalHandler<T> {

    @Override
    public void handle(Collection annotatedNodes, AstHelper helper) {
        // TODO implement me, please!!!
    }

    public static class PrivateHandler extends TypescriptFieldsHandler<Private> {

        @Override
        public Class getHandledAnnotationType() {
            return Private.class;
        }
    }

    public static class ProtectedHandler extends TypescriptFieldsHandler<Protected> {

        @Override
        public Class getHandledAnnotationType() {
            return Protected.class;
        }
    }

    public static class PackagePrivateHandler extends TypescriptFieldsHandler<PackagePrivate> {

        @Override
        public Class getHandledAnnotationType() {
            return PackagePrivate.class;
        }
    }

    public static class PublicHandler extends TypescriptFieldsHandler<Public> {

        @Override
        public Class getHandledAnnotationType() {
            return Public.class;
        }
    }


}
