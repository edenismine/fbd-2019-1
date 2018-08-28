package mx.unam.ciencias.fbd.service;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.repository.StaffRepository;

import java.util.UUID;
import java.util.stream.Stream;

public class StaffService extends AbstractResourceService<Staff, UUID> {
    private static final StaffService singleton = new StaffService();

    private StaffService() {
        super(StaffRepository.getInstance());
    }

    public static StaffService getInstance() {
        return singleton;
    }

    public Stream<Staff> getSubordinates(Staff staff) {
        return findAll().filter(s -> staff.getId().equals(s.getSupervisorID()));
    }
}
