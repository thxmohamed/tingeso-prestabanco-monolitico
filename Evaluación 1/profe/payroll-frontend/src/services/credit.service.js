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

const updateStatus = (id, newStatus) => {
    return httpClient.put(`/credit/${id}/status`, newStatus);
};


const deleteById = id => {
    return httpClient.delete(`/credit/${id}`)
}

export default {getAll, save, getByClientID, simulate, deleteById, updateStatus}
