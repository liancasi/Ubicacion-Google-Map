import React, { useEffect, useState } from 'react';
import { GoogleMap, LoadScript, Marker, InfoWindow } from '@react-google-maps/api';
import CoordenadasService from '../services/CoordenadasService';
import { Link } from 'react-router-dom'; // Asegúrate de importar Link

const MapComponent = () => {
  const [markers, setMarkers] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [isHovered, setIsHovered] = useState({ map: false }); // Para manejar el estado del hover

  const fetchCoordenadas = async () => {
    try {
      const coordenadas = await CoordenadasService.getCoordenadas();
      console.log('Coordenadas recibidas:', coordenadas); 

      if (!coordenadas || coordenadas.length === 0) {
        console.error('No se recibieron coordenadas válidas.');
        return;
      }

      const markersData = coordenadas.map(coord => ({
        id: coord.id,
        marca: coord.marca,
        lat: parseFloat(coord.longitud), // Corrige esto
        lng: parseFloat(coord.latitud),   // Corrige esto
      }));

      console.log('Marcadores actuales:', markersData);
      setMarkers(markersData);
    } catch (error) {
      console.error('Error fetching coordinates:', error);
    }
  };

  useEffect(() => {
    fetchCoordenadas();
  }, []);

  const handleMarkerClick = (marker) => {
    setSelectedMarker(marker);
  };

  const handleCloseInfoWindow = () => {
    setSelectedMarker(null);
  };

  const center = markers.length > 0 ? { lat: markers[0].lat, lng: markers[0].lng } : { lat: 0, lng: 0 };

  return (
    <div 
      style={{
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        justifyContent: 'center', 
        height: '100vh', 
        background: 'linear-gradient(to bottom, #6a11cb, #2575fc)' // Fondo degradado
      }}
    >
       <div className="container">
       <div className="row justify-content-center mt-4">
       <div className="col-md-8 bg-light rounded-lg shadow-lg p-4">
       <h1 className="card-title text-center mb-4 text-primary">Mapa Grupal de las Personas Registrada</h1>
       <LoadScript googleMapsApiKey="AIzaSyBomnY9UemevV5IVEXtYHP6SWw3kRsERGc">
        <GoogleMap
          mapContainerStyle={{ height: '400px', width: '800px', borderRadius: '10px' }} // Estilos del mapa
          center={center}
          zoom={6}
        >
          {markers.map(marker => (
            <Marker 
              key={marker.id} 
              position={{ lat: marker.lat, lng: marker.lng }} 
              title={marker.marca} 
              onClick={() => handleMarkerClick(marker)} 
            />
          ))}

          {selectedMarker && (
            <InfoWindow
              position={{ lat: selectedMarker.lat, lng: selectedMarker.lng }}
              onCloseClick={handleCloseInfoWindow}
            >
              <div>
                <h1>{selectedMarker.marca}</h1>
                <p>ID: {selectedMarker.id}</p>
                <p>Latitud: {selectedMarker.lat}</p>
                <p>Longitud: {selectedMarker.lng}</p>
              </div>
            </InfoWindow>
          )}
        </GoogleMap>
      </LoadScript>
       </div>
        </div>
              
       </div>
      

      {/* Botón para volver al Home */}
      <div className="d-flex justify-content-center mt-4">
        <Link 
          to="/home" 
          className="btn btn-info" 
          onMouseEnter={() => setIsHovered(prev => ({ ...prev, map: true }))} 
          onMouseLeave={() => setIsHovered(prev => ({ ...prev, map: false }))} 
          style={{ 
              transition: 'background-color 0.3s', 
              backgroundColor: isHovered.map ? '#138496' : '#b81762'
          }}>
          Volver Home
        </Link>
      </div>
    </div>
  );
};

export default MapComponent;
