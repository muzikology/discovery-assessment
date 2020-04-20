package za.co.discovery.assignment.interstella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories(includeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
public class InterstellaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterstellaApplication.class, args);
	}

	@Bean
	public WebMvcConfigurerAdapter adapter() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("swagger-ui.html")
						.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
				registry.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/");
				super.addResourceHandlers(registry);
			}
		};
	}

}
