package mx.unam.ciencias.fbd.domain;

import mx.unam.ciencias.fbd.util.Validate;

import java.util.UUID;

/**
 * Vehículo de la SSP.
 */
public class Vehicle {
    /**
     * Las placas del vehículo
     */
    private String id;
    /**
     * El tipo de vehículo
     */
    private Type type;
    /**
     * El modelo del vehículo
     */
    private String model;
    /**
     * La zona a la que pertence el vehículo
     */
    private String zone = ""; // TODO implement Zone
    /**
     * La descripción del vehículo
     */
    private String description;
    /**
     * El id del conductor del vehículo
     */
    private UUID driverId;

    /**
     * Crea un nuevo vehículo con las caracterísitcas dadas, ninguno de los parámetros debe ser null.
     *
     * @param id          la matrícula del vehículo.
     * @param type        el tipo de vehículo.
     * @param model       el modelo del vehículo.
     * @param description la descripción del vehículo.
     */
    public Vehicle(String id, Type type, String model, String description) {
        Validate.notNull(type);
        Validate.notEmpty(id, model, description);
        this.id = id;
        this.type = type;
        this.model = model;
        this.description = description;
    }

    /**
     * @return el tipo de vehículo.
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    /**
     * @return el modelo del vehículo.
     */
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        Validate.notEmpty(model);
        this.model = model;
    }

    /**
     * @return la zona del vehículo.
     */
    public String getZone() {
        return zone;
    }

    /**
     * Cambia la zona del vehículo a un nuevo valor.
     *
     * @param zone el nuevo valor, no debe ser null.
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * @return la descripción del vehículo.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Cambia la descripción del vehículo a un nuevo valor.
     *
     * @param description el nuevo valor, no debe ser null.
     */
    public void setDescription(String description) {
        Validate.notEmpty(description);
        this.description = description;
    }

    /**
     * @return el identificador de la persona que maneja el vehículo.
     */
    public UUID getDriverId() {
        return driverId;
    }

    /**
     * Cambia el identificador de la persona que maneja el vehículo a un nuevo valor.
     *
     * @param driverId el nuevo valor, no debe ser null.
     */
    public void setDriverId(UUID driverId) {
        Validate.notNull(driverId);
        this.driverId = driverId;
    }

    /**
     * @return el identificador del vehículo.
     */
    public String getId() {
        return id;
    }

    /**
     * Cambia el identificador del vehículo a un nuevo valor.
     *
     * @param id el nuevo valor, no debe ser null.
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", model='" + model + '\'' +
                ", zone='" + zone + '\'' +
                ", description='" + description + '\'' +
                ", driverId=" + driverId +
                '}';
    }

    /**
     * Vehicle types.
     */
    public enum Type {
        HELICOPTER,
        CAR,
        TRUCK,
        MOTORCYCLE
    }
}
