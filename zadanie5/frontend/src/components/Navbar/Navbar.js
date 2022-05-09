import './Navbar.scss';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {IconButton, Typography} from "@mui/material";
import {Link} from "react-router-dom";

function Navbar() {
    return (
        <div className="navbar">
            <Typography>
                <Link className="navbar__logo" to="/"> The Gadget Zone </Link>
            </Typography>

                <div>
                    <IconButton aria-label="delete" color="primary" component={Link} to="/shopping-cart">
                        <ShoppingCartIcon />
                    </IconButton>
                </div>
        </div>
    );
}

export default Navbar;
