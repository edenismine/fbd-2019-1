package mx.unam.ciencias.fbd.domain;

import mx.unam.ciencias.fbd.util.Validate;

import java.util.UUID;

/**
 * Arma de la SSP.
 */
public class Weapon {
    /**
     * Identificador de arma.
     */
    private UUID id;
    /**
     * Tipo de arma
     */
    private Type type;
    /**
     * Características específicas del arma.
     */
    private String description;
    /**
     * Matrícula del personal de la secretaría de seguridad que tiene asignada el arma.
     */
    private UUID userId;

    /**
     * Devuelve un arma con un nuevo identificador y los parámetros proporcionados.
     *
     * @param type        el tipo de arma.
     * @param description la descripción del arma.
     * @param userId      la matrícula del usuario del arma.
     */
    public Weapon(Type type, String description, UUID userId) {
        Validate.notNull(type, userId);
        Validate.notEmpty(description);
        this.id = UUID.randomUUID();
        this.type = type;
        this.description = description;
        this.userId = userId;
    }

    /**
     * @return el identificador del arma.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Cambia el identificador del arma.
     *
     * @param id el nuevo valor, no debe ser null.
     */
    public void setId(UUID id) {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * @return el tipo de arma.
     */
    public Type getType() {
        return type;
    }

    /**
     * Cambia el tipo del arma.
     *
     * @param type el nuevo valor, no debe ser null.
     */
    public void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    /**
     * @return las cracterísticas específicas del arma.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Cambia la descripción del arma.
     *
     * @param description el nuevo valor, no debe ser null ni vacía.
     */
    public void setDescription(String description) {
        Validate.notEmpty(description);
        this.description = description;
    }

    /**
     * @return el identificador del elemento.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Cambia el identificador del usuario del arma.
     *
     * @param userId el nuevo valor, no debe ser null.
     */
    public void setUserId(UUID userId) {
        Validate.notNull(userId);
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }

    /**
     * Tipos de armas.
     */
    public enum Type {
        PISTOL, MACHINE_GUN, GRENADE
    }
}
