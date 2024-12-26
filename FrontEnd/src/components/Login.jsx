import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { AuthService } from "../services/AuthService";
import "primereact/resources/themes/saga-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
import "bootstrap/dist/css/bootstrap.min.css"; // Asegúrate de importar Bootstrap

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");

    try {
      const authResponse = await AuthService.login(username, password);
      console.log(authResponse); // Verifica la respuesta
      if (authResponse && authResponse.token) {
        localStorage.setItem("token", authResponse.token); // Almacena el token
        const apiKeyResponse = await AuthService.fetchApiKey(username);
        if (apiKeyResponse && apiKeyResponse.apiKey) {
          localStorage.setItem("apiKey", apiKeyResponse.apiKey);
          navigate("/home"); // Redirige a la página de inicio
        } else {
          setError("No se pudo obtener la API Key");
        }
      } else {
        setError("Respuesta del servidor inválida");
      }
    } catch (error) {
      setError("Credenciales inválidas o error en la autenticación");
      console.error("Error al autenticar:", error);
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
                <div className="card shadow-lg"
                  style={{
                    background: "linear-gradient(to right, #c1d3ff, #c9b1ff)",
                  }}
                >
                  <div className="card-body">
                    <h2 className="text-center text-primary">Iniciar Sesión</h2>
                    <form onSubmit={handleSubmit} className="p-fluid">
                      {error && (
                        <div className="alert alert-danger">{error}</div>
                      )}
                      <div className="mb-3">
                        <label htmlFor="username" className="form-label">
                          Nombre de usuario:
                        </label>
                        <InputText
                          id="username"
                          value={username}
                          onChange={(e) => setUsername(e.target.value)}
                          required
                          className="form-control"
                        />
                      </div>
                      <div className="mb-3">
                        <label htmlFor="password" className="form-label">
                          Contraseña:
                        </label>
                        <InputText
                          id="password"
                          type="password"
                          value={password}
                          onChange={(e) => setPassword(e.target.value)}
                          required
                          className="form-control"
                        />
                      </div>
                      <Button
                        type="submit"
                        label="Iniciar Sesión"
                        className="btn btn-primary w-100"
                      />
                      <div className="mt-3 text-center">
                        <Button
                          label="Registrarse"
                          className="btn btn-secondary"
                          onClick={() => navigate("/register")} // Redirige a la página de registro
                        />
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    
    
  );
};

export default Login;
