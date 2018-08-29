package mx.unam.ciencias.fbd.common;

public enum TableOperation implements IRegex {
    LIST,   // LIST
    NEW;    // NEW

    @Override
    public String regex() {
        return this.name();
    }
}
