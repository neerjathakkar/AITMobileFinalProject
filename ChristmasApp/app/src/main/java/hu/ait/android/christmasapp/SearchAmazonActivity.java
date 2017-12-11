package hu.ait.android.christmasapp;

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
public class SearchAmazonActivity {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJ6KQ7UPX4TPL4GJQ";

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

    public static void main(String[] args) {
        /*
         * Set up the signed requests helper
         */
        Log.e("search crash", "started SearchAmazonActivity");
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
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
        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Music");
        params.put("Keywords", "girls generation");
        params.put("ResponseGroup", "Small");

        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        ArrayList<String> titles = fetchTitles(requestUrl);
        System.out.println("Girls GGeneration Albums!");
        for(int i = 0; i < titles.size(); i++) {
            System.out.println(titles.get(i));
        }
    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static ArrayList<String> fetchTitles(String requestUrl) {
        ArrayList<String> titles = new ArrayList<String>();
        try {
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
