package io.interview;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.interview.repository.InMemoryMovieRepository;
import io.interview.repository.MovieRepository;
import io.interview.resources.MovieResource;

public class MovieApplication extends Application<MovieConfiguration> {
    public static void main(String[] args) throws Exception {
        new MovieApplication().run(args);
    }

    private final MovieRepository movieRepository = new InMemoryMovieRepository();

    @Override
    public void initialize(Bootstrap<MovieConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<MovieConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MovieConfiguration movieConfiguration) {
                return movieConfiguration.swaggerBundleConfiguration;
            }

        });
    }

    @Override
    public void run(MovieConfiguration configuration,
                    Environment environment) {
        final MovieResource resource = new MovieResource(movieRepository);
        environment.jersey().register(resource);
    }

}