package amcereijo.com.bqevernote.ocr;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import amcereijo.com.bqevernote.R;

@Singleton
public class OCRServiceAPI {

    private final String PARAM_IMAGE = "image";
    private final String PARAM_LANGUAGE = "language";
    private final String PARAM_APIKEY = "apikey";


    private String apiKey;
    private String serviceUrl;

    private int responseCode;
    private String responseText;

    @Inject
    public OCRServiceAPI(Context context) {
        this.apiKey = context.getString(R.string.ocr_apikey);
        this.serviceUrl = context.getString(R.string.ocr_service_url);
    }

    /*
     * Convert image to text.
     *
     * @param language The image text language.
     * @param filePath The image absolute file path.
     *
     * @return true if everything went okay and false if there is an error with sending and receiving data.
     */
    public boolean convertToText(final String language, final String filePath) {
        try {
            sendImage(language, filePath);

            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    /*
     * Send image to OCR service and read response.
     *
     * @param language The image text language.
     * @param filePath The image absolute file path.
     *
     */
    private void sendImage(final String language, final String filePath) throws ClientProtocolException, IOException {
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost(serviceUrl);

        /*String boundary = "-------------" + System.currentTimeMillis();
        httppost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
*/
        final FileBody image = new FileBody(new File(filePath));

        final MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart(PARAM_IMAGE, image);
        reqEntity.addPart(PARAM_LANGUAGE, new StringBody(language));
        reqEntity.addPart(PARAM_APIKEY, new StringBody(apiKey));
        httppost.setEntity(reqEntity);

        final HttpResponse response = httpclient.execute(httppost);
        final HttpEntity resEntity = response.getEntity();
        final StringBuilder sb = new StringBuilder();
        if (resEntity != null) {
            final InputStream stream = resEntity.getContent();
            byte bytes[] = new byte[4096];
            int numBytes;
            while ((numBytes=stream.read(bytes))!=-1) {
                if (numBytes!=0) {
                    sb.append(new String(bytes, 0, numBytes));
                }
            }
        }

        responseCode = response.getStatusLine().getStatusCode();
        responseText = sb.toString();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

}
