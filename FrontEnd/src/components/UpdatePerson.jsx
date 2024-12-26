import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import "bootstrap/dist/css/bootstrap.min.css";
import BCrypt from "bcryptjs"; // Asegúrate de tener bcryptjs instalado

const UpdatePerson = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [persona, setPersona] = useState({
    id: id,
    primerNombre: "",
    segundoNombre: "",
    primerApellido: "",
    segundoApellido: "",
    identificacion: "",
    email: "",
    fechaNacimiento: "",
    ubicacion: {
      lugar: "",
      ciudad: "",
      departamento: "",
    }

  });

  useEffect(() => {
    const loadPersona = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        Swal.fire(
          "Error",
          "No se ha encontrado el token de autenticación.",
          "error"
        );
        navigate("/login");
        return;
      }
      const response = await fetch(
        `http://localhost:9001/Laboratorio1/persona/id/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (!response.ok) {
        Swal.fire("Error", "No se pudo cargar la persona.", "error");
        navigate("/home");
        return;
      }
      const data = await response.json();
      setPersona(data);
    };

    loadPersona();
  }, [id, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPersona((prevPersona) => ({
      ...prevPersona,
      [name]: value, // Actualiza directamente los campos del objeto persona
    }));
  };

  const handleUbicacionChange = (e) => {
    const { name, value } = e.target;
    setPersona((prevPersona) => ({
      ...prevPersona,
      ubicacion: {
        ...prevPersona.ubicacion,
        [name]: value,
      },
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("token");
    if (!token) {
      Swal.fire(
        "Error",
        "No se ha encontrado el token de autenticación.",
        "error"
      );
      navigate("/login");
      return;
    }

    // Verificamos si se ha proporcionado una nueva contraseña
    if (persona.usuario.newPassword) {
      const hashedPassword = BCrypt.hashSync(
        persona.usuario.newPassword,
        BCrypt.genSaltSync(12)
      );
      persona.usuario.password = hashedPassword; // Actualizamos la contraseña en el objeto persona
    }

    // Eliminamos la propiedad newPassword del objeto persona
    delete persona.usuario.newPassword;

    const response = await fetch(
      `http://localhost:9001/Laboratorio1/persona/${id}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(persona),
      }
    );

    if (response.ok) {
      Swal.fire(
        "Actualizado",
        "Los datos han sido actualizados correctamente.",
        "success"
      );
      navigate("/home");
    } else {
      Swal.fire("Error", "No se pudo actualizar los datos.", "error");
    }
  };

  const handleCancel = () => {
    navigate("/home");
  };

  return (
    <div
      className="container mt-5"
      style={{
        background: "linear-gradient(to right, #c1d3ff, #c9b1ff)",
        minHeight: "100vh",
      }}
    >
      <h2>Formulario de Actualización de Datos</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Primer Nombre</label>
          <input
            type="text"
            className="form-control"
            name="primerNombre"
            value={persona.primerNombre}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Segundo Nombre</label>
          <input
            type="text"
            className="form-control"
            name="segundoNombre"
            value={persona.segundoNombre}
            onChange={handleChange}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Primer Apellido</label>
          <input
            type="text"
            className="form-control"
            name="primerApellido"
            value={persona.primerApellido}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Segundo Apellido</label>
          <input
            type="text"
            className="form-control"
            name="segundoApellido"
            value={persona.segundoApellido}
            onChange={handleChange}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Identificación</label>
          <input
            type="text"
            className="form-control"
            name="identificacion"
            value={persona.identificacion}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            type="email"
            className="form-control"
            name="email"
            value={persona.email}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Fecha de Nacimiento</label>
          <input
            type="date"
            className="form-control"
            name="fechaNacimiento"
            value={persona.fechaNacimiento}
            onChange={handleChange}
            required
          />
        </div>

        <h5>Ubicación</h5>
        <div className="mb-3">
          <label className="form-label">Lugar</label>
          <input
            type="text"
            className="form-control"
            name="lugar"
            value={persona.ubicacion.lugar}
            onChange={handleUbicacionChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Ciudad</label>
          <input
            type="text"
            className="form-control"
            name="ciudad"
            value={persona.ubicacion.ciudad}
            onChange={handleUbicacionChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Departamento</label>
          <input
            type="text"
            className="form-control"
            name="departamento"
            value={persona.ubicacion.departamento}
            onChange={handleUbicacionChange}
            required
          />
        </div>



        <div className="d-flex justify-content-between">
          <button
            type="submit"
            className="btn"
            style={{ backgroundColor: "#4CAF50", color: "white" }}
          >
            Actualizar
          </button>
          <button
            type="button"
            className="btn"
            style={{ backgroundColor: "#6c757d", color: "white" }}
            onClick={handleCancel}
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default UpdatePerson;