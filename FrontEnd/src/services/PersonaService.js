import axios from 'axios';

const API_URL = 'http://localhost:9001/Laboratorio1'; // Cambia esto si es necesario

const PersonaService = {
    // Método privado para obtener el token
    _getToken: () => {
        return localStorage.getItem("token"); // Obtener el token
    },

    // Obtener todas las personas
    getAllPersons: async () => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.get(`${API_URL}/personas`, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al obtener personas:', error);
            throw error; // Propaga el error para manejarlo en el componente
        }
    },

    // Obtener una persona por ID
    getPersonById: async (id) => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.get(`${API_URL}/personass/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al obtener persona:', error);
            throw error;
        }
    },

    // Agregar una nueva persona
    addPerson: async (person) => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.post(`${API_URL}/persona`, person, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al agregar persona:', error);
            throw error;
        }
    },

    // Actualizar una persona existente
    updatePerson: async (id, person) => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.put(`${API_URL}/persona/${id}`, person, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al actualizar persona:', error);
            throw error;
        }
    },

    // Eliminar una persona
    deletePerson: async (id) => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.delete(`${API_URL}/persona/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al eliminar persona:', error);
            throw error;
        }
    },

    // Obtener API Key (si es necesario)
    getApiKey: async (username) => {
        try {
            const token = PersonaService._getToken(); // Obtener el token
            const response = await axios.get(`${API_URL}/apiKey`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'username': username, // Asegúrate de enviar el username también
                    'Content-Type': 'application/json',
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error al obtener la API Key:', error);
            throw error;
        }
    }
};

export default PersonaService;