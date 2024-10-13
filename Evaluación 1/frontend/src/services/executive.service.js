import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/executive/')
}

const register = data => {
    return httpClient.post("/executive/register", data)
}

const get = id => {
    return httpClient.get(`/executive/${id}`)
}

export default {getAll, register, get}
