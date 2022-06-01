import axios from '../config/AxiosConfig';

function savePayment(payment) {
    return axios.post("/payment", payment).then((res) =>{
        console.log(res.data);
        return res.data;
    });
}

export const paymentService = {
    savePayment,
}

