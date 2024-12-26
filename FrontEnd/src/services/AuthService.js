// src/services/AuthService.js

const API_BASE_URL = 'http://localhost:9001'; // Cambia esto si es necesario

export const AuthService = {
    // Método para iniciar sesión
    login: async (username, password) => {
        try {
            const response = await fetch(`${API_BASE_URL}/Laboratorio1/personass`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ login: username, password }),
            });

            if (!response.ok) {
                throw new Error('Error en el inicio de sesión');
            }

            const data = await response.json();
            localStorage.setItem('token', data.token); // Almacena el token
            localStorage.setItem('apiKey', data.apiKey); // Almacena el API Key
            return data; // Devuelve los datos, incluyendo el token
        } catch (error) {
            console.error('Error en login:', error);
            throw error;
        }
    },

    // Método para obtener la API Key
    fetchApiKey: async (username) => {
        try {
            const response = await fetch(`${API_BASE_URL}/apiKey`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'username': username, // Envía el nombre de usuario como encabezado
                },
            });

            if (!response.ok) {
                throw new Error('Error al obtener la API Key');
            }

            const data = await response.json();
            return data; // Devuelve la API Key
        } catch (error) {
            console.error('Error en fetchApiKey:', error);
            throw error; // Propaga el error
        }
    },

    // Método para realizar solicitudes que requieren el API Key
    makeAuthenticatedRequest: async (endpoint) => {
        const apiKey = localStorage.getItem('apiKey'); // Obtiene el API Key del localStorage

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'GET', // Cambia esto según el tipo de solicitud que necesites
                headers: {
                    'Content-Type': 'application/json',
                    'apikey': apiKey, // Incluye el API Key en los encabezados
                },
            });

            if (!response.ok) {
                throw new Error('Error en la solicitud autenticada');
            }

            const data = await response.json();
            return data; // Devuelve la respuesta de la solicitud
        } catch (error) {
            console.error('Error en makeAuthenticatedRequest:', error);
            throw error; // Propaga el error
        }
    },
};