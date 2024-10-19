import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import creditService from '../services/credit.service';
import '../App.css';

// Componente para mostrar los detalles de una solicitud seleccionada
const ApplicationDetails = ({ credit }) => {
  if (!credit) return <p>Selecciona una solicitud para ver los detalles</p>;

  return (
    <div className="application-details">
      <h3>Detalles de la Solicitud</h3>
      <p><strong>Plazo (años):</strong> {credit.yearsLimit}</p>
      <p><strong>Tasa de Interés:</strong> {credit.interestRate}%</p>
      <p><strong>Cuota Mensual:</strong> ${credit.monthlyFee}</p>
    </div>
  );
};

const ApplicationHistory = () => {
  const [applications, setApplications] = useState([]);
  const [selectedApplication, setSelectedApplication] = useState(null);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1); // Estado para la página actual
  const applicationsPerPage = 1; // Cantidad de solicitudes por página
  const navigate = useNavigate();

  useEffect(() => {
    // Obtener el ID del usuario loggeado desde localStorage
    const clientID = JSON.parse(localStorage.getItem('user')).id;

    // Llamar al servicio para obtener las solicitudes por ID de cliente
    creditService.getByClientID(clientID)
      .then(response => {
        setApplications(response.data);
      })
      .catch(error => {
        console.error("Error al obtener las solicitudes:", error);
        setError("Hubo un error al cargar las solicitudes.");
      });
  }, []);

  const indexOfLastApplication = currentPage * applicationsPerPage;
  const indexOfFirstApplication = indexOfLastApplication - applicationsPerPage;
  const currentApplications = applications.slice(indexOfFirstApplication, indexOfLastApplication);

  const handleApplicationClick = (application) => {
    setSelectedApplication(application);
  };

  const prevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  // Función para ir a la página siguiente
  const nextPage = () => {
    if (currentPage < Math.ceil(applications.length / applicationsPerPage)) {
      setCurrentPage(currentPage + 1);
    }
  };

  const goBack = () => {
    navigate('/profile');
  };

  return (
    <div className="application-history-container">
      <button className="btn-back" onClick={goBack}>
        Atrás
      </button>

      <h1>Mis Solicitudes</h1>

      {error && <p className="error">{error}</p>}

      <div className="applications-list">
        {applications.length === 0 ? (
          <p>No tienes solicitudes de crédito.</p>
        ) : (
          <>
            <ul>
              {currentApplications.map((app) => (
                <li
                  key={app.id}
                  onClick={() => handleApplicationClick(app)}
                  className={`clickable ${selectedApplication?.id === app.id ? 'selected' : ''}`}
                >
                  <p><strong>Tipo de Préstamo:</strong> {app.loanType}</p>
                  <p><strong>Monto Solicitado:</strong> ${app.requestedAmount}</p>
                  <p><strong>Estado:</strong> {app.status}</p>
                </li>
              ))}
            </ul>

            {/* Controles de paginación */}
            <div className="pagination-controls">
              <button onClick={prevPage} disabled={currentPage === 1}>Anterior</button>
              <span>Página {currentPage} de {Math.ceil(applications.length / applicationsPerPage)}</span>
              <button onClick={nextPage} disabled={currentPage === Math.ceil(applications.length / applicationsPerPage)}>Siguiente</button>
            </div>
          </>
        )}
      </div>

      <ApplicationDetails credit={selectedApplication} />
    </div>
  );
};

export default ApplicationHistory;
