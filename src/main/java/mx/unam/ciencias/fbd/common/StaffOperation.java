package mx.unam.ciencias.fbd.common;

public enum StaffOperation implements IRegex {
    SUBORDINATES,   // SUBORDINATES [ID]
    SENIORITY,      // SENIORITY [ID]
    AGE;            // AGE [ID]

    @Override
    public String regex() {
        return this.name() + "\\s+\"(.+)\"";
    }
}
