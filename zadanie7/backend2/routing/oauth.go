package routing

import (
	"context"
	"encoding/json"
	"github.com/labstack/echo/v4"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"io/ioutil"
	"myapp/controllers"
	"myapp/models"
	"net/http"
	"os"
)

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

	return c.JSON(http.StatusOK, userInDB)
}

func GoogleLogin(c echo.Context) error {
	url := getGoogleConf().AuthCodeURL("state")
	//fmt.Printf("Visit the URL for the auth dialog: %v", url)
	return c.JSON(http.StatusOK, url)
}

func AuthUser(c echo.Context) error {
	return c.JSON(http.StatusOK, "User")
}
