package mx.unam.ciencias.fbd.service;

import mx.unam.ciencias.fbd.domain.Weapon;
import mx.unam.ciencias.fbd.repository.WeaponRepository;

import java.util.UUID;

public class WeaponService extends AbstractResourceService<Weapon, UUID> {
    private static WeaponService singleton = new WeaponService();

    private WeaponService() {
        super(WeaponRepository.getInstance());
    }

    public static WeaponService getInstance() {
        return singleton;
    }
}
