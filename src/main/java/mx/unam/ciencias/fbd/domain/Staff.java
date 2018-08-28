package mx.unam.ciencias.fbd.domain;

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
    private final UUID id;
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
    private Staff supervisor;

    /**
     * Construye un nuevo elemento.
     *
     * @param name el nombre del elemento.
     * @param sex  el sexo del elemento.
     * @param dob  la fecha de nacimiento del elemento.
     * @param doh  la fecha de contratación del elemento.
     * @param role el puesto del elemento.
     */
    public Staff(String name, Sex sex, LocalDate dob, LocalDate doh, Role role) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.sex = sex;
        this.dob = dob;
        this.doh = doh;
        this.role = role;
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
     * @return la lista de subordinados directos del elemento.
     */
    public List<Staff> getSubordinates() {
        // TODO
        return null;
    }

    /**
     * @return la matrícula del elemento.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return el nombre del elemento.
     */
    public String getName() {
        return name;
    }

    /* ACCESS AND MODIFICATION METHODS */

    /**
     * Cambia el nombre del elemento al valor proporcionado.
     *
     * @param name el nuevo valor.
     */
    public void setName(String name) {
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
     * @param sex el nuevo valor.
     */
    public void setSex(Sex sex) {
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
     * @param dob el nuevo valor.
     */
    public void setDob(LocalDate dob) {
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
     * @param doh el nuevo valor.
     */
    public void setDoh(LocalDate doh) {
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
     * @param role el nuevo valor.
     */
    public void setRole(Role role) {
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
        OFFICE,
        LIUTENANT
    }
}
