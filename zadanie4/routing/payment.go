package routing

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type Payment struct {
	Id         int    `json:"id" form:"id" query:"id"`
	FirstName  string `json:"firstName" form:"firstName" query:"firstName"`
	LastName   string `json:"lastName" form:"lastName" query:"lastName"`
	CardNumber string `json:"cardNumber" form:"cardNumber" query:"cardNumber"`
	Value      string `json:"value" form:"value" query:"value"`
	Date       string `json:"date" form:"date" query:"date"`
}

func GetPayment(c echo.Context) error {
	return c.String(http.StatusOK, "SavePayment list")
}

func SavePayment(c echo.Context) error {
	payment := new(Payment)
	if err := c.Bind(payment); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	return c.JSON(http.StatusOK, payment)
}
