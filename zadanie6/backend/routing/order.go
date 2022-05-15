package routing

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"goApp/database"
	"goApp/models"
	"net/http"
)

func GetItemsInOrder(c echo.Context) error {
	id := c.Param("id")
	var order []models.Order
	fmt.Printf("Get orders with order_number: " + id + "\n")

	if result := database.Database.Where("order_number", id).Find(&order); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, order)
}

func AddItemToOrder(c echo.Context) error {
	orderDetails := new(models.Order)

	fmt.Printf("Add new order details \n")

	if err := c.Bind(orderDetails); err != nil {
		return c.String(http.StatusBadRequest, "Bad order details "+err.Error())
	}

	database.Database.Create(orderDetails)

	return c.JSON(http.StatusOK, orderDetails)
}
