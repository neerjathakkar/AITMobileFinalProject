package hu.ait.android.christmasapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    public static final String NAME_LIST = "NAME_LIST";
    public static final String PRICE_LIST = "PRICE_LIST";
    private static final java.lang.String SEARCH_KEYWORD = "SEARCH_KEYWORD";
    private static final java.lang.String SEARCH_CATEGORY = "SEARCH_CATEGORY";

    TextView tvResultOne;
    TextView tvResultTwo;
    TextView tvResultThree;
    TextView tvResultFour;
    TextView tvResultFive;
    TextView tvResultSix;

    TextView tvPriceOne;
    TextView tvPriceTwo;
    TextView tvPriceThree;
    TextView tvPriceFour;
    TextView tvPriceFive;
    TextView tvPriceSix;

    Button btnSelectOne;
    Button btnSelectTwo;
    Button btnSelectThree;
    Button btnSelectFour;
    Button btnSelectFive;
    Button btnSelectSix;

    String searchKeyword;

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
        String searchKeyword = getIntent().getExtras().getString(SEARCH_KEYWORD);
        String searchCategory = getIntent().getExtras().getString(SEARCH_CATEGORY);

        /* The helper can sign requests in two forms - map form and string form */

        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        Log.e("search", "Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", searchCategory);
        params.put("Keywords", searchKeyword);
        params.put("ResponseGroup", "ItemAttributes");

        requestUrl = helper.sign(params);
        Log.e("search", "Signed Request is \"" + requestUrl + "\"");

        final Intent intentResults = new Intent();

        ArrayList<String> titles = fetchTitles(requestUrl);
        ArrayList<String> prices = fetchPrices(requestUrl);

        tvResultOne = (TextView) findViewById(R.id.tvNameOne);
        tvPriceOne = (TextView) findViewById(R.id.tvPriceOne);
        btnSelectOne = (Button) findViewById(R.id.btnSelectOne);

        tvResultTwo = (TextView) findViewById(R.id.tvNameTwo);
        tvPriceTwo = (TextView) findViewById(R.id.tvPriceTwo);
        btnSelectTwo = (Button) findViewById(R.id.btnSelectTwo);

        setTvText(titles, prices);

        Log.e("search", "before btnSelect OnClick");

        btnSelectOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "setting intentResults");
                intentResults.putExtra(NAME_LIST, tvResultOne.getText());
                intentResults.putExtra(PRICE_LIST, tvPriceOne.getText());
                setResult(RESULT_OK, intentResults);
                Log.e("search", "intentResults set");
                finish();
            }
        });

        btnSelectTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "setting intentResults");
                intentResults.putExtra(NAME_LIST, tvResultTwo.getText());
                intentResults.putExtra(PRICE_LIST, tvPriceTwo.getText());
                setResult(RESULT_OK, intentResults);
                Log.e("search", "intentResults set");
                finish();
            }
        });



//        try {
//            intentResults.putExtra(NAME_LIST, titles);
//            intentResults.putExtra(PRICE_LIST, prices);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            return;
//        }
    }

    private void setTvText(ArrayList<String> titles, ArrayList<String> prices) {
        tvResultOne.setText(titles.get(0));
        tvPriceOne.setText(prices.get(0));

        tvResultTwo.setText(titles.get(1));
        tvPriceTwo.setText(prices.get(1));

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

    private static ArrayList<String> fetchPrices(String requestUrl) {
        ArrayList<String> prices = new ArrayList<String>();
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            NodeList priceNode = doc.getElementsByTagName("FormattedPrice");
            for (int i = 0; i < priceNode.getLength(); i++) {
                prices.add(priceNode.item(i).getTextContent());
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        return prices;
    }

}
