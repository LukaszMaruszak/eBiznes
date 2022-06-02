package controllers

import (
	"fmt"
	"myapp/database"
	"myapp/models"
)

func FindUserInDB(email string) bool {
	var user models.User
	database.Database.Find(&user, "email = ?", email)
	if user.Email == "" {
		return false
	}
	return true
}

func GetUserFromDB(email string) models.User {
	var user models.User
	if result := database.Database.Find(&user, "email = ? ", email); result.Error != nil {
		return models.User{}
	}

	return user
}

func AddUser(user models.OauthUser, token string) {
	newUser := new(models.User)

	newUser.Name = user.Name
	newUser.FamilyName = user.FamilyName
	newUser.Picture = user.Picture
	newUser.Email = user.Email
	newUser.OauthToken = token

	fmt.Printf("Add new user \n")

	database.Database.Create(newUser)
}
