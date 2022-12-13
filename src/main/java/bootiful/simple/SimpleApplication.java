package bootiful.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;

@SpringBootApplication
public class SimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class, args);
	}

	@Bean
	CoreV1Api coreV1Api(ApiClient apiClient) {
    	return new CoreV1Api(apiClient);
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener(CoreV1Api coreV1Api) {
		return event -> {
			try {
				coreV1Api
				.listNamespacedService("default", "true", false, "", "", "", 10, "", "", 100, false)
				.getItems()
				.forEach( svc -> System.out.println( "Service: " + svc.toString()));
			}
			catch (ApiException e) {
				throw new RuntimeException(e);
			}
		};
	}
}
