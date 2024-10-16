import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import userService from '../services/user.service'; // Importar el servicio
import Button from "@mui/material/Button"; // Importar el botón de Material UI

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(''); // Para mostrar errores
  const navigate = useNavigate();

  const goToRegister = () => {
    navigate('/register');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Llamar a la función login de userService
      const response = await userService.login(email, password);

      if (response.status === 200) {
        onLogin();
        localStorage.setItem('user', JSON.stringify(response.data));
        navigate('/home');
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setError('Credenciales incorrectas');
      } else {
        setError('Hubo un error al iniciar sesión. Intenta nuevamente.');
      }
    }
  };

  return (
    <div style={styles.loginContainer}>
      <h1>Iniciar Sesión</h1>
      {error && <p style={styles.errorMessage}>{error}</p>} {/* Mostrar el mensaje de error si existe */}
      <form onSubmit={handleSubmit} style={styles.form}>
        <input
          type="email"
          placeholder="Correo electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={styles.input}
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={styles.input}
        />
        <Button
          variant="contained"
          color="primary"
          type="submit"
          style={styles.button}
        >
          Iniciar Sesión
        </Button>

        <button style={styles.button} onClick={goToRegister}>
          Registrarse
        </button>

      </form>
    </div>
  );
};

// Definir los estilos en línea
const styles = {
  loginContainer: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    backgroundColor: '#f5f5f5',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    width: '300px',
  },
  input: {
    padding: '10px',
    margin: '10px 0',
    borderRadius: '5px',
    border: '1px solid #ccc',
  },
  button: {
    marginTop: '20px',
    padding: '10px',
  },
  errorMessage: {
    color: 'red',
    fontSize: '14px',
  },
};

export default Login;
