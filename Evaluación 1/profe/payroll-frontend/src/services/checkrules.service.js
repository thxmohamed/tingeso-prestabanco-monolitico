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

const checkRelationQuotaIncome = (credit, income) => {
    return httpClient.post(`/checkrules/check1/${income}`, credit);
};

const checkCreditHistory = (checkId, request) => {
    return httpClient.post(`/checkrules/check2/${checkId}/${request}`);
};

const checkEmploymentStability = (checkId, request) => {
    return httpClient.post(`/checkrules/check3/${checkId}/${request}`);
};

const checkDebtIncome = (checkId, currentDebt) => {
    return httpClient.post(`/checkrules/check4/${checkId}/${currentDebt}`);
};

const checkApplicantAge = checkId => {
    return httpClient.post(`/checkrules/check6/${checkId}`);
};

const checkMinimumBalance = (checkId, check) => {
    return httpClient.post(`/checkrules/check71/${checkId}/${check}`);
};

const checkSavingHistory = (checkId, check) => {
    return httpClient.post(`/checkrules/check72/${checkId}/${check}`);
};

const checkPeriodicDeposits = (checkId, check) => {
    return httpClient.post(`/checkrules/check73/${checkId}/${check}`);
};

const checkBalanceYearsAgo = (checkId, check) => {
    return httpClient.post(`/checkrules/check74/${checkId}/${check}`);
};

const checkRecentWithdrawals = (checkId, check) => {
    return httpClient.post(`/checkrules/check75/${checkId}/${check}`);
};

export default {
    createEvaluation,
    getAll,
    getById,
    checkRelationQuotaIncome,
    checkCreditHistory,
    checkEmploymentStability,
    checkDebtIncome,
    checkApplicantAge,
    checkMinimumBalance,
    checkSavingHistory,
    checkPeriodicDeposits,
    checkBalanceYearsAgo,
    checkRecentWithdrawals
};