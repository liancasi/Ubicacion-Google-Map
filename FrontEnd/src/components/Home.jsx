import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import "bootstrap/dist/css/bootstrap.min.css";
import PersonaService from "../services/PersonaService";

const Home = () => {
    const [personas, setPersonas] = useState([]); // Estado para almacenar las personas
    const [error, setError] = useState(""); // Estado para manejar errores
    const [isHovered, setIsHovered] = useState(false); // Estado para manejar el hover de botones
    const navigate = useNavigate();

    // Efecto para verificar el token al cargar el componente
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/login"); // Redirigir a login si no hay token
        } else {
            loadPersonas(); // Cargar las personas
        }

        // Establecer un intervalo para actualizar los datos cada 10 segundos
        const intervalId = setInterval(() => {
            loadPersonas();
        }, 10000);

        // Limpiar el intervalo al desmontar el componente
        return () => clearInterval(intervalId);
    }, []);

    const loadPersonas = async () => {
        try {
            const data = await PersonaService.getAllPersons(); // Llama al servicio para obtener las personas
            console.log("Datos cargados:", data); // Verifica los datos cargados
            setPersonas(data); // Guarda los datos en el estado
        } catch (error) {
            console.error("Error loading personas:", error);
            setError("Error al cargar las personas."); // Muestra el error en la interfaz
        }
    };

    const handleDelete = async (id) => {
        const result = await Swal.fire({
            title: "¿Estás seguro?",
            text: "¡No podrás recuperar este registro ni sus datos asociados!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "¡Sí, eliminar!",
        });

        if (result.isConfirmed) {
            try {
                await PersonaService.deletePerson(id); // Llamar al servicio para eliminar la persona
                setPersonas(personas.filter((persona) => persona.idpersona !== id)); // Actualizar la lista de personas después de eliminar
                Swal.fire("Eliminado", "La persona ha sido eliminada.", "success");
            } catch (error) {
                console.error("Error al eliminar la persona:", error);
                Swal.fire("Error", "No se pudo eliminar el registro. Inténtalo de nuevo.", "error");
            }
        }
    };

    // Función para cerrar sesión
    const handleLogout = () => {
        Swal.fire({
            title: "¿Estás seguro que deseas cerrar sesión?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Sí, cerrar sesión",
            cancelButtonText: "Cancelar",
        }).then((result) => {
            if (result.isConfirmed) {
                localStorage.removeItem("token"); // Elimina el token del localStorage
                navigate("/login"); // Redirige al login
            }
        });
    };

    // Nueva función para mostrar la alerta con los detalles de la persona
    const showReminder = (persona) => {
        Swal.fire({
            title: 'Detalles de la Persona',
            html: `
                <strong>Username:</strong> ${persona.usuario.login}<br>`,
            icon: 'info',
            confirmButtonText: 'Cerrar'
        });
    };

    return (
        <div
            className="container-fluid"
            style={{
                background: "linear-gradient(to right, #c1d3ff, #c9b1ff)",
                minHeight: "100vh",
                padding: "0",
                margin: "0",
            }}
        >
            <h1 className="mb-4 text-center">Bienvenido "Conoce Tu Ubicación"</h1>
            <button
                className="btn btn-warning mb-3"
                onClick={handleLogout}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                style={{
                    transition: "background-color 0.3s",
                    backgroundColor: isHovered ? "#138496" : "#b81762",
                }}
            >
                Cerrar Sesión
            </button>

            {error && <div className="alert alert-danger">{error}</div>} {/* Mostrar error si existe */}

            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Primer Nombre</th>
                        <th>Segundo Nombre</th>
                        <th>Primer Apellido</th>
                        <th>Segundo Apellido</th>
                       
                        <th>Identificación</th>
                        <th>Email</th>
                        <th>Fecha de Nacimiento</th>
                        <th>Edad Clínica</th>
                        <th>Ubicación</th>
                        <th>Acciones de edición</th>
                        <th>Ubicación y Username</th>

                    </tr>
                </thead>
                <tbody>
                    {personas.length > 0 ? (
                        personas.map((persona) => (
                            <tr key={persona.idpersona}>
                                <td>{persona.idpersona || "No disponible"}</td>
                                <td>{persona.primerNombre || "No disponible"}</td>
                                <td>{persona.segundoNombre || "No disponible"}</td>
                                <td>{persona.primerApellido || "No disponible"}</td>
                                <td>{persona.segundoApellido || "No disponible"}</td>
                                <td>{persona.identificacion || "No disponible"}</td>
                                <td>{persona.email || "No disponible"}</td>
                                <td>{persona.fechaNacimiento || "No disponible"}</td>
                                <td>{persona.edadClinica || "No disponible"}</td>
                                <td>
                                    {persona.ubicacion
                                        ? `${persona.ubicacion.lugar}, ${persona.ubicacion.ciudad}, ${persona.ubicacion.departamento}`
                                        : "No disponible"}
                                </td>
                                <td className="text-center">
                                    <Link to={`/update/${persona.idpersona}`} className="btn btn-success me-2">
                                        Actualizar
                                    </Link>
                                    <button className="btn btn-danger" onClick={() => handleDelete(persona.idpersona)}>
                                        Eliminar
                                    </button>
                                    <Link to={`/change-password/${persona.idpersona}`} className="btn btn-warning">
                                        Cambiar Contraseña
                                    </Link>
                                   
                                </td>
                               
                                <td className="text-center">
                                <button className="btn btn-primary" onClick={() => showReminder(persona)}>
                                        Recordar Username
                                    </button>
                                    <Link to={`/coordenadas/${persona.idpersona}`} className="btn btn-info me-2">
                                        Ver Ubicación
                                    </Link>
                                </td>

                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="12" className="text-center">
                                No hay personas disponibles.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>

            {/* Botones adicionales */}
            <div className="d-flex justify-content-center mt-4">
                <Link to="/mapComponent" className="btn btn-info me-3">
                    Ver Ubicaciones en Mapa General
                </Link>
                <Link to="/mapaMultiple" className="btn btn-success me-1">
                    Ver Ubicaciones Con Selección Multiple
                </Link>
            </div>
        </div>
    );
};

export default Home;