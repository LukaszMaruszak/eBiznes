import './App.scss';
import Navbar from "./components/Navbar/Navbar";
import ProductsList from "./components/ProductsList/ProductsList";
import ShoppingCart from "./components/ShoppingCart/ShoppingCart";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Payment from "./components/Payment/Payment";
import {ShoppingCardContextProvider} from "./context/ShoppingCardContext";
import {ProductListContextProvider} from "./context/ProductsListContext";

function App() {
  return (
      <ProductListContextProvider>
          <ShoppingCardContextProvider>
              <Router>
                  <Navbar/>
                  <Routes>
                      <Route path="/products" element={<ProductsList/>}/>

                      <Route path="/shopping-cart" element={<ShoppingCart />}/>

                      <Route path="/payment" element={<Payment />}/>

                      <Route path="/" element={<ProductsList/>}/>
                  </Routes>
              </Router>
          </ShoppingCardContextProvider>
      </ProductListContextProvider>
  );
}

export default App;
