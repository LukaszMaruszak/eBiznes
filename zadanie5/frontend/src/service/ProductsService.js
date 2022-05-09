import axios from '../config/AxiosConfig';

function getProducts() {
    return axios.get("/products").then((res) =>{
        console.log(res.data);
        return res.data;
    });
}

export const productsService = {
    getProducts,
}

