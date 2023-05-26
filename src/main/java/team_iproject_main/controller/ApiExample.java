package team_iproject_main.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
@Log4j2
@Component
public class ApiExample {

    @Value("${mygoogle.apiKey}")
    private String apiKey;

    @Value("${mygoogle.clientid}")
    private String clientid;

    @Value("${mygoogle.clientsecret}")
    private String clientsecret;

    @Value("${mygoogle.clientredirect}")
    private String redirecturl;

    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly",
                    "https://www.googleapis.com/auth/userinfo.profile",
                    "https://www.googleapis.com/auth/userinfo.email");

    private static final String APPLICATION_NAME = "웹 클라이언트 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


    private final String client = "{\n" +
            "  \"installed\": {\n" +
            "    \"client_id\": \""+clientid+"\",\n" +
            "    \"client_secret\": \""+clientsecret+"\",\n" +
            "    \"redirect_uri\": \""+redirecturl+"\"\n" +
            "}\n" +
            "}";

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = ApiExample.class.getResourceAsStream(client);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(4040)
                .build();
        Credential credential =
                new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }


    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public YouTube getYouTubeService(String accessToken) throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // 자료 맞춘거
        InputStream inputStream = new ByteArrayInputStream(client.getBytes());

        GoogleClientSecrets clientSecrets = GoogleClientSecrets
                .load(JSON_FACTORY, new InputStreamReader(inputStream));

        // Flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .build();

        // Use the access token to create the credential object
        TokenResponse response = new TokenResponse();

        response.setAccessToken(accessToken);

        Credential credential = flow.createAndStoreCredential(response, null);

        // Build and return the YouTube object
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setGoogleClientRequestInitializer(new YouTubeRequestInitializer(apiKey))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}


