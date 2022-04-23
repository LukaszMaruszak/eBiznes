package routing

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type Category struct {
	Id   string `json:"id" form:"id" query:"id"`
	Name string `json:"name" form:"name" query:"name"`
}

func GetCategories(c echo.Context) error {
	return c.String(http.StatusOK, "Category list")
}

func GetCategory(c echo.Context) error {
	id := c.Param("id")
	return c.String(http.StatusOK, "Category with id: "+id)
}

func SaveCategory(c echo.Context) error {
	category := new(Category)
	if err := c.Bind(category); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	return c.JSON(http.StatusOK, category)
}
