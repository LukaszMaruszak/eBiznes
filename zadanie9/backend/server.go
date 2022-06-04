package main

import (
	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"myapp/database"
	"myapp/routing"
	"net/http"
)

func main() {
	database.InitDataBase()

	err := godotenv.Load(".env")
	if err != nil {
		print("Error loading .env file")
	}

	e := echo.New()
	e.Use(middleware.CORS())

	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")
	})

	e.GET("/google/callback", routing.GoogleCallback)
	e.GET("/google/login", routing.GoogleLogin)

	e.GET("/github/callback", routing.GithubCallback)
	e.GET("/github/login", routing.GithubLogin)

	e.POST("/products", routing.SaveProduct)
	e.GET("/products/:id", routing.GetProduct)
	e.GET("/products", routing.GetProducts)

	e.POST("/category", routing.SaveCategory)
	e.GET("/category/:id", routing.GetCategory)
	e.GET("/category", routing.GetCategories)

	e.POST("/order", routing.AddItemToOrder)
	e.GET("/order/:id", routing.GetItemsInOrder)

	e.POST("/payment", routing.SavePayment)
	e.GET("/payment/:id", routing.GetPayment)

	e.POST("/user", routing.SaveUser)
	e.GET("/user/:id", routing.GetUser)

	e.Logger.Fatal(e.Start(":1323"))
}
