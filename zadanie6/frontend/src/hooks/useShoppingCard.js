import {useState} from "react";

function useShoppingCard() {
    const [products, setProducts] = useState([]);

    const addProductToBasket = (newProduct) => {
        let productInBasket = products.findIndex((product) => product.id === newProduct.id);
        if(productInBasket) {
            products[productInBasket].quantity = products[productInBasket].quantity++;
        } else {
            setProducts([...products, newProduct])
        }
    }

    return {
        products,
        addProductToBasket
    }
}

export default useShoppingCard;
