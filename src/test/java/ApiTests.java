import com.google.gson.Gson;
import entities.Category;
import entities.Pet;
import entities.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiTests {
    String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    public void test_add_pet(){
        Category cats = new Category(123, "Cats");

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

        //Pet addedPetResponse = addingPetResponse.as(Pet.class);

        System.out.println("Preparing for GET request by ID...");

        BigInteger id = petToAdd.getId();

        Response getPetInfo = given()
                .baseUri(BASE_URL)
                .pathParam("petId", id)
                .when()
                .get("/pet/{petId}");

        System.out.println("Response GET: " + getPetInfo.asString());

        Pet gotPetResponse = getPetInfo.as(Pet.class);
        Assert.assertEquals("Name does not match", petToAdd.getName(), gotPetResponse.getName());
    }


    @Test
    public void userRegister(){
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

      //  User addedUserResponse = addUser.as(User.class);

        Assert.assertEquals("User not created", 200, addUser.getStatusCode());

        System.out.println("Preparing for GET request by username...");
        Response getEmail = given()
                .baseUri(BASE_URL)
                .pathParam("username", "Juli145")
                .when()
                .get("/user/{username}");
        System.out.println("Response GET: " + getEmail.asString());

        User gotUserResponse = getEmail.as(User.class);
        Assert.assertEquals("Email does not match", userToReg.getEmail(), gotUserResponse.getEmail());
    }
}
