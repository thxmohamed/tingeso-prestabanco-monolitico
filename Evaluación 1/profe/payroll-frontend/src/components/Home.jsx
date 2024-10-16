import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css'

const Home = () => {
  const navigate = useNavigate();

  const goToProfile = () => {
    navigate('/profile'); 
  };

    const goToApplication = () => {
    navigate('/application'); 
  };

  const goToSimulate = () => {
    navigate('/simulate')
  }

  const handleLogout = () => {
    localStorage.removeItem('user'); // Elimina al usuario del localStorage
    navigate('/login'); // Redirige al Login
  };

  return (
    <div style={styles.homeContainer}>
      <h1 style={styles.heading}>Bienvenido a Préstamos PrestaBanco</h1>
      
      <button style={styles.button} onClick={goToSimulate}>
        Simular un crédito
      </button>
      <button style={styles.button} onClick={goToApplication}>
        Solicitar un crédito
      </button>
      <button style={styles.button} onClick={goToProfile}>
        Mi perfil
      </button>
      <button className='logout-button' onClick={handleLogout}>
        Cerrar sesión
      </button>
      
    </div>
  );
};

// Estilos en línea
const styles = {
  homeContainer: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    backgroundColor: '#f5f5f5',
  },
  heading: {
    color: '#333',
    fontSize: '28px',
    marginBottom: '30px',
  },
  button: {
    padding: '15px 25px',
    margin: '10px',
    fontSize: '16px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
  },
};

export default Home;
