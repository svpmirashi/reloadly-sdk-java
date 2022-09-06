package software.reloadly.sdk.utilitypayment.client;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static software.reloadly.sdk.core.enums.Environment.LIVE;
import static software.reloadly.sdk.core.enums.Service.AIRTIME;
import static software.reloadly.sdk.core.enums.Service.AIRTIME_SANDBOX;

import java.io.FileReader;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import lombok.Builder;
import okhttp3.HttpUrl;
import software.reloadly.sdk.authentication.client.AuthenticationAPI;
import software.reloadly.sdk.core.enums.Environment;
import software.reloadly.sdk.core.enums.Service;
import software.reloadly.sdk.core.exception.ReloadlyException;
import software.reloadly.sdk.core.internal.constant.HttpHeader;
import software.reloadly.sdk.core.internal.dto.request.CustomizableRequest;
import software.reloadly.sdk.core.internal.dto.request.interfaces.Request;
import software.reloadly.sdk.core.internal.enums.Version;
import software.reloadly.sdk.core.internal.net.ServiceAPI;
import software.reloadly.sdk.core.internal.util.Asserter;
import software.reloadly.sdk.core.net.HttpOptions;
import static software.reloadly.sdk.core.enums.Service.UTILITY_PAYMENT;
import static software.reloadly.sdk.core.enums.Service.UTILITY_PAYMENT_SANDBOX;

public class UtilityPaymentAPI extends ServiceAPI{
    private final HttpUrl baseUrl;
    private final Environment environment;
    
    @Builder
    @SuppressWarnings("unused")
    public UtilityPaymentAPI(String clientId, String clientSecret, String accessToken,
                       Environment environment, boolean enableLogging,
                       List<String> redactHeaders, HttpOptions options, Boolean enableTelemetry) {

        super(clientId, clientSecret, accessToken, enableLogging,
                redactHeaders, options, enableTelemetry, getSDKVersion(), Version.UTILITY_PAYMENT_V1.getValue());
        
        validateCredentials();
        this.environment = environment;
        baseUrl = createBaseUrl(environment);
    }
    
    private static String getSDKVersion() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;
        
        final String UTILITY_PAYMENT_SDK_POM = "./java-sdk-utility-payment/pom.xml";
        final String ROOT_SDK_POM = "./java-sdk-utility-payment/pom.xml";
        
        try {
            FileReader fileReader = new FileReader(UTILITY_PAYMENT_SDK_POM);
            model = reader.read(fileReader);
        } catch (Exception e) {
            try {
                model = reader.read(new FileReader(ROOT_SDK_POM));
            } catch (Exception ex) {
                return "MISSING";
            }

            if (model == null) {
                return "MISSING";
            }
        }

        return isBlank(model.getVersion()) ? "MISSING" : model.getVersion();
    }

	
    /**
     * Retrieve a new API access token to use on new calls.
     * This is useful when the token is about to expire or already has.
     *
     * @param request - The request to refresh the token for
     */
    @Override
    public void refreshAccessToken(Request<?> request) throws ReloadlyException {
        this.accessToken = null;
        CustomizableRequest<?> customizableRequest = (CustomizableRequest<?>) request;
        String newAccessToken = retrieveAccessToken();
        customizableRequest.addHeader(HttpHeader.AUTHORIZATION, "Bearer " + newAccessToken);
    }
    
    private Service getServiceByEnvironment(Environment environment) {
        return (environment != null && environment.equals(LIVE)) ? UTILITY_PAYMENT : UTILITY_PAYMENT_SANDBOX;
    }

    private String retrieveAccessToken() throws ReloadlyException {
        String accessToken = doGetAccessToken(getServiceByEnvironment(environment));
        if (cacheAccessToken) {
            this.accessToken = accessToken;
        }
        return accessToken;
    }

    private String doGetAccessToken(Service service) throws ReloadlyException {
        return isNotBlank(accessToken) ? accessToken : AuthenticationAPI.builder().service(service)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .enableLogging(enableLogging)
                .enableTelemetry(enableTelemetry)
                .build().clientCredentials().getAccessToken().execute().getToken();
    }
    

    private HttpUrl createBaseUrl(Environment environment) {
        Service service = getServiceByEnvironment(environment);
        Asserter.assertNotNull(service, "Service");
        HttpUrl url = HttpUrl.parse(service.getServiceUrl());
        if (url == null) {
            throw new IllegalArgumentException(
                    "The utility-payment base url had an invalid format and couldn't be parsed as a URL."
            );
        }
        return url;
    }
}
