import com.google.gson.Gson;
import entities.Category;
import entities.CustomResponse;
import entities.Pet;
import entities.User;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class Practice {

    String BASE_URL = "https://petstore.swagger.io/v2";
    Category cats = new Category(123, "Cats");

    // 1. Добавить питомца с невалидным ID (длиной 20 цифр). Проверить, что возвращается код 500 и сообщение "something bad happened".
    @Test
    public void addPet_invalidID() throws InterruptedException {
        System.out.println("Preparing test data...");
        BigInteger number = new BigInteger("12345678912345678912");
        Pet petToAdd = Pet.builder()
                .id(number)
                .category(cats)
                .name("Sherlock")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("available")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAdd));

        Response addingPetResponse = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAdd)
                .when()
                .post();
        TimeUnit.SECONDS.sleep(5);

        Assert.assertEquals("Status code does not match", 500, addingPetResponse.getStatusCode());
        System.out.println(addingPetResponse.getStatusLine());

        CustomResponse checkMessage = addingPetResponse.as(CustomResponse.class);
        Assert.assertEquals("Message does not match", "something bad happened", checkMessage.getMessage());
    }

    //   2. Создать питомца, получить код 200. Удалить питомца соответствующим запросом, получить код 200.
    //  Проверить что он удалён(отправить GET запрос с его ID и получить ответ о том, что питомец не найден).
    @Test
    public void test2() throws InterruptedException {
        System.out.println("Preparing test data...");
        BigInteger myID = new BigInteger("2");
        Pet petToAdd = Pet.builder()
                .id(myID)
                .category(cats)
                .name("Sherlock")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("available")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAdd));

        Response addingPetResponse = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAdd)
                .when()
                .post();
        //TimeUnit.SECONDS.sleep(5);
        System.out.println("Response: " + addingPetResponse.asString());
        Assert.assertEquals("pet not added",200, addingPetResponse.getStatusCode());

        Response deletePetResponse = given()
                .baseUri(BASE_URL)
                .pathParam("petId", myID)
                .when()
                .delete("/pet/{petId}");
        //TimeUnit.SECONDS.sleep(5);
        System.out.println("Response: " + deletePetResponse.asString());
        Assert.assertEquals("pet is not deleted", 200, deletePetResponse.getStatusCode());

        System.out.println("Checking Pet is deleted...");
        Response getPetInfo = given()
                .baseUri(BASE_URL)
                .pathParam("petId", myID)
                .when()
                .get("/pet/{petId}");
        System.out.println("Response GET PetInfo: " + getPetInfo.asString());

        //TimeUnit.SECONDS.sleep(5);
        getPetInfo.asString();
        CustomResponse gotPetResponse = getPetInfo.as(CustomResponse.class);

        Assert.assertEquals("Pet is found", "Pet not found" ,gotPetResponse.getMessage());
    }

    // 3. Добавить пользователя, получить код 200.
    // Достать добавленного пользователя GET запросом и сделать валидацию JSON SCHEMA полученного респонса.
    @Test
    public void test3(){
        System.out.println("Preparing test data...");
        User userToReg = User.builder()
                .id(new Random().nextInt(3))
                .username("Juli145")
                .firstName("Yulia")
                .lastName("Frankova")
                .email("test@gmail.com")
                .password("12345")
                .phone("0937881359")
                .userStatus(1)
                .build();
        System.out.println("Body to send: " + new Gson().toJson(userToReg));

        Response addUser = given()
                .baseUri(BASE_URL)
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(userToReg)
                .when()
                .post();
        System.out.println("Response: " + addUser.asString());
        Assert.assertEquals("User not added",200, addUser.getStatusCode());

        System.out.println("Preparing for GET request by username...");
        Response getUser = given()
                .baseUri(BASE_URL)
                .pathParam("username", "Juli145")
                .when()
                .get("/user/{username}").then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserSchema.json")).extract().response();
        System.out.println("Response GET: " + getUser.asString());
    }

    //    4. Добавить питомца со статусом sold, получить код 200.
//    Получить в GET запросе всех питомцем со статусом sold, проверить, что там присутствует питомец с нашим ID,
//      проверить его имя на соответствие тому, которое мы указывали при добавлении.

    @Test
    public void test4() throws InterruptedException {
        System.out.println("Preparing test data...");
        BigInteger myID = new BigInteger("2");
        Pet petToAdd = Pet.builder()
                .id(myID)
                .category(cats)
                .name("Sherlock")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("sold")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAdd));

        Response addingPetResponse = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAdd)
                .when()
                .post();
        System.out.println("Response: " + addingPetResponse.asString());
        Assert.assertEquals("pet not added",200, addingPetResponse.getStatusCode());

        TimeUnit.SECONDS.sleep(5);

        Response getPetsWithStatusSold = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/findByStatus/?status=sold");

        System.out.println("Sold pets request done");

        List<Pet> petsSold = Arrays.stream(getPetsWithStatusSold.as(Pet[].class))
                .filter(pet -> pet.getId().equals(petToAdd.getId()))
                .collect(Collectors.toList());

        Assert.assertEquals("Name is not needed", petToAdd.getName(), petsSold.get(0).getName());
    }

    // 5. Проверить количество свободных ID в базе данных, по которым не числятся питомцы, в диапазоне значений ID от 1 до 100 включительно.
    @Test
    public void test5(){
        int idFree = 0;

        for (int i = 1; i <= 100; i++) {
            int gettingPetsResponse = given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/pet/" + i)
                    .then().extract().statusCode();

            if (gettingPetsResponse != 200) {
                idFree++;
            }
        }
        System.out.println(idFree);
    }
}
