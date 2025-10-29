package de.seuhd.campuscoffee.domain.ports;


import de.seuhd.campuscoffee.domain.exceptions.PosNotFoundException;
import de.seuhd.campuscoffee.domain.model.Pos;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the POS service that the domain layer provides as a port.
 */
public interface PosService {
    void clear();
    @NonNull
    List<Pos> getAll();
    @NonNull
    Pos getById(@NonNull Long id) throws PosNotFoundException;
    @NonNull
    Pos upsert(@NonNull Pos pos) throws PosNotFoundException;
}
