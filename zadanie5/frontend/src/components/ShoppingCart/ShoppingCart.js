import './ShoppingCart.scss';
import * as React from 'react';
import {Button, Grid, Icon, IconButton, Stack, TextField} from "@mui/material";
import PaymentIcon from '@mui/icons-material/Payment';
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {Link} from "react-router-dom";
import {ShoppingCardContext} from "../../context/ShoppingCardContext";

function ShoppingCart() {
    const navigate = useNavigate();

    const icons = ["computer", "keyboard", "laptop_mac", "mouse", "tv", "phone_iphone","headphones","phone_android", "videogame_asset"]

    const {products, addProductToShoppingCard, removeProductFromShoppingCard} = useContext(ShoppingCardContext)

    function handleSubmitForm() {
        console.log("Button Clicked");
            navigate("/payment", { state: {
                    value: sumAmount(),
                }
            });
    }

    function handleChangeQuantity(product, e) {
        console.log("Value change " , product, e.target.value)

        if(product.quantity > Number(e.target.value)) {
            removeProductFromShoppingCard(product)
        } else {
            addProductToShoppingCard(product)
        }
    }

    function sumAmount() {
        return products.reduce((sum, {price, quantity} ) => {
            return sum + (quantity * price)
        }, 0)
    }

    return (
        <div className="shopping-cart">
            <h2>Zamówienie</h2>
            <div className="shopping-cart__list">
                {products.length === 0 ? <p style={{alignSelf: "center"}}>Koszyk jest pusty</p> : products.map((product, index) => (
                    <div className="shopping-cart__list-item" key={index}>
                        <div className="products-list__item-icon">
                            <Icon sx={{ fontSize: 100, color: product.color }}>{product.icon}</Icon>
                        </div>
                        <h4>
                            {product.name}
                        </h4>

                        <div className="details-price">
                            <p>
                                {product.price} zł
                            </p>
                        </div>
                        <TextField
                            id="quantity-number"
                            label="Quantity"
                            type="number"
                            variant="standard"
                            InputProps={{ inputProps: { min: 0, max: 10 } }}
                            value={product.quantity}
                            onChange={(e) => handleChangeQuantity(product,e)}
                        />
                    </div>
                ))}
            </div>

            <div  className="shopping-cart__order-summary">
                <h3>Razem: {sumAmount()} zł</h3>
            </div>

            <div className="shopping-cart__order-button">
                <Stack direction="row" spacing={2}>
                    <Button
                        variant="contained"
                        endIcon={<PaymentIcon />}
                        disabled={!products.length}
                        onClick={() => handleSubmitForm()}>
                        Zapłać
                    </Button>
                </Stack>
            </div>
        </div>
    );
}

export default ShoppingCart;
