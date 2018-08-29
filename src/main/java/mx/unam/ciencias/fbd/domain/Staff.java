package mx.unam.ciencias.fbd.domain;

import mx.unam.ciencias.fbd.util.Validate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Elemento de la SSP.
 */
public class Staff {
    /**
     * Matrícula del elemento.
     */
    private UUID id;
    /**
     * Nombre del elemento.
     */
    private String name;
    /**
     * Sexo del elemento.
     */
    private Sex sex;
    /**
     * Fecha de nacimiento del elemento.
     */
    private LocalDate dob;
    /**
     * Fecha de contratación del elemento.
     */
    private LocalDate doh;
    /**
     * Puesto del elemento.
     */
    private Role role;
    /**
     * Supervisor del elemento.
     */
    private UUID supervisorID;

    /**
     * Construye un nuevo elemento. Ningún valor debe ser nulo.
     *
     * @param name el nombre del elemento.
     * @param sex  el sexo del elemento.
     * @param dob  la fecha de nacimiento del elemento.
     * @param doh  la fecha de contratación del elemento.
     * @param role el puesto del elemento.
     */
    public Staff(String name, Sex sex, LocalDate dob, LocalDate doh, Role role) {
        Validate.notEmpty(name);
        Validate.notNull(sex, dob, doh, role);
        this.id = UUID.randomUUID();
        this.name = name;
        this.sex = sex;
        this.dob = dob;
        this.doh = doh;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", dob=" + dob +
                ", doh=" + doh +
                ", role=" + role +
                ", supervisorID=" + supervisorID +
                '}';
    }

    /**
     * @return la edad en años del elemento.
     */
    public int getAge() {
        return this.dob.until(LocalDate.now()).getYears();
    }

    /**
     * @return la antigüedad en años del elemento.
     */
    public int getSeniority() {
        return this.doh.until(LocalDate.now()).getYears();
    }

    /**
     * @return la matrícula del elemento que supervisa a este elemento.
     */
    public UUID getSupervisorID() {
        return supervisorID;
    }

    /**
     * Cambia la matrícula del elemento que supervisa a este elemento.
     *
     * @param supervisorID el nuevo valor. No debe ser null.
     */
    public void setSupervisorID(UUID supervisorID) {
        this.supervisorID = supervisorID;
    }

    /**
     * @return la matrícula del elemento.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Cambia la matrícula del elemento.
     *
     * @param id el nuevo valor. No debe ser null.
     */
    public void setId(UUID id) {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * @return el nombre del elemento.
     */
    public String getName() {
        return name;
    }

    /**
     * Cambia el nombre del elemento al valor proporcionado.
     *
     * @param name el nuevo valor. No debe ser null ni vacío.
     */
    public void setName(String name) {
        Validate.notEmpty(name);
        this.name = name;
    }

    /**
     * @return el sexo del elemento.
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Cambia el sexo del elemento al valor proporcionado.
     *
     * @param sex el nuevo valor. No debe ser null.
     */
    public void setSex(Sex sex) {
        Validate.notNull(sex);
        this.sex = sex;
    }

    /**
     * @return la fecha de nacimiento del elemento.
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Cambia la fecha de nacimiento del elemento al valor proporcionado.
     *
     * @param dob el nuevo valor. No debe ser null.
     */
    public void setDob(LocalDate dob) {
        Validate.notNull(dob);
        this.dob = dob;
    }

    /**
     * @return la fecha de contratación del elemento.
     */
    public LocalDate getDoh() {
        return doh;
    }

    /**
     * Cambia la fecha de contratación del elemento al valor proporcionado.
     *
     * @param doh el nuevo valor. No debe ser null.
     */
    public void setDoh(LocalDate doh) {
        Validate.notNull(doh);
        this.doh = doh;
    }

    /**
     * @return el puesto del elemento.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Cambia el puesto del elemento al valor proporcionado.
     *
     * @param role el nuevo valor. No debe ser null.
     */
    public void setRole(Role role) {
        Validate.notNull(role);
        this.role = role;
    }

    /**
     * Sexos del personal
     */
    public enum Sex {
        FEMALE,
        MALE
    }

    /**
     * Roles que puede desempeñar el personal.
     */
    public enum Role {
        POLICEMAN,
        OFFICER,
        LIEUTENANT
    }
}
