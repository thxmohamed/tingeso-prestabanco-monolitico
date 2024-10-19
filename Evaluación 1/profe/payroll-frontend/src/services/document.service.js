import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/file/')
}

const save = (data) => {
    return httpClient.post("/file/save", data, {
        headers: {
            "Content-Type": "multipart/form-data" // Asegura que el contenido es de tipo FormData
        }
    });
}

const getByCreditID = id => {
    return httpClient.get(`/file/${id}`)
}

export default {getAll, save, getByCreditID}