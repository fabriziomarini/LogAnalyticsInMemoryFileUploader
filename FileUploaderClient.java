import java.io.ByteArrayInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

public class FileUploaderClient {
	
	public static void main(String[] args) {
		
		ByteArrayInputStream fis = null;
		
		try {
			
			String event = "24.01.2019, 12:48, temp 25, hum 43";
			
			fis = new ByteArrayInputStream(event.getBytes());
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
					
			// Oracle Cloud Log Analytics server back-end URL
			HttpPost httppost = new HttpPost("https://<MyLogAnalyticsOracleCloudUrl>/serviceapi/logan.uploads?uploadName=demo_user_Proxima&logSourceName=<MySourceName>&timezone=Europe/Rome");
			
			// set header for basic Auth.
			httppost.setHeader("Authorization", "Basic ZXJpa3Euc3Npd56aUBvcmFjbGUuY39tOlN0cm9uZ3FsZTgxLg==");
			
			// extra header value needed
			httppost.setHeader("X-USER-IDENTITY-DOMAIN-NAME", "<MyIdName>");
			
			MultipartEntity entity = new MultipartEntity();

			// set the file input stream and file name as arguments
			entity.addPart("data", new InputStreamBody(fis, "proxima_log.log"));
			httppost.setEntity(entity);

			// execute the request
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			System.out.println("[" + statusCode + "] " + responseString);
			
		} catch (ClientProtocolException e) {
			System.err.println("Unable to make connection");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (Exception e) {}
		}
	}
}
