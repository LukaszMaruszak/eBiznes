package routing

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"goApp/database"
	"goApp/models"
	"net/http"
)

func GetUser(c echo.Context) error {
	id := c.Param("id")
	var user models.User

	fmt.Printf("Get user with id: " + id + "\n")

	if result := database.Database.First(&user, id); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, user)
}

func SaveUser(c echo.Context) error {
	user := new(models.User)

	fmt.Printf("Add new user \n")

	if err := c.Bind(user); err != nil {
		return c.String(http.StatusBadRequest, "Bad user "+err.Error())
	}

	database.Database.Create(user)

	return c.JSON(http.StatusOK, user)
}
