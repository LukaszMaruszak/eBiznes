package routing

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"goApp/database"
	"goApp/models"
	"net/http"
)

func GetCategories(c echo.Context) error {
	var categoryList []models.Category
	fmt.Printf("Get category list\n")

	if result := database.Database.Find(&categoryList); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, categoryList)
}

func GetCategory(c echo.Context) error {
	id := c.Param("id")
	var category models.Category
	fmt.Printf("Get category with id: " + id + "\n")

	if result := database.Database.First(&category, id); result.Error != nil {
		return c.String(http.StatusNotFound, "Database Error")
	}

	return c.JSON(http.StatusOK, category)
}

func SaveCategory(c echo.Context) error {
	category := new(models.Category)

	fmt.Printf("Add new category \n")

	if err := c.Bind(category); err != nil {
		return c.String(http.StatusBadRequest, "Bad user "+err.Error())
	}

	database.Database.Create(category)

	return c.JSON(http.StatusOK, category)
}
