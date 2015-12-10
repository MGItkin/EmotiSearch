package apples.spring.mobile;

import Utilty.Filter;
import Utilty.Image;
import Utilty.URLBuilder;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.simple.parser.ParseException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Created by TUSHAR_SK on 11/13/15.
 */
@Controller
public class MainController {


    @RequestMapping("/")
    public String getHomePage() {
        return "Home";
    }

    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    public String getImagePage() {
        return "UploadImage";
    }

    @RequestMapping(value = "/getQuestions", method = RequestMethod.GET)
    public String getQuestionsPage() {
        return "Questions";
    }



    @RequestMapping(value = "/getProductsFromImageClick",method = RequestMethod.POST)
    public String getProductsFromImageClick(@ModelAttribute Image image, ModelMap modelMap) throws JSONException, ParseException {

        System.out.println(image.getImage());
        String url = "https://api.projectoxford.ai/emotion/v1.0/recognize";
        String body = image.getImage();

        String response =  URLBuilder.postRequest(url, body);

        JSONTokener tokener = new JSONTokener(response);
        JSONArray finalResult = new JSONArray(tokener);

        JSONObject j = finalResult.getJSONObject(0);

        JSONObject j1 = (JSONObject) j.get("scores");


        double maxDouble = findMax((Double)j1.get("anger"),(Double)j1.get("contempt"),(Double)j1.get("disgust"),
                (Double)j1.get("fear"),(Double)j1.get("happiness"),(Double)j1.get("neutral"), (Double)j1.get("sadness"),(Double)j1.get("surprise"));

        System.out.println(maxDouble);

        Double[] array = {(Double)j1.get("anger"),(Double)j1.get("contempt"),(Double)j1.get("disgust"),
                (Double)j1.get("fear"),(Double)j1.get("happiness"),(Double)j1.get("neutral"), (Double)j1.get("sadness"),(Double)j1.get("surprise")};

        String[] arrayEmotion = {"anger","contempt","disgust","fear","happiness","neutral","sadness","surprise"};

        String emotion = "";
        for(int i=0;i<array.length;i++){
            if(array[i] == maxDouble){
                emotion = arrayEmotion[i];
            }
        }

        System.out.println(emotion);

        String  s = emotion;

        List<String> list = new ArrayList<>();
        if(s.equals("anger")){

            list.add("Fighting");

        }
        else if(s.equals("contempt")){

            list.add("Puzzle%20&%20Cards");
        }
        else if(s.equals("disgust")){

            list.add("Shooter");
        }
        else if(s.equals("fear")){

            list.add("Role-Playing");
        }
        else if(s.equals("happiness")){

            list.add("Sports");
        }
        else if(s.equals("neutral")){
            list.add("Casual");
        }
        else if(s.equals("sadness")){
            list.add("Action");
        }
        else if(s.equals("surprise")){
            list.add("3D");
        }

        String cat = list.get(0);

        String URL = "https://api.apim.ibmcloud.com/gamestop/prod/gamestop/Products?Search=&sortDirection=1&sortBy=price&offset=0&numRecords=4&filter=Condition:New,Platform:PlayStation%204" + ",Category:" + cat + "&";
        String response1 = URLBuilder.getResponse(URL);

        JSONTokener tokener1 = new JSONTokener(response1);
        JSONArray finalResult1 = new JSONArray(tokener1);

        Map<Integer, List> m = new HashMap<>();
        List<HashMap> list1 = new ArrayList<>();

        for (int i = 0; i < finalResult1.length(); i++) {

            JSONObject j3 = finalResult1.getJSONObject(i);

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("id", (String) j3.get("ProductID"));
            hashMap.put("name", (String) j3.get("DisplayName"));
            hashMap.put("price", String.valueOf(j3.get("Price")));
            hashMap.put("rating", String.valueOf(j3.get("AverageOverallRating")));
            hashMap.put("imageUrl", (String) j3.get("BoxArtUrl"));
            JSONArray list2 = (JSONArray) j3.get("Platforms");
            hashMap.put("platform", (String) list2.get(0));
            hashMap.put("productUrl", (String) j3.get("ProductPageUrl"));

            list1.add((HashMap) hashMap);

            m.put(i, list1);

            //System.out.println(m.toString());
        }
        modelMap.addAttribute("map", m);

        return "Results";
    }

