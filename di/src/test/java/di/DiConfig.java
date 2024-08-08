package di;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;

@Configuration
public class DiConfig {
    @Bean
    public DiService diService(final DiRepository diRepository) {
        return new DiService(diRepository);
    }
}
