package annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import annotation.sample.MovieCatalog;

@Configuration
public class QualifierConfig {

    private MovieCatalog movieCatalog;

    @Autowired
    //  @Resource(name = "secondMovieCatalog") // @Autowired와 @Qualifier를 사용하지 않고 @Resource를 사용할 수
    // 있습니다.
    public void setMovieCatalog(@Qualifier("secondMovieCatalog") final MovieCatalog movieCatalog) {
        this.movieCatalog = movieCatalog;
    }

    public MovieCatalog getMovieCatalog() {
        return movieCatalog;
    }

    @Bean
    public MovieCatalog firstMovieCatalog() {
        return new MovieCatalog();
    }

    @Bean
    public MovieCatalog secondMovieCatalog() {
        return new MovieCatalog();
    }
}
