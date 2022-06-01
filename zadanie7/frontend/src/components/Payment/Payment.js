import './Payment.scss';
import * as React from 'react';
import {Grid, FormControl, TextField, Stack, Button} from "@mui/material";
import SendIcon from '@mui/icons-material/Send';
import {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import ValidPayment from "../ValidPayment/ValidPayment";
import {paymentService} from "../../service/PaymentService";

function Payment() {

    const location = useLocation();

    let paymentDetails = {
        cardholderName: '',
        cardNumber: '',
        expiryDate: '',
        ccv: '',
    }

    const [formValues, setFormValues] = useState(paymentDetails);
    const [paymentValue] = useState(location.state.value);
    const [isSubmit, setIsSubmit] = useState(false);
    const [formErrors, setFormErrors] = useState({});
    const [validPayment, setValidPayment] = useState(false);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormValues({...formValues, [name]: value});
    }

    function handleSubmitForm() {
        setFormErrors(validate(formValues));
        setIsSubmit(true);
    }

    useEffect(() => {
        if (Object.keys(formErrors).length === 0 && isSubmit) {
            paymentService.savePayment({...formValues, value: paymentValue}).then(
                (paymentStatus) => {
                    console.log(paymentStatus);
                    setValidPayment(true);
                    setIsSubmit(false);
                },
                () => {
                    console.error("Błąd płatności")
                    alert("Błąd płatności")
                    setIsSubmit(false);
                });
        }
    }, [formErrors, paymentValue, isSubmit, formValues, validPayment]);

    const validate = (values) => {
        const errors = {};

        if(!values.cardholderName) {
            errors.cardholderName = "Podaj imię i nazwisko posiadacza karty!";
        }

        if(!values.cardNumber) {
            errors.cardNumber = "Podaj numer karty";
        }

        if(!values.expiryDate) {
            errors.expiryDate = "Podaj datę";
        }

        if(!values.ccv) {
            errors.ccv = "Podaj kod CCV";
        }

        return errors;
    }

    return (
        <div>
        {
            validPayment ? <ValidPayment /> :
                <div className="payment">
                <h2>Płatność</h2>
                <div className="payment__form">
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <FormControl fullWidth>
                                <TextField
                                    autoFocus
                                    required
                                    fullWidth
                                    margin="dense"
                                    id="cardholderName"
                                    name="cardholderName"
                                    label="Imię i Nazwisko"
                                    type="text"
                                    variant="standard"
                                    value={formValues.cardholderName}
                                    onChange={handleChange}
                                    error={!!formErrors.cardholderName}
                                    helperText={formErrors.cardholderName}
                                />
                            </FormControl>
                        </Grid>

                        <Grid item xs={12}>
                            <FormControl fullWidth>
                                <TextField
                                    fullWidth
                                    required
                                    margin="dense"
                                    id="cardNumber"
                                    name="cardNumber"
                                    label="Numer karty"
                                    type="text"
                                    variant="standard"
                                    value={formValues.cardNumber}
                                    onChange={handleChange}
                                    error={!!formErrors.cardNumber}
                                    helperText={formErrors.cardNumber}
                                />
                            </FormControl>
                        </Grid>

                        <Grid item xs={12} sm={6}>
                            <FormControl fullWidth>
                                <TextField
                                    fullWidth
                                    required
                                    margin="dense"
                                    id="expiryDate"
                                    name="expiryDate"
                                    label="Data ważności"
                                    type="text"
                                    variant="standard"
                                    value={formValues.expiryDate}
                                    onChange={handleChange}
                                    error={!!formErrors.expiryDate}
                                    helperText={formErrors.expiryDate}
                                />
                            </FormControl>
                        </Grid>

                        <Grid item xs={12} sm={6}>
                            <FormControl fullWidth>
                                <TextField
                                    fullWidth
                                    required
                                    margin="dense"
                                    id="ccv"
                                    name="ccv"
                                    label="Kod CVV/CVC"
                                    type="number"
                                    variant="standard"
                                    value={formValues.ccv}
                                    onChange={handleChange}
                                    error={!!formErrors.ccv}
                                    helperText={formErrors.ccv}
                                />
                            </FormControl>
                        </Grid>
                    </Grid>
                </div>

                <div className="payment__order-button">
                    <Stack direction="row" spacing={2}>
                        <Button variant="contained" endIcon={<SendIcon />} onClick={() => handleSubmitForm()}>
                            Zapłać i zamów
                        </Button>
                    </Stack>
                </div>
            </div>
        }
    </div>
    );
}

export default Payment;
