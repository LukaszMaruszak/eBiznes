package models

import "gorm.io/gorm"

type Payment struct {
	gorm.Model
	CardholderName string `json:"cardholderName"`
	CardNumber     string `json:"cardNumber"`
	ExpiryDate     string `json:"expiryDate"`
	Ccv            string `json:"ccv"`
	Value          int    `json:"value"`
}
