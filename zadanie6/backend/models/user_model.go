package models

import "gorm.io/gorm"

type User struct {
	gorm.Model
	Name      string
	Password  string
	Address   string
	Telephone string
	Email     string
}
