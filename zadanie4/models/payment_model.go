package models

import "gorm.io/gorm"

type Payment struct {
	gorm.Model
	Firstname  string
	Surname    string
	CardNumber string
	Value      string
	Status     string
}
