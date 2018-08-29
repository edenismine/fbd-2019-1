package mx.unam.ciencias.fbd.common;

import mx.unam.ciencias.fbd.App;

public enum BasicOperation implements IRegex {
    USE(String.format("\\s+(%s)", App.TABLES)), // USE [Table]
    EXIT(""); // EXIT

    String argument;

    BasicOperation(String argument) {
        this.argument = argument;
    }

    @Override
    public String regex() {
        return this.name() + this.argument;
    }
}