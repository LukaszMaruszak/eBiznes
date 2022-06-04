package routing

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"myapp/database"
	"myapp/models"
	"net/http"
)

func GetPayment(c echo.Context) error {
	id := c.Param("id")
	var payment models.Payment
	fmt.Printf("Get Payment with id: " + id + "\n")

	if result := database.Database.First(&payment, id); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, payment)
}

func SavePayment(c echo.Context) error {
	payment := new(models.Payment)

	fmt.Printf("Add new payment \n")

	if err := c.Bind(payment); err != nil {
		return c.String(http.StatusBadRequest, "Bad payment "+err.Error())
	}

	database.Database.Create(payment)

	return c.JSON(http.StatusOK, payment)
}
