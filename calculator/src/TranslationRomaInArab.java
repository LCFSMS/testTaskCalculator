public enum TranslationRomaInArab {
    I(1), V(5), X(10);

    private int translation;

    TranslationRomaInArab(int translation){
        this.translation = translation;
    }

    public int getTranslation() {
        return translation;
    }
}
