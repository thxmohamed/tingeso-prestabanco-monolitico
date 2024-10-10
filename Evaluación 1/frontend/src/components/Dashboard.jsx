import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard-container">
      <h1>Bienvenido al Dashboard</h1>
      <div className="dashboard-buttons">
        <button>Simular un crédito</button>
        <button>Solicitar un crédito</button>
        <button><Link to="/profile">Mi perfil</Link></button>
      </div>
    </div>
  );
};

export default Dashboard;
