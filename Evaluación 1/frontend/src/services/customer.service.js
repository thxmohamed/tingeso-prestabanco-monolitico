import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/customer/')
}

const register = data => {
    return httpClient.post("/customer/save", data)
}

const get = id => {
    return httpClient.get(`/customer/${id}`)
}

export default {getAll, register, get}