package com.lc.df.studentapp.studentinfo;

import com.lc.df.studentapp.model.StudentPojo;
import com.lc.df.studentapp.testbase.TestBase;
import com.lc.df.studentapp.utils.TestUtils;

import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;


/**
 * Created By Bhavesh
 */
@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StudentCRUDTest extends TestBase {

    static String firstName = "Rocky" + TestUtils.getRandomValue();
    static String lastName = "Rich" + TestUtils.getRandomValue();
    static String programme = "Automation Testing";
    static String email = "RocRich"+TestUtils.getRandomValue()+"@gmail.com";
    static int studentid;

@Title("This test will create a new student record")
@Test
    public void test001(){
        List<String> courses = new ArrayList<>();
        courses.add("Java");
        courses.add("Selenium WebDriver");
        courses.add("API Testing");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

      SerenityRest.rest().given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then()
                .log().all().statusCode(201);
    }

    @Title("This test will Verify if the student is added to the application")
    @Test
    public void test002(){

    String p1 = "findAll{it.firstName=='";
    String p2 = "'}.get(0)";


        HashMap<String,Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1+firstName+p2);
            assertThat(value,hasValue(firstName));
            studentid=(int)value.get("id");


    }

    @Title("Update the student information and verify the updated information")
    @Test

    public void test003(){
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";
        firstName = firstName+"_Amended";


        List<String> courses = new ArrayList<>();
        courses.add("Java");
        courses.add("API Testing");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest().given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
               .put("/"+ studentid)
                .then().log().all().statusCode(200);

        HashMap<String,Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1+firstName+p2);
        System.out.println(value);
        assertThat(value,hasValue(firstName));


    }
    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test004() {

        SerenityRest.rest()
                .given()
                .when()
                .delete("/" +studentid)
                .then()
                .statusCode(204);
        SerenityRest.rest()
                .given()
                .when()
              .get("/"+studentid)
                .then().statusCode(404);

    }


}
