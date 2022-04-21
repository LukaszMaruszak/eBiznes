package routing

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type BasketItem struct {
	Id        int `json:"id" form:"id" query:"id"`
	ProductId int `json:"productId" form:"productId" query:"productId"`
	Quantity  int `json:"quantity" form:"quantity" query:"quantity"`
}

type Basket struct {
	Id          int          `json:"id" form:"id" query:"id"`
	BasketItems []BasketItem `json:"basketItems" form:"basketItems" query:"basketItems"`
}

func GetItemsInBasket(c echo.Context) error {
	return c.String(http.StatusOK, "Basket itmes list")
}

func DeleteItemInBasket(c echo.Context) error {
	id := c.Param("id")
	return c.String(http.StatusOK, "Delete item with id: "+id)
}

func AddItemToBasket(c echo.Context) error {
	basketItem := new(BasketItem)
	if err := c.Bind(basketItem); err != nil {
		return c.String(http.StatusBadRequest, "Bad product"+err.Error())
	}

	return c.JSON(http.StatusOK, basketItem)
}
