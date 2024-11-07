import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import "../App.css";
import checkrulesService from '../services/checkrules.service';
import creditService from '../services/credit.service';

const CreditEvaluationP2 = () => {
  const navigate = useNavigate();
  const { creditID } = useParams();

  const [evaluationData, setEvaluationData] = useState({
    creditHistoryCheck: false,
    employmentStabilityCheck: false,
    minimumBalanceCheck: false,
    savingHistoryCheck: false,
    periodicDepositsCheck: false,
    balanceYearsAgoCheck: false,
    recentWithdrawalsCheck: false,
    currentDebt: 0.0,
  });

  const updateCheck = (checkName) => {
    setEvaluationData((prevData) => ({
      ...prevData,
      [checkName]: true,
    }));
  };

  const calculateCurrentDebt = async (clientID) => {
    try {
      const response = await creditService.getByClientID(clientID);
      const credits = response.data;
  
      // Filtrar créditos que no deben ser contabilizados
      const validCredits = credits.filter(credit => 
        credit.status === "E6_APROBADA" || credit.status === "E9_EN_DESEMBOLSO"
      );
  
      // Calcular la deuda total solo con créditos válidos
      const totalDebt = validCredits.reduce((sum, credit) => sum + credit.monthlyFee, 0);
      
      return totalDebt;
    } catch (error) {
      console.error("Error al calcular currentDebt:", error);
      return 0;
    }
  };
  

  const handleEvaluation = async () => {
    try {
      const response = await checkrulesService.getByCreditID(creditID);
      const checkid = response.data?.id;
      const clientID = response.data?.clientID;
  
      if (!checkid) {
        alert("Error: No se pudo encontrar un checkid válido.");
        return;
      }
  
      const currentDebt = await calculateCurrentDebt(clientID);
      setEvaluationData((prevData) => ({
        ...prevData,
        currentDebt: currentDebt,
      }));
  
      const result = await checkrulesService.creditEvaluation(checkid, {
        ...evaluationData,
        currentDebt: currentDebt,
      });
  
      const checkRulesResponse = await checkrulesService.getById(checkid);
      const creditResponse = await creditService.getByID(creditID);
      const checkRulesEntity = checkRulesResponse.data;
      const creditEntity = creditResponse.data;
  
      let observations = ""; // Para acumular las observaciones
  
      // Verifica las reglas críticas y acumula observaciones para cada regla falsa
      const criticalRules = {
        rule1: "antigüedad laboral y estabilidad",
        rule2: "saldo mínimo requerido",
        rule3: "historial de ahorro consistente",
        rule4: "depósitos periódicos",
        rule6: "retiros recientes",
      };
  
      let criticalRulesPassed = true;
      for (const [rule, description] of Object.entries(criticalRules)) {
        if (!checkRulesEntity[rule]) {
          observations += `Se rechazó la solicitud por no cumplir con la ${description}. `;
          criticalRulesPassed = false;
        }
      }
  
      // Si alguna regla crítica no se cumple, rechaza automáticamente
      if (!criticalRulesPassed) {
        await creditService.updateStatus(creditID, "E7_RECHAZADA");
        await creditService.updateObservations(creditID, observations);
        alert("El crédito ha sido rechazado. Motivos: " + observations);
        return;
      }
  
      // Si todas las reglas críticas son `true`, verifica las opcionales
      const optionalRules = {
        rule71: "requisito opcional 1",
        rule72: "requisito opcional 2",
        rule73: "requisito opcional 3",
        rule74: "requisito opcional 4",
        rule75: "requisito opcional 5",
      };
  
      const optionalRulesPassedCount = Object.entries(optionalRules)
        .filter(([rule, description]) => {
          if (!checkRulesEntity[rule]) {
            observations += `Se rechazó la solicitud por no cumplir con el ${description}. `;
            return false;
          }
          return true;
        })
        .length;
  
      if (optionalRulesPassedCount === 5) {
        // Todas las reglas (críticas y opcionales) son `true`, cambia a `E4_PRE_APROBADA`
        await creditService.updateStatus(creditID, "E4_PRE_APROBADA");
        observations = "";
        await creditService.updateObservations(creditID, observations);
        alert("El crédito ha sido pre-aprobado.");
      } else if (optionalRulesPassedCount >= 3 && optionalRulesPassedCount <= 4) {
        // Si cumple con 3 o 4 de las 5 reglas opcionales, cambia a `E2_PENDIENTE_DOCUMENTACION`
        await creditService.updateStatus(creditID, "E2_PENDIENTE_DOCUMENTACION");
        await creditService.updateObservations(creditID, observations);
        alert("El crédito está pendiente de documentación adicional.");
      } else {
        // Si cumple con menos de 3 de las 5 reglas opcionales, rechaza
        await creditService.updateStatus(creditID, "E7_RECHAZADA");
        await creditService.updateObservations(creditID, observations);
        alert("El crédito ha sido rechazado debido a reglas opcionales insuficientes. Motivos: " + observations);
      }
  
      if (result.success) {
        alert('Evaluación enviada');
      } else {
        alert('Hubo un error en la evaluación');
      }
    } catch (error) {
      console.error("Error en la evaluación de crédito:", error);
      alert('Error al enviar la evaluación');
    }
  };  
  
  return (
    <div className="credit-evaluation-container">
      <h2>Evaluación de Crédito</h2>

      <button className="go-back-button" onClick={() => navigate(-1)}>
        Atrás
      </button>

      <div className="credit-evaluation">
        <div className="rule">
          <p>Historial Crediticio</p>
          <button className="btn-green" onClick={() => updateCheck('creditHistoryCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Antigüedad Laboral y Estabilidad</p>
          <button className="btn-green" onClick={() => updateCheck('employmentStabilityCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Saldo Mínimo Requerido</p>
          <button className="btn-green" onClick={() => updateCheck('minimumBalanceCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Historial de Ahorro Consistente</p>
          <button className="btn-green" onClick={() => updateCheck('savingHistoryCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Depósitos Periódicos</p>
          <button className="btn-green" onClick={() => updateCheck('periodicDepositsCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Relación Saldo/Años Antigüedad</p>
          <button className="btn-green" onClick={() => updateCheck('balanceYearsAgoCheck')}>Cumple</button>
        </div>

        <div className="rule">
          <p>Retiros Recientes</p>
          <button className="btn-green" onClick={() => updateCheck('recentWithdrawalsCheck')}>Cumple</button>
        </div>

        <button onClick={handleEvaluation} className="btn-submit">Enviar Evaluación</button>
      </div>
    </div>
  );
};

export default CreditEvaluationP2;
