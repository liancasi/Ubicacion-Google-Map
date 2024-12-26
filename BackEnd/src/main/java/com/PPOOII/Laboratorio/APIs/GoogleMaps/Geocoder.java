package com.PPOOII.Laboratorio.APIs.GoogleMaps;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.PPOOII.Laboratorio.Entities.Ubicacion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Geocoder {

    // URL del servicio de geocodificación de Google Maps
    private static final String GEOCODING_RESOURCE = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=all&inputtype=textquery&key=";
    private static final String API_KEY = "AIzaSyBomnY9UemevV5IVEXtYHP6SWw3kRsERGc"; // Tu clave de API de Google

    public Geocoder() {
        // Constructor
    }

    // Método para realizar la geocodificación de manera síncrona
    public String GeocodeSync(String query) throws IOException, InterruptedException {
        // Crear un cliente HTTP
        HttpClient httpClient = HttpClient.newHttpClient();
        
        // Codificar la consulta para la URL
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        
        // Construir la URI de la solicitud
        String requestUri = GEOCODING_RESOURCE + API_KEY + "&input=" + encodedQuery;
        
        // Crear la solicitud HTTP GET
        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000))
                .build();
        
        // Enviar la solicitud y obtener la respuesta
        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        
        // Devolver el cuerpo de la respuesta como String
        return geocodingResponse.body();
    }

    // Método para obtener las coordenadas de latitud y longitud a partir de una consulta
    public String getLatLng(String query) throws IOException, InterruptedException {
        // Crear un objeto ObjectMapper para procesar la respuesta JSON
        ObjectMapper mapper = new ObjectMapper();
        
        // Realizar la geocodificación
        String response = this.GeocodeSync(query);
        
        // Parsear la respuesta JSON
        JsonNode responseJsonNode = mapper.readTree(response);
        JsonNode items = responseJsonNode.get("candidates");
        
        // Verificar si hay candidatos
        if (items != null && items.isArray() && items.size() > 0) {
            // Obtener latitud y longitud del primer candidato
            String latitud = items.get(0).get("geometry").get("location").get("lat").asText(); 
            String longitud = items.get(0).get("geometry").get("location").get("lng").asText();
            
            // Devolver las coordenadas en formato "latitud,longitud"
            return latitud + "," + longitud;
        } else {
            throw new IOException("No se encontraron coordenadas para la consulta: " + query);
        }
    }

    // Método adicional para obtener coordenadas de una Ubicacion
    public String getLatLngFromUbicacion(Ubicacion ubicacion) throws IOException, InterruptedException {
        String direccionCompleta = ubicacion.getDireccionCompleta(); // "parque la concordia, mariquita, tolima"
        return getLatLng(direccionCompleta);
    }
}

