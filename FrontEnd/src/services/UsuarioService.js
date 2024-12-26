import axios from 'axios';

const API_URL = 'http://localhost:9001/Laboratorio1'; // Reemplaza con la URL de tu API

const UsuarioService = {
    // Método privado para obtener el token
    _getToken: () => {
        return localStorage.getItem("token"); // Obtener el token
    },

    // Método para agregar un nuevo usuario
    agregarUsuario: async (usuario) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.post(`${API_URL}/usuario`, usuario, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al agregar usuario:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },

    // Método para cambiar la contraseña
    cambiarPassword: async (id, newPassword) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.put(`${API_URL}/usuario/changePassword/${id}`, { password: newPassword }, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al cambiar la contraseña:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },

    // Método para editar un usuario
    editarUsuario: async (usuario) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.put(`${API_URL}/usuario`, usuario, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al editar usuario:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },

    // Método para eliminar un usuario
    eliminarUsuario: async (login, persona) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.delete(`${API_URL}/usuario/${login}/${persona}`, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al eliminar usuario:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },

    // Método para listar usuarios
    listarUsuarios: async (page, size) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.get(`${API_URL}/usuarios`, { 
                params: { page, size },
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al listar usuarios:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },

    // Método para obtener un usuario por login y persona
    obtenerUsuario: async (login, persona) => {
        try {
            const token = UsuarioService._getToken(); // Obtener el token
            const response = await axios.get(`${API_URL}/usuario/${login}/${persona}`, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Envía el token de autenticación
                },
            });
            return response.data; // Retorna los datos de la respuesta
        } catch (error) {
            console.error("Error al obtener usuario:", error);
            throw error; // Lanzar error para manejarlo en el componente
        }
    },
};

export default UsuarioService;