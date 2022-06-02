import './App.scss';
import Navbar from "./components/Navbar/Navbar";
import ProductsList from "./components/ProductsList/ProductsList";
import ShoppingCart from "./components/ShoppingCart/ShoppingCart";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Payment from "./components/Payment/Payment";
import {ShoppingCardContextProvider} from "./context/ShoppingCardContext";
import {ProductListContextProvider} from "./context/ProductsListContext";
import Login from "./components/Login/Login";
import {LoginSuccess} from "./components/Login/LoginSuccess";

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

                      <Route path="/login" element={<Login />}/>
                      <Route exact path="/login/success" element={<LoginSuccess />}/>

                      <Route path="/" element={<ProductsList/>}/>
                  </Routes>
              </Router>
          </ShoppingCardContextProvider>
      </ProductListContextProvider>
  );
}

export default App;
