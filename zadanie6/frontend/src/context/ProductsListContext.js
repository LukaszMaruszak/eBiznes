import {useState, createContext, useEffect} from "react";
import {productsService} from "../service/ProductsService";

export const ProductListContext = createContext({
    productsList: [],
});

export const ProductListContextProvider = ({children}) => {
    const [productsList, setProductsList] = useState([]);

    useEffect(() => {
        productsService.getProducts().then((productsListData) => setProductsList(productsListData));
    }, [])

    return (
        <ProductListContext.Provider value={{
            productsList,
        }}>
            {children}
        </ProductListContext.Provider>
    )
}

