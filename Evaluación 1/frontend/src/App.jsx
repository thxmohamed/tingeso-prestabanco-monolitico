import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Home from './components/Home';
import Register from './components/Register';
import './App.css'
import MyProfile from './components/MyProfile';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  return (
    <Router>
      <div className="App">
        <nav>
          <ul>
            {isLoggedIn ? (
              <button className='btn-login' onClick={handleLogout}>Cerrar sesión</button>
            ) : (
              <>
                <button className='btn-login'><Link to="/login">Iniciar Sesión</Link></button>
                <button className='btn-register'><Link to="/register">Registrarse</Link></button>
              </>
            )}
          </ul>
        </nav>

        <Routes>
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/home" element={isLoggedIn ? <Home /> : <Navigate to="/login" />} />
          <Route path="/profile" element={<MyProfile />} />
          <Route path="*" element={<Navigate to={isLoggedIn ? "/home" : "/login"} />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
