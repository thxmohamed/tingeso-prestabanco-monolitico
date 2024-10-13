import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/file/')
}

const save = data => {
    return httpClient.post("/file/save", data)
}

const getByCreditID = id => {
    return httpClient.get(`/file/${id}`)
}

export default {getAll, save, getByCreditID}