package kendal.test.positive.clone.inheritingAnnotation;
        import kendal.annotations.Clone;

class TestTransformer implements Clone.Transformer<String, StringBuilder> {

    @Override
    public StringBuilder transform(String input) {
        return new StringBuilder(input);
    }
}
