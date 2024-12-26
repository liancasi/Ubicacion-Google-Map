import React, { useState } from "react";
import { useParams } from "react-router-dom";
import UsuarioService from "../services/UsuarioService";
import Swal from "sweetalert2";
import bcrypt from "bcryptjs"; // Importar bcrypt

const ChangePassword = () => {
  const { id } = useParams();
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleChangePassword = async (e) => {
    e.preventDefault();
    try {
      // Encriptar la nueva contraseña
      const hashedPassword = await bcrypt.hash(password, 10);
      await UsuarioService.cambiarPassword(id, hashedPassword);
      Swal.fire("Éxito", "Contraseña cambiada correctamente.", "success");
      // No redirigir automáticamente
    } catch (error) {
      console.error("Error al cambiar la contraseña:", error);
      setError("Error al cambiar la contraseña.");
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        background: "linear-gradient(to bottom, #000569, #ffffff)",
      }}
    >
      <div className="container">
        <div className="row justify-content-center mt-4">
          <div className="row justify-content-center">
            <div className="col-md-6">
              <div
                className="card shadow-lg"
                style={{
                  background: "linear-gradient(to right, #ecffc1, #c3ffb1)",
                }}
              >
                <div className="card-body">
                  <div className="container mt-5">
                    <h1 className="text-center">Cambiar Contraseña</h1>
                    <form onSubmit={handleChangePassword} className="mt-4">
                      <div className="form-group">
                        <input
                          type="password"
                          className="form-control"
                          value={password}
                          onChange={(e) => setPassword(e.target.value)}
                          placeholder="Nueva Contraseña"
                          required
                        />
                      </div>
                      <button
                        type="submit"
                        className="btn btn-primary btn-block"
                      >
                        Cambiar Contraseña
                      </button>
                      {error && (
                        <div className="alert alert-danger mt-2">{error}</div>
                      )}
                    </form>
                    <div className="d-flex justify-content-center mt-4">
                    <button
                      className="btn btn-secondary mt-3"
                      onClick={() => window.history.back()}
                    >
                      Regresar
                    </button>
                    </div>

                  </div>
                  
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