    @RequestMapping(value = "/getProductsFromImage",method = RequestMethod.POST)
    public String getProductsFromImage(@ModelAttribute Filter filter, ModelMap modelMap) throws JSONException, ParseException {


        String platform ;

        List<String> l = getCategories();
        String category = l.get(0);

        platform = filter.getPlatform();


        if (filter.getPlatform().equals("PlayStation 4")){
            platform = "PlayStation%204";
        }
        else if (filter.getPlatform().equals("Nintendo Wii U")){
            platform = "Nintendo%20Wii%20U";
        }
        else if (filter.getPlatform().equals("xbox One")){
            platform = "Xbox%20One";
        }
        else if (filter.getPlatform().equals("xbox 360")){
            platform = "Xbox%20360";
        }

        category = filter.getCategory();

        if (filter.getCategory().equals("Puzzles and Cards")){
            category = "Puzzles%20and%20Cards";
        }
        else if (filter.getCategory().equals("Role Playing")){
            category = "Role%20Playing";
        }

        String URL = "https://api.apim.ibmcloud.com/gamestop/prod/gamestop/Products?Search=&sortDirection=1&sortBy=price&offset=0&numRecords=4&filter=Condition:New,Platform:" + platform + ",Category:" + category + "&";
            String response = URLBuilder.getResponse(URL);

            JSONTokener tokener = new JSONTokener(response);
            JSONArray finalResult = new JSONArray(tokener);

            Map<Integer, List> m = new HashMap<>();
            List<HashMap> list1 = new ArrayList<>();

            for (int i = 0; i < finalResult.length(); i++) {

                JSONObject j = finalResult.getJSONObject(i);

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("id", (String) j.get("ProductID"));
                hashMap.put("name", (String) j.get("DisplayName"));
                hashMap.put("price", String.valueOf(j.get("Price")));
                hashMap.put("rating", String.valueOf(j.get("AverageOverallRating")));
                hashMap.put("imageUrl", (String) j.get("BoxArtUrl"));
                JSONArray list2 = (JSONArray) j.get("Platforms");
                hashMap.put("platform", (String) list2.get(0));
                hashMap.put("productUrl", (String) j.get("ProductPageUrl"));

                list1.add((HashMap) hashMap);

                m.put(i, list1);

                //System.out.println(m.toString());
            }
        modelMap.addAttribute("map", m);

        return "Results";
    }


    @RequestMapping(value = "/getImageCriteria", produces = "application/json")
    @ResponseBody
    public String getCategory() throws JSONException {

        String url = "https://api.projectoxford.ai/emotion/v1.0/recognize";
        String body = "https://s3-us-west-1.amazonaws.com/elasticbeanstalk-us-west-1-414939076196/IMAG1179_1.jpg";

        String response =  URLBuilder.postRequest(url, body);

        JSONTokener tokener = new JSONTokener(response);
        JSONArray finalResult = new JSONArray(tokener);

        JSONObject j = finalResult.getJSONObject(0);

        JSONObject j1 = (JSONObject) j.get("scores");


        double maxDouble = findMax((Double)j1.get("anger"),(Double)j1.get("contempt"),(Double)j1.get("disgust"),
                (Double)j1.get("fear"),(Double)j1.get("happiness"),(Double)j1.get("neutral"), (Double)j1.get("sadness"),(Double)j1.get("surprise"));

        System.out.println(maxDouble);

        Double[] array = {(Double)j1.get("anger"),(Double)j1.get("contempt"),(Double)j1.get("disgust"),
                (Double)j1.get("fear"),(Double)j1.get("happiness"),(Double)j1.get("neutral"), (Double)j1.get("sadness"),(Double)j1.get("surprise")};

        String[] arrayEmotion = {"anger","contempt","disgust","fear","happiness","neutral","sadness","surprise"};

        String emotion = "";
        for(int i=0;i<array.length;i++){
            if(array[i] == maxDouble){
                emotion = arrayEmotion[i];
            }
        }

        System.out.println(emotion);

        return emotion;
    }

    public List<String> getCategories() throws JSONException {
        String s = getCategory();
        List<String> list = new ArrayList<>();
        if(s.equals("anger")){

            list.add("Fighting");

        }
        else if(s.equals("contempt")){

            list.add("Puzzle%20&%20Cards");
        }
        else if(s.equals("disgust")){

            list.add("Shooter");
        }
        else if(s.equals("fear")){

            list.add("Role-Playing");
        }
        else if(s.equals("happiness")){

            list.add("Sports");
        }
        else if(s.equals("neutral")){
            list.add("Casual");
        }
        else if(s.equals("sadness")){
            list.add("Action");
        }
        else if(s.equals("surprise")){
          list.add("3D");
        }

        return list;

    }

    private double findMax(double... vals) {
        double max = Double.NEGATIVE_INFINITY;
        for (double d : vals) {

            if (d > max){
                max = d;
            }
        }
        return max;
    }

}
