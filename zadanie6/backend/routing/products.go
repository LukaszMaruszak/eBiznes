package routing

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"goApp/database"
	"goApp/models"
	"net/http"
)

func GetProducts(c echo.Context) error {
	var products []models.Product
	fmt.Printf("Get products\n")

	if result := database.Database.Find(&products); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, products)
}

func GetProduct(c echo.Context) error {
	id := c.Param("id")
	var product models.Product
	fmt.Printf("Get product with id: " + id + "\n")

	if result := database.Database.First(&product, id); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, product)
}

func SaveProduct(c echo.Context) error {
	product := new(models.Product)

	fmt.Printf("Add new product \n")

	if err := c.Bind(product); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	database.Database.Create(product)

	return c.JSON(http.StatusOK, product)
}
