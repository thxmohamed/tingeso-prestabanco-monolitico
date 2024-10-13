import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/Login.css'; 

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(''); // Para mostrar errores
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://192.168.1.10:8090/customer/login', {
        email,
        password
      });

      if (response.status === 200) {
        onLogin(); // Establece el estado de login en true
        navigate('/dashboard'); // Redirige al dashboard
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setError('Credenciales incorrectas'); // Mensaje de error
      } else {
        setError('Hubo un error al iniciar sesión. Intenta nuevamente.');
      }
    }
  };

  return (
    <div className="login-container">
      <h1>Iniciar Sesión</h1>
      {error && <p className="error-message">{error}</p>} {/* Mostrar error */}
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Correo electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button className='btn-login'>Iniciar Sesión</button>
      </form>
    </div>
  );
};

export default Login;
