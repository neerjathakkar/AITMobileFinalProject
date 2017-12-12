package hu.ait.android.christmasapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class SearchAmazonActivity extends AppCompatActivity {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJ6KQ7UPX4TPL4GJQ";

    private static final String AWS_ASSOCIATE_TAG = "aitchristmasl-20";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "RZN6qliCE2ya7vq0+1kCg0oj7OaMLIF87NPjWhn7";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     *
     *      US: ecs.amazonaws.com
     *      CA: ecs.amazonaws.ca
     *      UK: ecs.amazonaws.co.uk
     *      DE: ecs.amazonaws.de
     *      FR: ecs.amazonaws.fr
     *      JP: ecs.amazonaws.jp
     *
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    private static final String ITEM_ID = "B001666E4I";

    public static final String NODE_LIST = "NODE_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Set up the signed requests helper
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_amazon);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.e("search crash", "started SearchAmazonActivity");
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_ASSOCIATE_TAG, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String requestUrl = null;
        String title = null;

        /* The helper can sign requests in two forms - map form and string form */

        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        Log.e("search", "Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Food");
        params.put("Keywords", "popcorn");
        params.put("ResponseGroup", "Small");

        requestUrl = helper.sign(params);
        Log.e("search", "Signed Request is \"" + requestUrl + "\"");

        Intent intentResults = new Intent();

        ArrayList<String> titles = fetchTitles(requestUrl);
        Log.e("search", "Girls GGeneration Albums!");
        for(int i = 0; i < titles.size(); i++) {
            Log.e("search", titles.get(i));
        }
        try {
            intentResults.putExtra(NODE_LIST, titles.get(1));
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static ArrayList<String> fetchTitles(String requestUrl) {
        ArrayList<String> titles = new ArrayList<String>();
        try {
            Log.e("search crash", "in titles try");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            NodeList titleNode = doc.getElementsByTagName("Title");

            for(int i = 0; i < titleNode.getLength(); i++) {
                titles.add(titleNode.item(i).getTextContent());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return titles;
    }

}
