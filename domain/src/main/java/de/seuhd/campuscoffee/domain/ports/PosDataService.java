package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.exceptions.PosNotFoundException;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the POS data service that the data layer provides as a port.
 */
public interface PosDataService {
    void clear();
    @NonNull
    List<Pos> getAll();
    Pos getById(@NonNull Long id) throws PosNotFoundException;
    @NonNull
    Pos upsert(@NonNull Pos pos) throws PosNotFoundException;
}
