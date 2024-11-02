import httpClient from "../http-common";

const createEvaluation = data => {
    return httpClient.post("/checkrules/", data);
};

const getAll = () => {
    return httpClient.get("/checkrules/");
};

const getById = id => {
    return httpClient.get(`/checkrules/${id}`);
};

const getByCreditID = creditID => {
    return httpClient.get(`/checkrules/credit/${creditID}`)
}

const creditEvaluation = async (checkId, evaluationData) => {
    try {
        await httpClient.post(`/checkrules/check1/${checkId}`);
        await httpClient.post(`/checkrules/check2/${checkId}/${evaluationData.creditHistoryCheck}`);
        await httpClient.post(`/checkrules/check3/${checkId}/${evaluationData.employmentStabilityCheck}`);
        await httpClient.post(`/checkrules/check4/${checkId}/${evaluationData.currentDebt}`);
        await httpClient.post(`/checkrules/check6/${checkId}`);
        await httpClient.post(`/checkrules/check71/${checkId}/${evaluationData.minimumBalanceCheck}`);
        await httpClient.post(`/checkrules/check72/${checkId}/${evaluationData.savingHistoryCheck}`);
        await httpClient.post(`/checkrules/check73/${checkId}/${evaluationData.periodicDepositsCheck}`);
        await httpClient.post(`/checkrules/check74/${checkId}/${evaluationData.balanceYearsAgoCheck}`);
        await httpClient.post(`/checkrules/check75/${checkId}/${evaluationData.recentWithdrawalsCheck}`);

        return { success: true };
    } catch (error) {
        console.error("Error en la evaluación de crédito:", error);
        return { success: false, error: error.response.data };
    }
};


export default {
    createEvaluation,
    getAll,
    getById,
    creditEvaluation,
    getByCreditID
};