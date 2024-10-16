import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/credit/')
}

const save = data => {
    return httpClient.post("/credit/save", data)
}

const getByClientID = id => {
    return httpClient.get(`/credit/${id}`)
}

const simulate = (data) => {
    return httpClient.post("/credit/simulate", data);
};

export default {getAll, save, getByClientID, simulate}
