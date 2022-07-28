package com.kinorlev.appengine.v1.doc


import com.google.common.base.Predicate
import com.kinorlev.appengine.v1.config.EnvironmentWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


//http://localhost:8080/swagger-ui.html#!/
//https://www.javainuse.com/spring/boot_swagger
/**
 * In order doc will work i added in application.properties
 * spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
 * If for some reason its do problem you can remove this line (doc will not work) and also you need to remove the dependency in gradle
 */
@Configuration
@EnableSwagger2
class ApplicationSwaggerConfig {

    @Autowired
    lateinit var environmentWrapper: EnvironmentWrapper


    @Bean
    fun postsApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            //.groupName("public-api")
            .securitySchemes(Collections.singletonList(apiKey()))
            .apiInfo(apiInfo())
            .select()
            .paths(postPaths())

            .build()
    }

    /**
     * Go to android project
     * Search for GetTokenIdUseCaseTest
     * Copy and paste the value to the swagger Authorize btn
     */
    private fun apiKey(): ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }

    private fun postPaths(): Predicate<String> {
        val profile = environmentWrapper.getProfile()
        return when (profile) {
            "dev" -> PathSelectors.any()
            "prod" -> PathSelectors.none()
            else -> throw IllegalArgumentException()
        }
    }


    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder()
            .title("KINOR-LEV API")
            .description("API reference for developers")
            //.termsOfServiceUrl("http://javainuse.com")
            //.contact("javainuse@gmail.com")
            //.license("JavaInUse License")
            //.licenseUrl("javainuse@gmail.com")
            .version("1.0")
            .build()
    }
}

