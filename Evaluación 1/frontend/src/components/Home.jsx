import React from 'react';
import "../styles/Dashboard.css"

const Home = () => {
  return (
    <div className="dashboard-container">
      <h1>Bienvenido a Préstamos PrestaBanco</h1>
      
      <button className='dashboard-buttons'>Simular un crédito</button>
      <button className='dashboard-buttons'>Solicitar un crédito</button>
      <button className='dashboard-buttons'>Mi perfil</button>
      
    </div>
  );
};

export default Home;
