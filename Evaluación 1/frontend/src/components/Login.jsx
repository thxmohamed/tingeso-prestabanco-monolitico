import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css'; 
import userService from '../services/user.service'; // Importar el servicio

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(''); // Para mostrar errores
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Llamar a la función login de userService
      const response = await userService.login(email, password);

      if (response.status === 200) {
        onLogin();
        navigate('/home');
        console.log(response.data);
        localStorage.setItem('user', JSON.stringify(response.data));
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
      {error && <p className="error-message">{error}</p>} {}
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
