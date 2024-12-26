import axios from 'axios';

const API_URL = 'http://localhost:9001/Laboratorio1'; // Cambia esto a tu URL base

// Crear un objeto para el servicio
const CoordenadasService = {
  // Método para obtener todas las coordenadas
  getCoordenadas: async () => {
    try {
      const response = await axios.get(`${API_URL}/coordenadas`); // URL para obtener todas las coordenadas
      return response.data; // Devuelve los datos recibidos
    } catch (error) {
      console.error('Error fetching coordinates:', error);
      throw error; // Lanza el error para que pueda ser manejado en el componente
    }
  },

  getCoordenadasPorIds: async (ids) => {
    try {
      const idsString = ids.join(',');
      const response = await axios.get(`${API_URL}/coordenadas/lista`, {
        params: { ids: idsString },
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching coordinates for multiple IDs:', error);
      throw error;
    }
  },


  // Método para obtener las coordenadas individuales por ID
  getCoordenadasIndividual: async (id, token) => {
    try {
      const response = await axios.get(`${API_URL}/coordenadas/${id}`, {
        headers: {
          'Authorization': `Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJQUE9PSUlfSldUIiwic3ViIjoibGl6ZXRoY2FybyIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3Mjg3MDk4NjYsImV4cCI6MTcyOTU3Mzg2Nn0.eldgvkN0MnjzuK7PSktA7tk9oqdE--3N3PB08M4-AzWYTFFLMjAqUADZdJkrdXvHcwjVTH2IKyNfnuDE3oIYqw` 
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching individual coordinates:', error);
      throw error; // Lanza el error para que pueda ser manejado en el componente
    }
  }
};

export default CoordenadasService;
