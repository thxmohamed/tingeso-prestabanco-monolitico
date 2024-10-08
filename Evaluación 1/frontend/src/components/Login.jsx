import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../Login.css';

function Login() {
  const [form, setForm] = useState({
    email: '',
    password: ''
  });
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate(); // Para redirigir después del login exitoso

  // Manejar cambios en los campos
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // Manejar envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();

    const loginData = {
      email: form.email,
      password: form.password
    };

    try {
      const response = await fetch('http://192.168.1.10:8090/customer/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
      });

      const data = await response.text();
      if (response.ok && data === "Successful login") {
        // Login exitoso
        alert('Inicio de sesión exitoso');
        setErrorMessage('');
        // Redirigir a la página deseada, como un dashboard
        navigate('/dashboard');
      } else {
        // Credenciales incorrectas
        setErrorMessage('Credenciales incorrectas. Intenta de nuevo.');
      }
    } catch (error) {
      // Manejo de errores en la solicitud
      setErrorMessage('Error al intentar iniciar sesión. Intenta más tarde.');
    }
  };

  return (
    <div className="login-container">
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Contraseña:</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
          />
        </div>

        {errorMessage && <p className="error">{errorMessage}</p>}

        <button type="submit" className='btn-login'>
            Iniciar Sesión
            </button>
      </form>
    </div>
  );
}

export default Login;
