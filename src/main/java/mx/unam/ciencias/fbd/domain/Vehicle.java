package mx.unam.ciencias.fbd.domain;

import mx.unam.ciencias.fbd.util.Validate;

import java.util.UUID;

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

    public Vehicle(String id, Type type, String model, String description) {
        Validate.notNull(type, driverId);
        Validate.notEmpty(id, model, description);
        this.id = id;
        this.type = type;
        this.model = model;
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        Validate.notEmpty(model);
        this.model = model;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Validate.notEmpty(description);
        this.description = description;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        Validate.notNull(driverId);
        this.driverId = driverId;
    }

    public String getId() {
        return id;
    }

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
