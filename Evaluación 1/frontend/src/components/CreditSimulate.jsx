import React, { useState } from 'react';
import creditService from '../services/credit.service';
import { useNavigate } from 'react-router-dom';
import "../App.css";

const CreditSimulate = () => {
  const [form, setForm] = useState({
    interestRate: '',
    yearsLimit: '',
    requestedAmount: ''
  });
  const [monthlyPayment, setMonthlyPayment] = useState(null);
  const [error, setError] = useState('');

  // Maneja los cambios en los inputs del formulario
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const navigate = useNavigate();
  const handleBack = () => {
    navigate('/home');
  };

  // Enviar los datos a través del servicio de simulación de crédito
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); // Limpiar error previo

    try {
      const response = await creditService.simulate(form);
      setMonthlyPayment(response.data); // Guardar el pago mensual en el estado
    } catch (err) {
      setError('Hubo un error al simular el crédito. Inténtalo de nuevo.');
      console.error(err);
    }
  };

  return (
    <div className="credit-simulate-container">
      <h1>Simulación de Crédito</h1>
      
      <form onSubmit={handleSubmit}>
        <div>
          <label>Tasa de Interés Anual (%)</label>
          <input
            type="number"
            name="interestRate"
            value={form.interestRate}
            onChange={handleChange}
            required
            step="0.01"
            placeholder="Ej. 5"
          />
        </div>

        <div>
          <label>Años de Plazo</label>
          <input
            type="number"
            name="yearsLimit"
            value={form.yearsLimit}
            onChange={handleChange}
            required
            placeholder="Ej. 5"
          />
        </div>

        <div>
          <label>Monto Solicitado ($)</label>
          <input
            type="number"
            name="requestedAmount"
            value={form.requestedAmount}
            onChange={handleChange}
            required
            placeholder="Ej. 10000"
          />
        </div>

        <button className='button'>Simular Crédito</button>
        <button className='button' onClick={handleBack}>
            Atrás
        </button>
      </form>

      {error && <p className="error">{error}</p>}
      
      {monthlyPayment !== null && (
        <div className="result">
          <h2>La cuota mensual a pagar es de ${monthlyPayment.toFixed(2)}</h2>
        </div>
      )}
    </div>
  );
};

export default CreditSimulate;
