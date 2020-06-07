package ru.netology.web;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.UserRegistrationData;
import ru.netology.data.UserRegistrtionDataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class UserRegistrationTest {

    private static UserRegistrationData activeUser;
    private static UserRegistrationData blockedUser;

    private static Gson gson = new Gson();
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void registrationUsers() {
        activeUser = UserRegistrtionDataGenerator.generateData("active");
        given()
                .spec(requestSpec)
                .body(gson.toJson(activeUser))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        blockedUser = UserRegistrtionDataGenerator.generateData("blocked");
/*        given()
                .spec(requestSpec)
                .body(gson.toJson(blockedUser))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);*/
    }

    @Test
    void rightSignInTest() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("h2.heading.heading_size_l.heading_theme_alfa-on-white").shouldHave(text("Личный кабинет"));

    }

    @Test
    void wrongLoginTest() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(activeUser.getLogin() + "@");
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    void wrongPasswordTest() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword() + "0");
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    void noPasswordTest() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=password]").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void blockedUserSignInTest() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Пользователь заблокирован"));

    }

}
