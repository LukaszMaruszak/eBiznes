import React from 'react';
import GoogleLogin from 'react-google-login';
import GoogleLogout from 'react-google-login';

function Login() {

    const handleLogin = async googleData => {
        const res = await fetch("/api/v1/auth/google", {
            method: "POST",
            body: JSON.stringify({
                token: googleData.tokenId
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
        const data = await res.json()
        // store returned user somehow
    }

    const onLogoutSuccess = () => {
        alert('Wylogowano się!')
    }

    const onFailure = (res) => {
        console.log(res);
    }

    return (
        <div style={{margin: '20% 30%'}}>
            <GoogleLogin
                clientId={process.env.REACT_APP_CLIENT_ID}
                buttonText="Log in with Google"
                onSuccess={handleLogin}
                onFailure={onFailure}
                cookiePolicy={'single_host_origin'}
            />

            {/*<GoogleLogout*/}
            {/*    clientId={process.env.REACT_APP_CLIENT_ID}*/}
            {/*    buttonText="Logout"*/}
            {/*    onLogoutSuccess={onLogoutSuccess}>*/}
            {/*</GoogleLogout>*/}
        </div>

    );
}






export default Login;
