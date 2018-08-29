package mx.unam.ciencias.fbd.common;

public enum IdOperation implements IRegex {
    DELETE, // DELETE [ID]
    EDIT,   // EDIT [ID]
    GET;     // GET [ID]

    @Override
    public String regex() {
        return this.name() + "\\s+\"(.+)\"";
    }
}
