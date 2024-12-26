package com.PPOOII.Laboratorio.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PPOOII.Laboratorio.Entities.Coordenadas;
import com.PPOOII.Laboratorio.Services.Interfaces.ICoordenadasService;

@RestController
@RequestMapping("/Laboratorio1")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Coordenadas", description = "API para gestionar coordenadas")
public class CoordenadasController {

    private final ICoordenadasService coordenadaService;

    @Autowired
    public CoordenadasController(ICoordenadasService coordenadaService) {
        this.coordenadaService = coordenadaService;
    }

    @Operation(summary = "Obtener todas las coordenadas", description = "Retorna una lista paginada de todas las coordenadas")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(schema = @Schema(implementation = Coordenadas.class)))
    @GetMapping("/coordenadas")
    public ResponseEntity<List<Coordenadas>> consultarAllCoordenadas(
            @Parameter(description = "Información de paginación") Pageable pageable) {
        List<Coordenadas> coordenadas = coordenadaService.consultarAllCoordenadas(pageable);
        return ResponseEntity.ok(coordenadas);
    }
    
    @Operation(summary = "Obtener coordenadas por IDs", description = "Retorna una lista de coordenadas basada en los IDs proporcionados")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(schema = @Schema(implementation = Coordenadas.class)))
    @GetMapping("/coordenadas/lista")
    public ResponseEntity<List<Coordenadas>> obtenerCoordenadasPorIds(
            @Parameter(description = "Lista de IDs de coordenadas") @RequestParam List<Integer> ids) {
        List<Coordenadas> coordenadas = coordenadaService.obtenerCoordenadasPorIds(ids);
        return ResponseEntity.ok(coordenadas);
    }
    
    @Operation(summary = "Obtener coordenadas por ID", description = "Retorna las coordenadas para un ID específico")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                 content = @Content(schema = @Schema(implementation = Coordenadas.class)))
    @ApiResponse(responseCode = "404", description = "Coordenadas no encontradas")
    @GetMapping("/coordenadas/{id}")
    public ResponseEntity<Coordenadas> obtenerCoordenadasPorId(
            @Parameter(description = "ID de las coordenadas") @PathVariable Integer id) {
        Optional<Coordenadas> coordenada = coordenadaService.obtenerCoordenadasPorId(id);
        
        return coordenada.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}