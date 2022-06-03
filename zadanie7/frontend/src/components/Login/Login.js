import React from 'react';
import { GithubLoginButton } from "react-social-login-buttons";
import { GoogleLoginButton } from "react-social-login-buttons";
import axios from "../../config/AxiosConfig";

function Login() {

    const redirectToGoogleSSO = async () => {
        axios.get("/google/login").then((googleLoginURL) => {
            console.log(googleLoginURL.data)
            const newWindow = window.open(googleLoginURL.data, "_self");
        });
    }

    const redirectToGitHubSSO = async () => {
        axios.get("/github/login").then((githubLoginURL) => {
            console.log(githubLoginURL.data)
            const newWindow = window.open(githubLoginURL.data, "_self");
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
