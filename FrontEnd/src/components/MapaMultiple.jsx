import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { GoogleMap, useLoadScript, MarkerF, InfoWindowF } from '@react-google-maps/api';
import { Link } from 'react-router-dom';
import Select from 'react-select';
import PersonaService from '../services/PersonaService';
import CoordenadasService from '../services/CoordenadasService';

const mapContainerStyle = {
  height: '400px',
  width: '100%',
};

const defaultCenter = { lat: 4.570868, lng: -74.297333 }; // Centro por defecto de Colombia
const libraries = ['places'];

function MapaMultiple() {
  const [personas, setPersonas] = useState([]);
  const [selectedPersonas, setSelectedPersonas] = useState([]);
  const [markers, setMarkers] = useState([]);
  const [activeMarker, setActiveMarker] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: "AIzaSyBomnY9UemevV5IVEXtYHP6SWw3kRsERGc",
    libraries,
  });

  useEffect(() => {
    const fetchPersonas = async () => {
      try {
        const data = await PersonaService.getAllPersons();
        console.log('Datos de personas:', data);
        setPersonas(data);
      } catch (error) {
        console.error('Error fetching personas:', error);
        setError('Error al cargar la lista de personas');
      }
    };

    fetchPersonas();
  }, []);

  useEffect(() => {
    const fetchCoordenadas = async () => {
      setIsLoading(true);
      try {
        if (selectedPersonas.length > 0) {
          const ids = selectedPersonas.map(persona => persona.value);
          const coordenadas = await CoordenadasService.getCoordenadasPorIds(ids);
          const markersData = coordenadas.map(coord => {
            const lat = parseFloat(coord.longitud); // Intercambiado de latitud a longitud
            const lng = parseFloat(coord.latitud);  // Intercambiado de longitud a latitud
            
            if (isNaN(lat) || isNaN(lng) || lat < -90 || lat > 90 || lng < -180 || lng > 180) {
              console.error(`Coordenadas inválidas para ID ${coord.id}: lat ${lat}, lng ${lng}`);
              return null;
            }

            return {
              id: coord.id,
              position: { lat, lng },
              title: coord.marca,
            };
          }).filter(marker => marker !== null);

          setMarkers(markersData);
        } else {
          setMarkers([]);
        }
      } catch (error) {
        console.error('Error al obtener coordenadas:', error);
        setError('Error al cargar las coordenadas');
      } finally {
        setIsLoading(false);
      }
    };

    fetchCoordenadas();
  }, [selectedPersonas]);

  const handlePersonaChange = (selectedOptions) => {
    setSelectedPersonas(selectedOptions);
  };

  const handleMarkerClick = useCallback((marker) => {
    setActiveMarker(marker);
  }, []);

  const handleInfoWindowClose = useCallback(() => {
    setActiveMarker(null);
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
        center={markers.length > 0 ? markers[0].position : defaultCenter}
        zoom={markers.length > 0 ? 10 : 5}
        options={mapOptions}
      >
        {markers.map((marker) => (
          <MarkerF
            key={marker.id}
            position={marker.position}
            title={marker.title}
            onClick={() => handleMarkerClick(marker)}
          />
        ))}
        {activeMarker && (
          <InfoWindowF
            position={activeMarker.position}
            onCloseClick={handleInfoWindowClose}
          >
            <div>
              <h3 className="font-bold">{activeMarker.title}</h3>
              <p>ID: {activeMarker.id}</p>
              <p>Latitud: {activeMarker.position.lat.toFixed(6)}</p>
              <p>Longitud: {activeMarker.position.lng.toFixed(6)}</p>
            </div>
          </InfoWindowF>
        )}
      </GoogleMap>
    );
  }, [isLoaded, markers, activeMarker, handleMarkerClick, handleInfoWindowClose, mapOptions]);

  if (loadError) {
    return <div>Error al cargar el mapa</div>;
  }

  return (
    <div 
      style={{
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        justifyContent: 'center', 
        height: '100vh', 
        background: 'linear-gradient(to bottom, #11cb46, #2575fc)' // Fondo degradado
      }}
    >

 
    <div className="container">
      <div className="row justify-content-center mt-4">
        <div className="col-md-8 bg-light rounded-lg shadow-lg p-4">
        <h1 className="card-title text-center mb-4 text-primary">Mapa de Selección de las Personas Registrada</h1>
          
          {personas.length > 0 ? (
            <Select
              isMulti
              options={personas.map(persona => ({
                value: persona.idpersona,
                label: `${persona.primerNombre} ${persona.segundoNombre || ''} ${persona.primerApellido}`.trim()
              }))}
              onChange={handlePersonaChange}
              placeholder="Seleccionar personas que desees"
              className="mb-4"
              styles={{
                control: (provided) => ({
                  ...provided,
                  backgroundColor: '#f8f9fa',
                }),
                option: (provided, state) => ({
                  ...provided,
                  backgroundColor: state.isSelected ? '#007bff' : state.isFocused ? '#e9ecef' : '#ffffff',
                  color: state.isSelected ? 'white' : 'black',
                }),
              }}
            />
          ) : (
            <div className="alert alert-info">No hay personas disponibles para seleccionar.</div>
          )}
          
          {error && (
            <div className="alert alert-danger" role="alert">
              <strong>Error!</strong> {error}
            </div>
          )}
          
          {isLoading ? (
            <div className="d-flex justify-content-center align-items-center" style={{ height: '400px' }}>
              <div className="spinner-border" role="status">
                <span className="visually-hidden">Cargando...</span>
              </div>
            </div>
          ) : (
            renderMap
          )}

          <div className="mt-4 text-center">
            <Link to="/home" className="btn btn-primary">
              Volver al Inicio
            </Link>
          </div>
        </div>
      </div>
    </div>
    </div>
  );
}

export default MapaMultiple;