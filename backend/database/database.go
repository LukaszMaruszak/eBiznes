package database

import (
	"fmt"
	"goApp/models"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

var Database *gorm.DB = nil

func InitDataBase() {
	// GORM configuration
	db, err := gorm.Open(sqlite.Open("./database/shop.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	// Migrate the schema
	db.AutoMigrate(&models.Order{})
	db.AutoMigrate(&models.Category{})
	db.AutoMigrate(&models.Payment{})
	db.AutoMigrate(&models.Product{})
	db.AutoMigrate(&models.User{})

	Database = db
	fmt.Printf("Database connected!")
}
