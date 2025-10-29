package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.api.mapper.PosDtoMapper;
import de.seuhd.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/pos")
@RequiredArgsConstructor
public class PosController {
    private final PosService posService;
    private final PosDtoMapper posDtoMapper;

    @GetMapping("")
    public ResponseEntity<List<PosDto>> getAll() {
        return ResponseEntity.ok(
                posService.getAll().stream()
                        .map(posDtoMapper::fromDomain)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PosDto> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                posDtoMapper.fromDomain(posService.getById(id))
        );
    }

    @PostMapping("")
    public ResponseEntity<PosDto> create(
            @RequestBody PosDto posDto) {
        return upsert(posDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PosDto> update(
            @PathVariable Long id,
            @RequestBody PosDto posDto) {
        if (!id.equals(posDto.getId())) {
            throw new IllegalArgumentException("POS ID in path and body do not match.");
        }
        return upsert(posDto);
    }

    private ResponseEntity<PosDto> upsert(PosDto posDto) {
        return ResponseEntity.ok(
                posDtoMapper.fromDomain(
                        posService.upsert(
                                posDtoMapper.toDomain(posDto)
                        )
                )
        );
    }
}
