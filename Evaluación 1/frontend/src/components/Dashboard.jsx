import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../Dashboard.css';

function Dashboard() {
  const navigate = useNavigate();

  // Función para cerrar sesión
  const handleLogout = () => {
    // Aquí puedes eliminar cualquier información de sesión o token
    alert('Has cerrado sesión exitosamente');
    // Redirigir a la página de inicio de sesión
    navigate('/');
  };

  return (
    <div className="dashboard-container">
      <h1>Bienvenido master, has iniciado sesión</h1>
      <p>Has iniciado sesión exitosamente en PrestaBanco.</p>

      {/* Botón para cerrar sesión */}
      <button onClick={handleLogout} className="logout-btn">
        Cerrar Sesión
      </button>
    </div>
  );
}

export default Dashboard;
