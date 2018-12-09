package kendal.test.positive.tsfields.general.fieldGenerationInSubclass;

import java.util.List;

@SuppressWarnings("unused")
public class SubClass extends ClassWithFieldsGenerated {

    SubClass(int superField1, List<Integer> superField2, int superField3, List<Integer> superField4/*,
            @Public int publicField, @Protected List protectedField,
            @PackagePrivate(makeFinal = false) int packagePrivateField, @Private(makeFinal = false) List privateField*/) {
        super(superField1, superField2, superField3, superField4);
    }

    /* todo: fix adding field in subclasses
       todo: task - (https://trello.com/c/yP1LaqL5/36-tsfields-bug-fields-generation-in-subclasses-does-not-work)
    private int shouldAccessGeneratedField() {
        return publicField + protectedField.size() + packagePrivateField + privateField.size();
    }*/
}
