import React from 'react';
// import GoogleLogin from 'react-google-login';
import GoogleButton from 'react-google-button';
import axios from "../../config/AxiosConfig";

function Login() {

    const fetchAuthUser = async () => {
        const respone = await axios.get('/auth/user')
    }

    const redirectToGoogleSSO = async () => {
        axios.get("/google/login").then((googleLoginURL) =>{
            console.log(googleLoginURL.data)
            const newWindow = window.open(googleLoginURL.data, "_blank", "width=500, height=600");
        });
    }

    return (
        <div style={{margin: '20% 30%'}}>
            <GoogleButton onClick={redirectToGoogleSSO}/>

            {/*<GoogleLogout*/}
            {/*    clientId={process.env.REACT_APP_CLIENT_ID}*/}
            {/*    buttonText="Logout"*/}
            {/*    onLogoutSuccess={onLogoutSuccess}>*/}
            {/*</GoogleLogout>*/}
        </div>

    );
}






export default Login;
