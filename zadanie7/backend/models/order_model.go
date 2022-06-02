package models

import "gorm.io/gorm"

type Order struct {
	gorm.Model
	OrderNumber int
	ProductID   int
	Product     Product
	Quantity    int
	UserID      int
	User        User
}
