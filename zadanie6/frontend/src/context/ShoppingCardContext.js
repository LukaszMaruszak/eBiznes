import {useState, createContext} from "react";

export const ShoppingCardContext = createContext({
    products: [],
    addProductToShoppingCard: () => {},
    removeProductFromShoppingCard: () => {},
});

export const ShoppingCardContextProvider = ({children}) => {
    const [products, setProducts] = useState([]);

    const addProductToShoppingCard = (newProduct) => {
        let productInBasket = products.findIndex((product) => product.ID === newProduct.ID);
        if(productInBasket !== -1) {
            products[productInBasket].quantity = products[productInBasket].quantity + 1;
            setProducts([...products])
        } else {
            setProducts([...products, {...newProduct, quantity: 1}])
        }
    }

    const removeProductFromShoppingCard = (product) => {
        let updatedProducts = products.map((item) => {
            if(item.ID === product.ID) {
                item.quantity = item.quantity - 1;
            }

            return item;
        })

        setProducts(checkZeroQuantity(updatedProducts));
    }

    function checkZeroQuantity(products) {
        return products.filter((product) => product.quantity !== 0);
    }

    return (
        <ShoppingCardContext.Provider value={{
            products,
            addProductToShoppingCard,
            removeProductFromShoppingCard,
        }}>
            {children}
        </ShoppingCardContext.Provider>
    )
}

