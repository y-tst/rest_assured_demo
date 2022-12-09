package api;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {

    private final static String REQRES_URL = "https://reqres.in";

    @Test
    @DisplayName("Avatar matches ser ID test")
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpecOK200());
        List<UserData> users = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        // the same:


        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));


        // 2nd variant of checked below:
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    // Check error for 400 request:
//        @Test
//        public void checkAvatarAndIdTest1() {
//            Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpec400());
//            List<UserData> users1 = given()
//                    .when()
//                    .get("/api/users?page=2")
//                    .then()
//                    .log().all()
//                    .extract().body().jsonPath().getList("data", UserData.class);
//
//            Assertions.assertTrue(users1.stream().allMatch(x -> x.getAvatar().contains(x.getId().toString())));
//    }

    @Test
    @DisplayName("Successful User registration test")
    public void successfulRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpecOK200());
        String email = "eve.holt@reqres.in";
        String password = "pistol";
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Register user = new Register(email, password);
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);

        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    @Test
    @DisplayName("Unsuccessful User registration test")
    public void unSuccessfulRegistrationTest(){
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpec400());
        String email = "sydney@fife";
        String password = "";
        String error = "Missing password";

        Register newUser = new Register(email, password);
        UnSuccessReg unSuccessReg = given()
                .body(newUser)
                .when()
                .post("api/register")
                .then().log().body()
                .extract().as(UnSuccessReg.class);

        Assertions.assertNotNull(unSuccessReg.getError());
        Assertions.assertEquals(unSuccessReg.getError(), error);
    }
}


