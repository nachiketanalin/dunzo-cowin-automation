import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.diy1.POJOS.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Objects;

public class test01_get {

//    1. Validate the state id of Karnataka is "16".
    @Test
    void test_01(){

        String queryState = "Karnataka";
        int stateId = 16;

        Response res = RestAssured.get("https://cdn-api.co-vin.in/api/v2/admin/location/states");
        StateList sList= res.getBody().as(StateList.class);
        boolean flag = false;
        for(State state : sList.states) {
            if (state.state_id==stateId && state.state_name.equals(queryState)) {
                    flag = true;
                    break;
            }
        }
        Assert.assertEquals(flag,true);
    }


//   2. Validate the district id of Bangalore Urban is "265"
    @Test
    void test_02(){
        String queryDistrict="Bangalore Urban";
        int queryDistrictId=265;

        Response res = RestAssured.get("https://cdn-api.co-vin.in/api/v2/admin/location/districts/16/");
        DistrictList dlist= res.getBody().as(DistrictList.class);

        boolean flag = false;
        for(District dis : dlist.districts) {
            if (dis.district_id==queryDistrictId && dis.district_name.equals(queryDistrict)) {
                flag = true;
                break;
            }
        }
        Assert.assertEquals(flag,true);
    }

//    3. Validate  that all states/UTs have their state_id
    @Test
    void test_03(){

        Response res= RestAssured.get("https://cdn-api.co-vin.in/api/v2/admin/location/states");
        StateList sList = res.getBody().as(StateList.class);
        boolean flag = true;
        for(State state : sList.states){
            if (state.state_id == 0){
                flag = false;
                break;
            }
        }
        Assert.assertEquals(flag, true);
    }

//    4. Validate the price of vaccine does in Hospital "Springleaf Healthcare" [State :   Karnataka, District : Bangalore Urban] is > Rs 300
    @Test
    void test_04(){
        Response res= RestAssured.get("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=265&date=28-01-2023");
        SessionList slist= res.getBody().as(SessionList.class);
        String queryState="Karnataka";
        String queryDistrict="Bangalore Urban";
        String hospital= "Springleaf Healthcare";

        float fee_limit=300;
        boolean flag = false;

        for(Session session : slist.sessions) {
            if(session.state_name.equals(queryState) && session.district_name.equals(queryDistrict) && session.name.equals(hospital)) {
                float price= Float.parseFloat(session.fee);
                if(price>fee_limit) {
                    flag=true;
                    break;
                }
            }
        }
        Assert.assertEquals(flag,true);
    }

//    5. Validate that atleast one Hospital is providing vaccine as Free
    @Test
    void test_05(){
        Response res= RestAssured.get("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=265&date=28-01-2023");
        SessionList slist= res.getBody().as(SessionList.class);

        boolean flag = false;
        for(Session session : slist.sessions) {
            if(Objects.equals(session.fee_type, "Free")) {
                flag=true;
                break;
            }
        }
        Assert.assertEquals(flag,true);
    }
}
