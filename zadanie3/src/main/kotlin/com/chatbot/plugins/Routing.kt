package com.chatbot.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

val hello = "Witaj w Buon Appetito - restauracja włoska!\nMożesz u nas zamówić dania z następujących kategorii.\n"

fun returnCategories(): String {
    val categories = mutableMapOf<Int, String>()
    categories.put(1, "Pizza \uD83C\uDF55")
    categories.put(2, "Pasta \uD83C\uDF5D")
    categories.put(3, "Dania Główne \uD83C\uDF7D️")
    categories.put(4, "Zupy \uD83C\uDF72")

    var response = ""

    categories.forEach { category ->
        response += "${category.key}: ${category.value}\n"
    }

    return hello + response
}

fun returnCategoryItems(categoryId: String): String {
    val pizza = mutableMapOf<Int, String>()
    pizza.put(1, "Margerita 10zł")
    pizza.put(2, "Hawajska 15zł")
    pizza.put(3, "Salami 20zł")
    pizza.put(4, "Serowa 18zł")

    val pasta = mutableMapOf<Int, String>()
    pasta.put(1, "Spaghetti Carbonara 15zł")
    pasta.put(2, "Spaghetti Bolognese 18zł")

    val mainDish = mutableMapOf<Int, String>()
    mainDish.put(1, "Pieczony Łosoś 25zł")
    mainDish.put(2, "Kurczak z pesto 20zł")
    mainDish.put(3, "Grilowana ośmiornica 30zł")

    val soup = mutableMapOf<Int, String>()
    soup.put(1, "Pomidorowa 8zł")
    soup.put(2, "Z owocami morza 10zł")

    var response = ""

    when (categoryId.toInt()) {
        1 -> {
            pizza.forEach { category ->
                response += "${category.key}: ${category.value}\n"
            }

            return response
        }
        2 -> {
            pasta.forEach { category ->
                response += "${category.key}: ${category.value}\n"
            }
            return response
        }
        3 -> {
            mainDish.forEach { category ->
                response += "${category.key}: ${category.value}\n"
            }
            return response
        }
        4 -> {
            soup.forEach { category ->
                response += "${category.key}: ${category.value}\n"
            }
            return response
        }
        else -> {
            return "Nieprawidłowa kategoria"
        }
    }
}

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }

    routing {
        post("/hello") {
            call.respondText("Witaj w jestem botem!")
        }

        post("/category") {
            call.respondText(returnCategories())
        }

        post("/category-items") {
            val formParameters = call.receiveParameters()
            log.info(formParameters.toString())
            var id = formParameters["text"]

            if (id == null) {
                id = "99"
            }
            call.respondText(returnCategoryItems(id))
        }
    }
}