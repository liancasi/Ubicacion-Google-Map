package com.PPOOII.Laboratorio.Controller;

/*import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PPOOII.Laboratorio.Services.UbicacionServiceImpl;

import java.io.IOException;
import java.lang.InterruptedException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/Laboratorio1")
//@Tag(name = "Ubicación", description = "API para gestionar ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionServiceImpl ubicacionService;
/*
    @Operation(summary = "Obtener coordenadas por ID", description = "Retorna las coordenadas para una ubicación específica")
    @ApiResponse(responseCode = "200", description = "Operación exitosa")
    @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/coordenadas/{id}")
    public ResponseEntity<String> obtenerCoordenadas(
            @Parameter(description = "ID de la ubicación") @PathVariable int id) {
        try {
            String coordenadas = ubicacionService.obtenerCoordenadas(id);
            return ResponseEntity.ok(coordenadas);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener coordenadas: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }*/
}