package io.interview;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.interview.repository.InMemoryMovieRepository;
import io.interview.repository.MovieRepository;
import io.interview.resources.MovieResource;

public class MovieWorldApplication extends Application<MovieWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new MovieWorldApplication().run(args);
    }

    private final MovieRepository movieRepository = new InMemoryMovieRepository();

    @Override
    public void initialize(Bootstrap<MovieWorldConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<MovieWorldConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MovieWorldConfiguration movieWorldConfiguration) {
                return movieWorldConfiguration.swaggerBundleConfiguration;
            }

        });
    }

    @Override
    public void run(MovieWorldConfiguration configuration,
                    Environment environment) {
        final MovieResource resource = new MovieResource(movieRepository);
        environment.jersey().register(resource);
    }

}