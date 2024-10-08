import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../Header.css';

function Header() {
  const location = useLocation();  // Hook para obtener la ubicación actual

  return (
    <div className="header">
      {location.pathname === '/register' ? (
        <Link to="/" className="btn-login">Iniciar Sesión</Link>
      ) : (
        <Link to="/register" className="btn-register">Registrarse</Link>
      )}
    </div>
  );
}

export default Header;
