import com.google.gson.Gson;
import entities.Category;
import entities.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiTests {
    String BASE_URL = "https://petstore.swagger.io/v2";

    @Test
    public void test_add_pet(){
        Category cats = new Category(123, "Cats");

        System.out.println("I preparing test data...");

        Pet petToAdd = Pet.builder()
                .id(new Random().nextInt(3))
                .category(cats)
                .name("Sherlock")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("available")
                .build();

        System.out.println("Body to send: " + new Gson().toJson(petToAdd));

        long id = petToAdd.getId();

        Response addingPetResponse = given()
                .baseUri(BASE_URL)
                .pathParam("petId", id)
                .when()
                .get("/pet/{petId}");

        System.out.println("Response GET: " + addingPetResponse.asString());

        Pet gotPetResponse = addingPetResponse.as(Pet.class);
        Assert.assertEquals("Wrong name", petToAdd.getName(), gotPetResponse.getName());
    }




//    @Test
//    public void addNewPetToTheStore() {
//        Category catCategory = new Category(131231231, "Cats");
//
//        System.out.println("I preparing test data...");
//        Pet petToAdd = Pet.builder()
//                .id(new Random().nextInt(3))
//                .category(catCategory)
//                .name("Jozzy")
//                .photoUrls(Collections.singletonList("urls"))
//                .tags(null)
//                .status("available")
//                .build();
//        System.out.println("Body to send: " + new Gson().toJson(petToAdd));
//
//        Response addingPetResponse = given()
//                .baseUri(BASE_URL)
//                .basePath("/pet")
//                .contentType(ContentType.JSON)
//                .body(petToAdd)
//                .when()
//                .post();
//
//        System.out.println("Response: " + addingPetResponse.asString());
//
//        Pet addedPetResponse = addingPetResponse.as(Pet.class);
//
//        String expectedStatus = petToAdd.getStatus();
//        String actualStatus = addedPetResponse.getStatus();
//
//        Assert.assertEquals("Wrong status code", 200, addingPetResponse.getStatusCode());
//        Assert.assertEquals("Wrong status", expectedStatus, actualStatus);
//    }

   // @Test
//    public void changeInfoAboutPet() {
//        Category catCategory = new Category(131231231, "Cats");
//
//        System.out.println("I preparing test data...");
//        Pet petToAdd = Pet.builder()
//                .id(new Random().nextInt(3))
//                .category(catCategory)
//                .name("Jozzy")
//                .photoUrls(Collections.singletonList("urls"))
//                .tags(null)
//                .status("available")
//                .build();
//        System.out.println("Body to send: " + new Gson().toJson(petToAdd));
//
//        Response addingPetResponse = given()
//                .baseUri(BASE_URL)
//                .basePath("/pet")
//                .contentType(ContentType.JSON)
//                .body(petToAdd)
//                .when()
//                .post();
//
//        System.out.println("Response: " + addingPetResponse.asString());
//
//        Pet addedPetResponse = addingPetResponse.as(Pet.class);
//        long petId = addedPetResponse.getId();
//
//        petToAdd.setName("Qwerty");
//        Pet changedPet = petToAdd;
//
//        Response changePetIdResponse = given()
//                .baseUri(BASE_URL)
//                .basePath("/pet")
//                .contentType(ContentType.JSON)
//                .body(changedPet)
//                .when()
//                .put();
//
//        Pet changedPetFromResponse = changePetIdResponse.as(Pet.class);
//
//        Assert.assertEquals("Wrong new name of pet", petToAdd.getName(), changedPetFromResponse.getName());
//    }
}
