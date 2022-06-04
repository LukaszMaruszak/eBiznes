package models

import "gorm.io/gorm"

type User struct {
	gorm.Model
	Name       string `json:"name"`
	FamilyName string `json:"family_name"`
	Password   string `json:"password"`
	Picture    string `json:"picture"`
	Email      string `json:"email"`
	OauthToken string `json:"token"`
}

type OauthUser struct {
	gorm.Model
	Name       string `json:"name"`
	FamilyName string `json:"family_name"`
	Password   string `json:"password"`
	Picture    string `json:"picture"`
	Email      string `json:"email"`
}
