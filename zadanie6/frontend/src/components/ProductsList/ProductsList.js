import './ProductsList.scss';
import * as React from 'react';
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import {Icon, IconButton} from "@mui/material";
import {ShoppingCardContext} from "../../context/ShoppingCardContext";
import {useContext} from "react";
import {ProductListContext} from "../../context/ProductsListContext";

function ProductsList() {
    const {addProductToShoppingCard} = useContext(ShoppingCardContext)
    const { productsList }= useContext(ProductListContext)

    function addToShoppingCard(product) {
        addProductToShoppingCard(product)
        console.log("Add to Shopping Card - ", product)
    }

    return (
        <div className="products-list">
            {productsList.map((product, index) => (
                <div className="products-list__item" key={index}>
                    <div className="products-list__item-icon">
                        <Icon sx={{ fontSize: 100, color: product.color }}>{product.icon}</Icon>
                    </div>
                    <div className="products-list__item-details">
                        <h4>
                            {product.name}
                        </h4>

                        <div className="details-price">
                            <p>
                                {product.price} zł
                            </p>
                            <IconButton className="add-product-button" aria-label="delete" color="primary" onClick={() => addToShoppingCard(product)}>
                                <AddCircleOutlineOutlinedIcon color="primary" />
                            </IconButton>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default ProductsList;
