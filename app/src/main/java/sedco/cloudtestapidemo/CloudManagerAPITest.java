package sedco.cloudtestapidemo;// please find library at https://code.google.com/p/org-json-java/downloads/list and include in your project's build path
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple example how to connect with the Wikitude Cloud Targets API using
 * Java.
 * <p>
 * This example is published under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * @author Wikitude
 */

public class CloudManagerAPITest {

    // The token to use when connecting to the endpoint
    private static final String API_TOKEN = "b7c4379d78126d9fbb672a7e3429cd75";
    // The version of the API we will use
    private static final int API_VERSION = 2;
    // The sample image URLs we are using in this example that will be used as targets inside a target collection
    private static final String[] EXAMPLE_IMAGE_URLS = {
            "http://s3-eu-west-1.amazonaws.com/web-api-hosting/examples_data/surfer.jpeg",
            "http://s3-eu-west-1.amazonaws.com/web-api-hosting/examples_data/biker.jpeg"
    };

    public static void main(String args[]) {
        try {
            // create the object
            final CloudManagerAPI api = new CloudManagerAPI(API_TOKEN, API_VERSION);

            // create an empty targetCollection
            final JSONObject createdTargetCollection = api.createTargetCollection("myFirstTc");

            // targetCollection's id, which was created and will be modified in the following lines
            final String currentTcId = createdTargetCollection.getString("id");

            // create empty target collection
            System.out.println("\nCREATED TARGET-COLLECTION:");
            printTargetCollection(createdTargetCollection);

            // add a single target to the existing targetCollection
            System.out.println("\nCREATE TARGET");
            // create target image JSON with basic information
            final JSONObject target = new JSONObject();
            target.put("name", "target_0");
            target.put("imageUrl", EXAMPLE_IMAGE_URLS[0]);
            final JSONObject createdTarget = api.addTarget(currentTcId, target);
            printTarget(createdTarget);

            // add multiple targets at once to existing targetCollection
            final JSONArray targets = new JSONArray();
            final JSONObject newTarget = new JSONObject();
            newTarget.put("name", "target_1");
            newTarget.put("imageUrl", EXAMPLE_IMAGE_URLS[1]);
            targets.put(newTarget);
            final JSONObject addedTargets = api.addTargets(currentTcId, targets);
            System.out.println("\n\nADDED TARGETS to tc " + currentTcId);
            System.out.println(addedTargets.toString());

            // generate target collection for using its targets in productive Client API
            System.out.println("\n\nGENERATING TARGET COLLECTION " + currentTcId);
            final JSONObject generatedTargetCollection = api.generateTargetCollection(currentTcId);
            System.out.println("CREATED CLOUD-ARCHIVE: " + generatedTargetCollection.getString("id"));

            // clean up
            api.deleteTargetCollection(currentTcId);
            System.out.println("deleted target collection: " + currentTcId);
        } catch (final Exception e) {
            System.out.println("Unexpected exception occurred '" + e.getMessage() + "'");
            e.printStackTrace();
        }
    }


    /**
     * Helper method to print out basic information of given target collection JSONObject
     *
     * @param tc JSONObject of a valid target collection
     * @throws JSONException thrown if mandatory values are not set
     */
    private static void printTargetCollection(final JSONObject tc) throws JSONException {
        System.out.println("************************");
        System.out.println(" - tc id:      " + tc.getString("id"));
        System.out.println(" - tc name:    " + tc.getString("name"));
        System.out.println("************************");
    }

    /**
     * Helper method to print out basic information of given target JSONObject
     *
     * @param target target to print
     * @throws JSONException thrown if mandatory values are not set
     */
    private static void printTarget(final JSONObject target) throws JSONException {
        System.out.println("________________________");
        System.out.println(" - target id:      " + target.getString("id"));
        System.out.println(" - target name:    " + target.getString("name"));
        System.out.println("________________________");
    }

}
