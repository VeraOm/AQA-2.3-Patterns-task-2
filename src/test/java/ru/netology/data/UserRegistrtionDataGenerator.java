package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserRegistrtionDataGenerator {
    private UserRegistrtionDataGenerator(){}

    public static UserRegistrationData generateData(String status){
        Faker faker = new Faker(new Locale("en"));
        return new UserRegistrationData(faker.name().username(), faker.internet().password(true), status);
    }
}
