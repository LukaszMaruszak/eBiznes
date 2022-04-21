package main

import (
	"goApp/routing"
	_ "goApp/routing"
	"net/http"

	"github.com/labstack/echo/v4"
)

func main() {
	e := echo.New()
	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")
	})

	e.POST("/products", routing.SaveProduct)
	e.GET("/products/:id", routing.GetProduct)
	e.GET("/products", routing.GetProducts)

	e.POST("/category", routing.SaveCategory)
	e.GET("/category/:id", routing.GetCategory)
	e.GET("/category", routing.GetCategories)

	e.POST("/basket/:id", routing.AddItemToBasket)
	e.GET("/basket", routing.GetItemsInBasket)
	e.DELETE("/basket/:id", routing.DeleteItemInBasket)

	e.POST("/payment", routing.SavePayment)
	e.GET("/payment/:id", routing.GetPayment)

	e.Logger.Fatal(e.Start(":1323"))
}
