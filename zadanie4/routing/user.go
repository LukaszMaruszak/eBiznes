package routing

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type User struct {
	Id        string `json:"id" form:"id" query:"id"`
	Name      string `json:"name" form:"name" query:"name"`
	Password  string `json:"password" form:"password" query:"password"`
	Address   string `json:"address" form:"address" query:"address"`
	Telephone int    `json:"telephone" form:"telephone" query:"telephone"`
	Email     string `json:"email" form:"email" query:"email"`
}

func GetUser(c echo.Context) error {
	id := c.Param("id")
	return c.String(http.StatusOK, "Category with id: "+id)
}

func SaveUser(c echo.Context) error {
	user := new(User)
	if err := c.Bind(user); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	return c.JSON(http.StatusOK, user)
}
