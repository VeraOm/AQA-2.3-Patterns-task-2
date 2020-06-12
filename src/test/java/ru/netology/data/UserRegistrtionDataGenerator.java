package ru.netology.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserRegistrtionDataGenerator {

    private final static String activeStatus = "active";
    private final static String blockedStatus = "blocked";

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Gson gson = new Gson();
    private static Faker faker = new Faker(new Locale("en"));


    private UserRegistrtionDataGenerator() {
    }

    private static void registrationUsers(UserRegistrationData userData) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(userData))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static UserRegistrationData generateValidActive() {
        UserRegistrationData userData = new UserRegistrationData(faker.name().username(),
                faker.internet().password(true), activeStatus);
        registrationUsers(userData);
        return userData;
    }

    public static UserRegistrationData generateValidBlocked() {
        UserRegistrationData userData = new UserRegistrationData(faker.name().username(),
                faker.internet().password(true), blockedStatus);
        registrationUsers(userData);
        return userData;
    }

    public static UserRegistrationData generateInvalidLogin() {
        String password = faker.internet().password(true);
        UserRegistrationData userDataRegistration = new UserRegistrationData(faker.name().username(),
                password, activeStatus);
        registrationUsers(userDataRegistration);
        return new UserRegistrationData(faker.name().username(),
                password, activeStatus);
    }

    public static UserRegistrationData generateInvalidPassword() {
        String login = faker.name().username();
        UserRegistrationData userDataRegistration = new UserRegistrationData(login,
                faker.internet().password(true), activeStatus);
        registrationUsers(userDataRegistration);
        return new UserRegistrationData(login,
                faker.internet().password(true), activeStatus);
    }

}
