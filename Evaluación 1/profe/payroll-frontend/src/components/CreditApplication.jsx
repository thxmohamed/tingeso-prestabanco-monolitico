import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../App.css";
import FileUpload from './FileUpload'; // Importamos FileUpload
import creditService from '../services/credit.service';

const CreditApplication = () => {
  const [form, setForm] = useState({
    loanType: 'Primera Vivienda',
    requestedAmount: '',
    propertyValue: '',
    yearsLimit: '',
  });

  const [monthlyFee, setMonthlyFee] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);  // Estado para controlar si se ha enviado el formulario
  const [generatedCreditID, setGeneratedCreditID] = useState(null); // Estado para guardar el ID del crédito
  const navigate = useNavigate();

  const loanTypesMap = {
    "Primera Vivienda": {
      loanType: "PRIMERA_VIVIENDA",
      interestRate: 5.0,
      maxYears: 30
    },
    "Segunda Vivienda": {
      loanType: "SEGUNDA_VIVIENDA",
      interestRate: 6.0,
      maxYears: 20
    },
    "Propiedades Comerciales": {
      loanType: "PROPIEDADES_COMERCIALES",
      interestRate: 7.0,
      maxYears: 25
    },
    "Remodelación": {
      loanType: "REMODELACION",
      interestRate: 6.0,
      maxYears: 15
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const validateForm = () => {
    const selectedLoanType = loanTypesMap[form.loanType];
    if (form.yearsLimit > selectedLoanType.maxYears) {
      return `El plazo máximo para ${form.loanType} es de ${selectedLoanType.maxYears} años.`;
    }
    if (!form.requestedAmount || !form.yearsLimit) {
      return 'Todos los campos son obligatorios.';
    }
    return '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }
    setError('');
    setLoading(true);

    const clientID = JSON.parse(localStorage.getItem('user')).id;
    const selectedLoanType = loanTypesMap[form.loanType];

    const creditEntity = {
      clientID: clientID,
      loanType: selectedLoanType.loanType,
      requestedAmount: form.requestedAmount,
      yearsLimit: form.yearsLimit,
      interestRate: selectedLoanType.interestRate,
      propertyValue: form.propertyValue,
      status: "E1_EN_REVISION_INICIAL"
    };

    try {
      // Simulamos el crédito para obtener la cuota mensual
      const simulateResponse = await creditService.simulate(creditEntity);
      const monthlyFee = simulateResponse.data;
      setMonthlyFee(monthlyFee);

      // Guardamos el crédito en la base de datos
      const creditData = {
        ...creditEntity,
        monthlyFee: monthlyFee
      };
      const saveResponse = await creditService.save(creditData);
      const creditID = saveResponse.data.id;
      setGeneratedCreditID(creditID); 
      setIsSubmitted(true); 

      alert("Crédito guardado exitosamente.");
      console.log("Este es el credito real: ", creditID);
    } catch (error) {
      console.error("Error en la simulación o guardado del crédito:", error);
      setError('Ocurrió un error al procesar la solicitud.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="credit-application-container">
      <h1>Solicitud de Crédito</h1>

      {!isSubmitted ? (
        <form onSubmit={handleSubmit} className="credit-form">
          <div className="form-group">
            <label>Tipo de Préstamo</label>
            <select
              name="loanType"
              value={form.loanType}
              onChange={handleChange}
              className="input-field"
            >
              <option value="Primera Vivienda">Primera Vivienda</option>
              <option value="Segunda Vivienda">Segunda Vivienda</option>
              <option value="Propiedades Comerciales">Propiedades Comerciales</option>
              <option value="Remodelación">Remodelación</option>
            </select>
          </div>

          <div className="form-group">
            <label>Monto Solicitado ($)</label>
            <input
              type="number"
              name="requestedAmount"
              value={form.requestedAmount}
              onChange={handleChange}
              required
              placeholder="Ej. 100000"
              className="input-field"
            />
          </div>

          <div className="form-group">
            <label>Valor de la Propiedad ($)</label>
            <input
              type="number"
              name="propertyValue"
              value={form.propertyValue}
              onChange={handleChange}
              required
              placeholder="Ej. 150000"
              className="input-field"
            />
          </div>

          <div className="form-group">
            <label>Plazo (años)</label>
            <input
              type="number"
              name="yearsLimit"
              value={form.yearsLimit}
              onChange={handleChange}
              required
              placeholder="Ej. 15"
              className="input-field"
            />
          </div>

          {error && <p className="error">{error}</p>}

          <button type="submit" className="submit-btn" disabled={loading}>
            {loading ? 'Procesando...' : 'Adjuntar Archivos'}
          </button>

          <button className="logout-button" onClick={() => navigate('/home')}>
            Atrás
          </button>

          {monthlyFee && (
            <p className="success">
              La cuota mensual a pagar es de ${monthlyFee}
            </p>
            
          )}
        </form>
      ) : (
        <FileUpload creditID={generatedCreditID} loanType={form.loanType} />
      )}
    </div>
  );
};

export default CreditApplication;
