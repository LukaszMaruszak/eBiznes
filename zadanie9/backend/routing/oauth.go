package routing

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/labstack/echo/v4"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/github"
	"golang.org/x/oauth2/google"
	"io/ioutil"
	"math/rand"
	"myapp/controllers"
	"myapp/models"
	"net/http"
	"os"
	"time"
)

var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

func randSeq(n int) string {
	b := make([]rune, n)
	for i := range b {
		b[i] = letters[rand.Intn(len(letters))]
	}
	return string(b)
}

func GenerateTokensAndSetCookies(c echo.Context) error {
	accessToken := generateAccessToken()
	print(accessToken)
	setTokenCookie(accessToken, c)

	return nil
}

func setTokenCookie(token string, c echo.Context) {
	cookie := new(http.Cookie)
	cookie.Name = "token"
	cookie.Value = token
	cookie.Expires = time.Now().Add(1 * time.Hour)
	c.SetCookie(cookie)
	print("Cookie saved")
}

func generateAccessToken() string {
	tokenString := randSeq(10)

	return tokenString
}

func getGoogleConf() *oauth2.Config {
	conf := &oauth2.Config{
		ClientID:     os.Getenv("CLIENT_ID"),
		ClientSecret: os.Getenv("CLIENT_SECRET"),
		RedirectURL:  "http://localhost:1323/google/callback",
		Scopes: []string{
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile",
		},
		Endpoint: google.Endpoint,
	}

	return conf
}

func getGithubConf() *oauth2.Config {
	conf := &oauth2.Config{
		ClientID:     os.Getenv("GITHUB_CLIENT_ID"),
		ClientSecret: os.Getenv("GITHUB_CLIENT_SECRET"),
		RedirectURL:  "http://localhost:1323/github/callback",
		Scopes: []string{
			"user:email",
			"read:user",
		},
		Endpoint: github.Endpoint,
	}

	return conf
}

func GoogleLogin(c echo.Context) error {
	url := getGoogleConf().AuthCodeURL("state")
	//fmt.Printf("Visit the URL for the auth dialog: %v", url)
	return c.JSON(http.StatusOK, url)
}

func GoogleCallback(c echo.Context) error {

	token, err := getGoogleConf().Exchange(context.Background(), c.QueryParam("code"))

	if err != nil {
		print(err)
	}

	response, err := http.Get("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token.AccessToken)

	if err != nil {
		print(err)
	}
	defer response.Body.Close()

	userInfo, err := ioutil.ReadAll(response.Body)

	if err != nil {
		print(err)
	}

	var user models.OauthUser
	err = json.Unmarshal(userInfo, &user)

	if err != nil {
		print(err)
	}

	if !controllers.FindUserInDB(user.Email) {
		controllers.AddUser(user, token.AccessToken)
	}

	userInDB := controllers.GetUserFromDB(user.Email)

	c.Redirect(http.StatusFound, "http://localhost:3000/login/success")

	errToken := GenerateTokensAndSetCookies(c)
	if errToken != nil {
		return errToken
	}

	return c.JSON(http.StatusOK, userInDB)
}

func GithubLogin(c echo.Context) error {
	url := getGithubConf().AuthCodeURL("state")
	//fmt.Printf("Visit the URL for the auth dialog: %v", url)
	return c.JSON(http.StatusOK, url)
}

func GithubCallback(c echo.Context) error {

	token, err := getGithubConf().Exchange(context.Background(), c.QueryParam("code"))

	if err != nil {
		print(err)
	}

	req, err := http.NewRequest("GET", "https://api.github.com/user", nil)
	if err != nil {
		return err
	}

	req.Header.Add("Accept", "application/vnd.github.v3+json")
	req.Header.Add("Authorization", "token "+token.AccessToken)
	response, err := http.DefaultClient.Do(req)
	if err != nil {
		print(err)
	}

	defer response.Body.Close()

	userInfo, err := ioutil.ReadAll(response.Body)
	str := string(userInfo)

	if err != nil {
		print(err)
	}

	u := struct {
		ID       int    `json:"id"`
		Email    string `json:"email"`
		Bio      string `json:"bio"`
		Name     string `json:"name"`
		Login    string `json:"login"`
		Picture  string `json:"avatar_url"`
		Location string `json:"location"`
	}{}

	json.Unmarshal([]byte(str), &u)

	fmt.Printf("%+v\n", u)

	if err != nil {
		print(err)
	}

	var user models.OauthUser
	user.Name = u.Name
	user.FamilyName = u.Login
	user.Picture = u.Picture
	user.Email = u.Email

	if !controllers.FindUserInDB(user.Email) {
		controllers.AddUser(user, token.AccessToken)
	}

	userInDB := controllers.GetUserFromDB(user.Email)

	c.Redirect(http.StatusFound, "http://localhost:3000/login/success")

	errToken := GenerateTokensAndSetCookies(c)
	if errToken != nil {
		return errToken
	}

	return c.JSON(http.StatusOK, userInDB)
}
