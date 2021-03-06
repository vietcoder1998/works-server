package com.worksvn.student_service.configs.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worksvn.common.components.communication.*;
import com.worksvn.common.components.core.JSONProcessor;
import com.worksvn.common.services.excel.import_excel.ImportExcelService;
import com.worksvn.common.services.file_storage.FileStorageService;
import com.worksvn.common.services.firebase.firestore.FirestoreListener;
import com.worksvn.common.services.firebase.firestore.FirestoreService;
import com.worksvn.common.services.internal_service.DistributedDataService;
import com.worksvn.common.services.notification.NotificationListener;
import com.worksvn.common.services.notification.NotificationService;
import com.worksvn.common.services.notification.UserNotificationService;
import com.worksvn.common.services.pdf.PdfExportService;
import com.worksvn.common.services.thymeleaf.HtmlTemplateBindingService;
import com.worksvn.common.utils.core.JacksonObjectMapper;
import com.worksvn.common.utils.jpa.JPAQueryExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class CommonBeansConfig {

    // UTILS =========================================================================================================

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonObjectMapper.getInstance();
    }

    @Bean
    JSONProcessor jsonProcessor(ResourceLoader resourceLoader) {
        return new JSONProcessor(resourceLoader);
    }

    // REST TEMPLATE ===================================================================================================

    @Value("${application.internal-service.communication.logging.enable:false}")
    private boolean enableLogging;

    @Bean
    public RestTemplate restTemplate() {
        return new CustomRestTemplate(enableLogging);
    }

    @Bean
    public RestCommunicator restCommunicator(RestTemplate restTemplate,
                                             RestTemplate serviceDiscoveryRestTemplate) {
        return new RestCommunicator(restTemplate, serviceDiscoveryRestTemplate);
    }

    @LoadBalanced
    @Bean
    public RestTemplate serviceDiscoveryRestTemplate() {
        return new CustomRestTemplate(enableLogging);
    }

    // COMMUNICATION ===================================================================================================

    @Value("${application.internal-service.authentication.oauth2.client-id}")
    private String clientID;
    @Value("${application.internal-service.authentication.oauth2.secret}")
    private String clientSecret;
    @Value("${application.internal-service.communication.error.max-retry:3}")
    private int maxRetryOnError;

    @Bean
    public InMemoryTokenStorage inMemoryTokenStorage() {
        return new InMemoryTokenStorage();
    }

    @Bean
    public ISRestCommunicator isRestCommunicator(RestTemplate restTemplate,
                                                 RestTemplate serviceDiscoveryRestTemplate,
                                                 InMemoryTokenStorage inMemoryTokenStorage) {
        return new ISRestCommunicator(restTemplate, serviceDiscoveryRestTemplate, inMemoryTokenStorage, maxRetryOnError);
    }

    @Bean
    public AcquireNewTokenService acquireNewTokenService(InMemoryTokenStorage inMemoryTokenStorage,
                                                         ISRestCommunicator isRestCommunicator) {

        AcquireNewTokenService acquireNewTokenService =
                new AcquireNewTokenService(clientID, clientSecret, inMemoryTokenStorage);
        acquireNewTokenService.setRestCommunicator(isRestCommunicator);
        isRestCommunicator.setAcquireNewTokenService(acquireNewTokenService);
        return acquireNewTokenService;
    }

    // NOTIFICATION ====================================================================================================

    @Value("${application.firebase.fcm.legacy-server-key}")
    private String firebaseLegacyServerKey;

    @Bean
    public NotificationService notificationService(ApplicationEventPublisher applicationEventPublisher) {
        return new NotificationService(applicationEventPublisher);
    }

    @Bean
    public NotificationListener notificationListener(UserNotificationService userNotificationService,
                                                     RestCommunicator restCommunicator) {
        return new NotificationListener(userNotificationService, restCommunicator, firebaseLegacyServerKey);
    }

    // INTERNAL ========================================================================================================

    @Bean
    public DistributedDataService distributedDataService(ApplicationContext context) {
        return new DistributedDataService(context);
    }

    // JPA =============================================================================================================

    @PersistenceContext(unitName = "primary")
    private EntityManager entityManager;

    @Bean
    public JPAQueryExecutor jpaQueryExecutor() {
        return new JPAQueryExecutor(entityManager);
    }

    // THYMELEAF =======================================================================================================

    @Value("${application.thymeleaf.html-templates.dir:templates/}")
    private String templateDirectory;
    @Value("${application.thymeleaf.html-templates.extension:.html}")
    private String templateExtension;
    @Value("${application.thymeleaf.html-templates.mode:HTML5}")
    private String templateMode;

    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver thymeleafTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver);
        return templateEngine;
    }

    @Bean
    public ClassLoaderTemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(templateDirectory);
        templateResolver.setSuffix(templateExtension);
        templateResolver.setTemplateMode(TemplateMode.valueOf(templateMode));
        return templateResolver;
    }

    @Bean
    public HtmlTemplateBindingService templateBindingService(SpringTemplateEngine templateEngine) {
        return new HtmlTemplateBindingService(templateEngine);
    }

    @Bean
    public PdfExportService pdfExportService(HtmlTemplateBindingService htmlTemplateBindingService,
                                             ResourceLoader resourceLoader) {
        return new PdfExportService(htmlTemplateBindingService, resourceLoader);
    }

    // NOTIFICATION ====================================================================================================

//    @Value("${application.firebase.fcm.legacy-server-key}")
//    private String firebaseLegacyServerKey;
//
//    @Bean
//    public NotificationService notificationService(ApplicationEventPublisher applicationEventPublisher) {
//        return new NotificationService(applicationEventPublisher);
//    }
//
//    @Bean
//    public NotificationListener notificationListener(UserNotificationService userNotificationService,
//                                                     RestCommunicator restCommunicator) {
//        return new NotificationListener(userNotificationService, restCommunicator, firebaseLegacyServerKey);
//    }

    // CHAT ============================================================================================================

    @Bean
    public FirestoreService firestoreService(ApplicationEventPublisher applicationEventPublisher) {
        return new FirestoreService(applicationEventPublisher);
    }

    @Bean
    public FirestoreListener firestoreListener() {
        return new FirestoreListener();
    }

    // FILE STORAGE ====================================================================================================

    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageService();
    }

    // IMPORT/EXPORT EXCEL =============================================================================================

    @Bean
    public ImportExcelService importExcelService() {
        return new ImportExcelService();
    }
}
