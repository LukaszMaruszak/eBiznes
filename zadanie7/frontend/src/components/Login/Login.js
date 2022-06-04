import React from 'react';
import { GithubLoginButton, GoogleLoginButton } from "react-social-login-buttons";
import axios from "../../config/AxiosConfig";

function Login() {

    const redirectToGoogleSSO = async () => {
        axios.get("/google/login").then((googleLoginURL) => {
            console.log(googleLoginURL.data);
            window.open(googleLoginURL.data, "_self");
        });
    }

    const redirectToGitHubSSO = async () => {
        axios.get("/github/login").then((githubLoginURL) => {
            console.log(githubLoginURL.data);
            window.open(githubLoginURL.data, "_self");
        });
    }

    return (
        <div style={{margin: '20% 30%'}}>
            <GoogleLoginButton onClick={redirectToGoogleSSO}/>

            <GithubLoginButton onClick={redirectToGitHubSSO}/>
        </div>

    );
}






export default Login;
