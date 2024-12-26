import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
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
    },
    usuario: {
      login: "",
      password: "",
    },
  });

  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalType, setModalType] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name.includes(".")) {
      const [objectName, field] = name.split(".");
      setFormData((prevData) => ({
        ...prevData,
        [objectName]: {
          ...prevData[objectName],
          [field]: value,
        },
      }));
    } else {
      setFormData((prevData) => ({ ...prevData, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const currentDate = new Date();
    const selectedDate = new Date(formData.fechaNacimiento);
    const year = selectedDate.getFullYear();

    if (selectedDate > currentDate) {
      setModalMessage("La fecha de nacimiento no puede ser futura.");
      setModalType("error");
      setShowModal(true);
      return;
    }

    if (year < 1900 || year > 2100) {
      setModalMessage("El año de nacimiento debe estar entre 1900 y 2100.");
      setModalType("error");
      setShowModal(true);
      return;
    }

    const dataToSend = {
      ...formData,
      identificacion: parseInt(formData.identificacion),
      usuario: {
        ...formData.usuario,
        id: {
          login: formData.usuario.login,
        },
      },
    };

    try {
      const response = await fetch("http://localhost:9001/Laboratorio1/persona", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(dataToSend),
      });

      let responseData;
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        responseData = await response.json();
      } else {
        responseData = await response.text();
      }

      if (response.ok) {
        setModalMessage(typeof responseData === "object" ? responseData.message : responseData);
        setModalType("success");
        setShowModal(true);
        // Clear the form after successful registration
        setFormData({
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
          },
          usuario: {
            login: "",
            password: "",
          },
        });
      } else {
        setModalMessage(typeof responseData === "object" ? responseData.message : responseData);
        setModalType("error");
        setShowModal(true);
      }
    } catch (error) {
      console.error("Error:", error);
      setModalMessage("Error en la solicitud.");
      setModalType("error");
      setShowModal(true);
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleGoToLogin = () => {
    navigate("/login");
  };

  return (
    <div
    style={{
      display: "flow-root",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
      height: "240vh",
      background: "linear-gradient(to bottom, #000569, #e2a7af)",
    }}
  >
         <div className="container">
        <div className="row justify-content-center mt-4">
   

            <div className="row justify-content-center">
              <div className="col-md-6">
                <div className="card shadow-lg"
                  style={{
                    background: "linear-gradient(to right, #c1d3ff, #c9b1ff)",
                  }}
                >
                  <div className="card-body">
          <h2>Formulario de Registro de Persona</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            {/* Form inputs */}
            <div className="mb-3">
              <label className="form-label">Primer Nombre:</label>
              <input
                type="text"
                name="primerNombre"
                className="form-control"
                value={formData.primerNombre}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Segundo Nombre:</label>
              <input
                type="text"
                name="segundoNombre"
                className="form-control"
                value={formData.segundoNombre}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Primer Apellido:</label>
              <input
                type="text"
                name="primerApellido"
                className="form-control"
                value={formData.primerApellido}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Segundo Apellido:</label>
              <input
                type="text"
                name="segundoApellido"
                className="form-control"
                value={formData.segundoApellido}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Identificación:</label>
              <input
                type="number"
                name="identificacion"
                className="form-control"
                value={formData.identificacion}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Email:</label>
              <input
                type="email"
                name="email"
                className="form-control"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Fecha de Nacimiento:</label>
              <input
                type="date"
                name="fechaNacimiento"
                className="form-control"
                value={formData.fechaNacimiento}
                onChange={handleChange}
                required
              />
            </div>
            <h3>Ubicación</h3>
            <div className="mb-3">
              <label className="form-label">Lugar:</label>
              <input
                type="text"
                name="ubicacion.lugar"
                className="form-control"
                value={formData.ubicacion.lugar}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Ciudad:</label>
              <input
                type="text"
                name="ubicacion.ciudad"
                className="form-control"
                value={formData.ubicacion.ciudad}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Departamento:</label>
              <input
                type="text"
                name="ubicacion.departamento"
                className="form-control"
                value={formData.ubicacion.departamento}
                onChange={handleChange}
                required
              />
            </div>
            <h3>Usuario</h3>
            <div className="mb-3">
              <label className="form-label">Login:</label>
              <input
                type="text"
                name="usuario.login"
                className="form-control"
                value={formData.usuario.login}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Contraseña:</label>
              <input
                type="password"
                name="usuario.password"
                className="form-control"
                value={formData.usuario.password}
                onChange={handleChange}
                required
                autoComplete="new-password"
              />
            </div>
            <button type="submit" className="btn btn-primary">Registrar</button>
            <button type="button" className="btn btn-secondary ms-2" onClick={handleGoToLogin}>
              Volver al Login
            </button>
          </form>
        </div>
      </div>
      {showModal && (
        <div className={`modal fade show d-block`} style={{ display: 'block' }} tabIndex="-1">
          <div className="modal-dialog">
            <div className={`modal-content ${modalType === 'success' ? 'bg-success' : 'bg-danger'}`}>
              <div className="modal-header">
                <h5 className="modal-title">{modalType === 'success' ? 'Éxito' : 'Error'}</h5>
                <button type="button" className="btn-close" onClick={handleCloseModal} aria-label="Close"></button>
              </div>
              <div className="modal-body">
                <p>{modalMessage}</p>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Cerrar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
    </div>
    </div>
    </div>
    </div>
    
  );
}

export default Register;