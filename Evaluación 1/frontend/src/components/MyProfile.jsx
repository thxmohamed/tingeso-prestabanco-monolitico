import React, { useEffect, useState } from 'react';
import "../App.css"

const MyProfile = () => {
  const [user, setUser] = useState(null);

  // Obtener datos del usuario loggeado al cargar el componente
  useEffect(() => {
    const storedUser = localStorage.getItem('user'); // Suponiendo que guardaste los datos en localStorage
    if (storedUser) {
      setUser(JSON.parse(storedUser)); // Parsear el string almacenado en un objeto
    } else {
      console.log("No se encontró ningún usuario loggeado.");
    }
  }, []);

  // Si el usuario no está loggeado, mostrar mensaje
  if (!user) {
    return <div>No estás loggeado.</div>;
  }

  return (
    <div className="profile-container">
      <h1>Mi Perfil</h1>
      <div className="profile-details">
        <p><strong>Nombre:</strong> {user.name}</p>
        <p><strong>Apellido:</strong> {user.lastName}</p>
        <p><strong>Correo:</strong> {user.email}</p>
        <p><strong>Rol:</strong> {user.rol}</p>
      </div>
    </div>
  );
};

export default MyProfile;