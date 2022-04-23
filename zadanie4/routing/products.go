package routing

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type Product struct {
	Id       int    `json:"id" form:"id" query:"id"`
	Name     string `json:"name" form:"name" query:"name"`
	Price    string `json:"price" form:"price" query:"price"`
	Category string `json:"category" form:"category" query:"category"`
}

func GetProducts(c echo.Context) error {
	return c.String(http.StatusOK, "Products list")
}

func GetProduct(c echo.Context) error {
	id := c.Param("id")
	return c.String(http.StatusOK, "Products with id: "+id)
}

func SaveProduct(c echo.Context) error {
	product := new(Product)
	if err := c.Bind(product); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	return c.JSON(http.StatusOK, product)
}
