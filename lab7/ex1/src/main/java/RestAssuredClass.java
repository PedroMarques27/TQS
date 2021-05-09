import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class RestAssuredClass {
    final static String url="https://jsonplaceholder.typicode.com/todos";


    //This will fetch the response body as is and log it. given and when are optional here
    public static String getResponseBody(int id){
        String data = given().when().get(url+"/"+id).body().asString();
        return data;

    }

    public static String getResponseBody(){
        String data = given().when().get(url).body().asString();
        return data;
    }



    public static int getResponseStatus(){
        int statusCode= given().when().get(url).getStatusCode();
        System.out.println("The response status is "+statusCode);


        return statusCode;
    }

    public static ArrayList<String> getSpecificPartOfResponseBody(){

        ArrayList<String> amounts = when().get(url).then().extract().path("id") ;

       return amounts;

    }


}
