package mx.unam.ciencias.fbd.service;

import mx.unam.ciencias.fbd.repository.CrudRepository;
import mx.unam.ciencias.fbd.util.Validate;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractResourceService<S, ID> implements CrudRepository<S, ID> {

    private final CrudRepository<S, ID> repository;

    AbstractResourceService(CrudRepository<S, ID> repository) {
        Validate.notNull(repository);
        this.repository = repository;
    }

    @Override
    public S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<S> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public Stream<S> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean deleteById(ID id) {
        return repository.deleteById(id);
    }
}
