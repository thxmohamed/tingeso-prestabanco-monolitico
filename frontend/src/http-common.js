import axios from "axios";

const prestaBancoServer = "prestabanco-mohamed.westus2.cloudapp.azure.com";
//const prestaBancoServer = "localhost:80";

export default axios.create({
    baseURL: `http://${prestaBancoServer}`,
    headers: {
        'Content-Type': 'application/json'
    } 
});
