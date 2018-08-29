package mx.unam.ciencias.fbd.service;

import mx.unam.ciencias.fbd.domain.Vehicle;
import mx.unam.ciencias.fbd.repository.VehicleRepository;

public class VehicleService extends AbstractResourceService<Vehicle, String> {
    private static final VehicleService singleton = new VehicleService();

    private VehicleService() {
        super(VehicleRepository.getInstance());
    }

    public static VehicleService getInstance() {
        return singleton;
    }
}
