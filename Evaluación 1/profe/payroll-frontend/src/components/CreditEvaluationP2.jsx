import React, { useState } from 'react';
import "../App.css"

const CreditEvaluationP2 = ({ creditId }) => {
  const [quotaIncome, setQuotaIncome] = useState(null);
  const [creditHistory, setCreditHistory] = useState(null);
  const [employmentStability, setEmploymentStability] = useState(null);
  const [debtIncome, setDebtIncome] = useState(null);
  const [maxFinancing, setMaxFinancing] = useState(null);
  const [applicantAge, setApplicantAge] = useState(null);

  const handleEvaluation = () => {
    alert('Evaluación enviada');
  };

  return (
    <div className="credit-evaluation-container">
      <h2>Evaluación de Crédito</h2>

      <div className="credit-evaluation">
        <div className="rule">
          <p>Relación Cuota/Ingreso</p>
          <button className="btn-green" onClick={() => setQuotaIncome(true)}>Cumple</button>
        </div>

        <div className="rule">
          <p>Historial Crediticio</p>
          <button className="btn-green" onClick={() => setCreditHistory(true)}>Cumple</button>
        </div>

        <div className="rule">
          <p>Antigüedad Laboral y Estabilidad</p>
          <button className="btn-green" onClick={() => setEmploymentStability(true)}>Cumple</button>
        </div>

        <div className="rule">
          <p>Relación Deuda/Ingreso</p>
          <button className="btn-green" onClick={() => setDebtIncome(true)}>Cumple</button>
        </div>

        <div className="rule">
          <p>Monto Máximo de Financiamiento</p>
          <button className="btn-green" onClick={() => setMaxFinancing(true)}>Cumple</button>
        </div>

        <div className="rule">
          <p>Edad del Solicitante</p>
          <button className="btn-green" onClick={() => setApplicantAge(true)}>Cumple</button>
        </div>

        <button onClick={handleEvaluation} className="btn-submit">Enviar Evaluación</button>
      </div>
    </div>
  );
};

export default CreditEvaluationP2;
