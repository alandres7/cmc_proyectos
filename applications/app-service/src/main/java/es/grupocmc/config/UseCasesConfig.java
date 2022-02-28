package es.grupocmc.config;

import es.grupocmc.model.user.gateways.UserRepository;
import es.grupocmc.usecase.user.UserUseCase;
import es.grupocmc.util.service.BCryptService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "es.grupocmc.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public UserUseCase getUserUseCase(UserRepository userRepository, BCryptService bCryptService) {
                return new UserUseCase(userRepository, bCryptService);
        }

}
