import React, { useEffect, useState, useCallback, useMemo } from 'react';
import { GoogleMap, useLoadScript, MarkerF, InfoWindowF } from '@react-google-maps/api';
import { useParams, useNavigate } from 'react-router-dom';
import CoordenadasService from '../services/CoordenadasService';

const mapContainerStyle = {
  height: '400px',
  width: '100%',
};

const defaultCenter = { lat: 4.570868, lng: -74.297333 }; // Default center of Colombia

const libraries = ['places'];

function MapaIndividual() {
  const { idpersona } = useParams();
  const navigate = useNavigate();
  const [marker, setMarker] = useState(null);
  const [isInfoWindowOpen, setIsInfoWindowOpen] = useState(false);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: "AIzaSyBomnY9UemevV5IVEXtYHP6SWw3kRsERGc",
    libraries,
  });

  const fetchCoordenadas = useCallback(async () => {
    try {
      setIsLoading(true);
      const coordenadas = await CoordenadasService.getCoordenadasIndividual(idpersona);
      
      if (!coordenadas || !coordenadas.latitud || !coordenadas.longitud) {
        throw new Error('Se recibieron datos inválidos o incompletos');
      }

      const lat = parseFloat(coordenadas.longitud);
      const lng = parseFloat(coordenadas.latitud);

      if (isNaN(lat) || isNaN(lng) || lat < -90 || lat > 90 || lng < -180 || lng > 180) {
        throw new Error('Coordenadas fuera de rango válido');
      }

      setMarker({
        id: coordenadas.id,
        marca: coordenadas.marca,
        lat,
        lng,
      });
      setError(null);
    } catch (error) {
      console.error('Error al obtener coordenadas:', error);
      setError(error.message || 'Error al obtener las coordenadas');
    } finally {
      setIsLoading(false);
    }
  }, [idpersona]);

  useEffect(() => {
    fetchCoordenadas();
  }, [fetchCoordenadas]);

  const handleMarkerClick = useCallback(() => {
    setIsInfoWindowOpen(true);
  }, []);

  const handleInfoWindowClose = useCallback(() => {
    setIsInfoWindowOpen(false);
  }, []);

  const mapOptions = useMemo(() => ({
    disableDefaultUI: true,
    clickableIcons: false,
    scrollwheel: true,
  }), []);

  const renderMap = useMemo(() => {
    if (!isLoaded) return null;

    return (
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        center={marker ? { lat: marker.lat, lng: marker.lng } : defaultCenter}
        zoom={marker ? 12 : 5}
        options={mapOptions}
      >
        {marker && (
          <MarkerF
            position={{ lat: marker.lat, lng: marker.lng }}
            onClick={handleMarkerClick}
          >
            {isInfoWindowOpen && (
              <InfoWindowF
                position={{ lat: marker.lat, lng: marker.lng }}
                onCloseClick={handleInfoWindowClose}
              >
                <div>
                  <h3>{marker.marca}</h3>
                  <p>ID: {marker.id}</p>
                  <p>Latitud: {marker.lat.toFixed(6)}</p>
                  <p>Longitud: {marker.lng.toFixed(6)}</p>
                </div>
              </InfoWindowF>
            )}
          </MarkerF>
        )}
      </GoogleMap>
    );
  }, [isLoaded, marker, isInfoWindowOpen, handleMarkerClick, handleInfoWindowClose, mapOptions]);

  if (loadError) {
    return <div>Error al cargar el mapa</div>;
  }

  const handleGoHome = () => {
    navigate('/home');
  };

  return (

    <div 
      style={{
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        justifyContent: 'center', 
        height: '100vh', 
        background: 'linear-gradient(to bottom, #c2cb11, #3025fc,  #fc3e25)' // Fondo degradado
      }}
    >


     <div className="container">
      <div className="row justify-content-center mt-4">
        <div className="col-md-8 bg-light rounded-lg shadow-lg p-4">
        <h1 className="card-title text-center">Mapa Individual de la Persona Registrada</h1>

        {error && (
          <div className="alert alert-danger" role="alert">
            <strong>Error!</strong> {error}
          </div>
        )}

        {isLoading ? (
          <div className="d-flex justify-content-center align-items-center" style={{ height: '400px' }}>
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Cargando...</span>
            </div>
          </div>
        ) : (
          <div className="d-flex justify-content-center">
            {renderMap}
          </div>
        )}
      </div>
      
      <div className="mt-4 text-center">
        <button 
          onClick={handleGoHome} 
          className="btn btn-success btn-lg"
        >
          Volver al Inicio
        </button>
      </div>
    </div>
    </div>
    </div>
  );
}

export default MapaIndividual;
