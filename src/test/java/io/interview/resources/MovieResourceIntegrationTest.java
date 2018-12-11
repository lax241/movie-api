package com.interview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.interview.dto.RepositoryDTO;
import com.interview.model.Call;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GitHubInfoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Profile("integration")
@TestPropertySource(locations = "classpath:application-integration.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GitHubInfoIntegrationTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(options().port(9999));

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @BeforeClass
    public static void init() throws Exception{
        //given
        wireMockRule.stubFor(get(urlPathMatching("/repos/dropwizard/dropwizard"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
                        .withBody(FileUtils.readFileToString(new ClassPathResource("json/dropwizard.json").getFile()))));

        wireMockRule.stubFor(get(urlPathMatching("/repos/dropwizard/metrics"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
                        .withBody(FileUtils.readFileToString(new ClassPathResource("json/dropwizard-metrics.json").getFile()))));

        wireMockRule.stubFor(get(urlPathMatching("/repos/dropwizard/notFound"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
                        .withBody(FileUtils.readFileToString(new ClassPathResource("json/notFound.json").getFile()))));

        wireMockRule.start();
    }

    @AfterClass
    public static void teardown() throws Exception {
        wireMockRule.stop();
    }

    @Test
    public void testSuccessfulCall() throws Exception {
        //when
        ResponseEntity<RepositoryDTO> responseEntity =
                restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/dropwizard/dropwizard", RepositoryDTO.class);
        RepositoryDTO result = responseEntity.getBody();

        //then
        assertThat(result.getFullName()).isEqualTo("dropwizard/dropwizard");
        assertThat(result.getDescription()).isEqualTo("A damn simple library for building production-ready RESTful web services.");
        assertThat(result.getCloneUrl()).isEqualTo("https://github.com/dropwizard/dropwizard.git");
        assertThat(result.getStargazersCount()).isEqualTo(6931);
        assertThat(result.getCreatedAt()).isEqualTo("2011-01-19T19:52:29Z");
    }

    @Test
    public void testNotFoundCall() throws Exception {
        //when
        ResponseEntity<String> responseEntityFromNotFoundReposiotry =
                restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/dropwizard/notFound", String.class);
        ResponseEntity<String> responseEntityFromCalls =
                restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/calls", String.class);

        //then
        assertThat(responseEntityFromNotFoundReposiotry.getStatusCodeValue()).isEqualTo(404);
        assertThat(responseEntityFromNotFoundReposiotry.getBody()).isEqualTo(FileUtils.readFileToString(new ClassPathResource("json/notFound.json").getFile()));

        assertThat(responseEntityFromCalls.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntityFromCalls.getBody()).isEqualTo("[]");
    }

    @Test
    public void testCalls() throws Exception {
        //when
        restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/dropwizard/dropwizard", RepositoryDTO.class);
        restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/dropwizard/notFound", RepositoryDTO.class);
        restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/dropwizard/metrics", RepositoryDTO.class);

        ResponseEntity<String> responseEntityFromCalls =
                restTemplate.getForEntity("http://localhost:" + randomServerPort + "/repositories/calls", String.class);
        List<Call> result = new ObjectMapper().readValue(responseEntityFromCalls.getBody(), new TypeReference<List<Call>>() {});

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getCallId()).isEqualTo(1L);
        assertThat(result.get(0).getCallIp()).isEqualTo("127.0.0.1");
        assertThat(result.get(0).getCallRepository().getRepositoryId()).isEqualTo(1);
        assertThat(result.get(0).getCallRepository().getRepositoryName()).isEqualTo("dropwizard");
        assertThat(result.get(0).getCallRepository().getRepositoryOwner().getOwnerName()).isEqualTo("dropwizard");
        assertThat(result.get(0).getCallRepository().getRepositoryOwner().getOwnerId()).isEqualTo(1L);

        assertThat(result.get(1).getCallId()).isEqualTo(2L);
        assertThat(result.get(1).getCallIp()).isEqualTo("127.0.0.1");
        assertThat(result.get(1).getCallRepository().getRepositoryId()).isEqualTo(2L);
        assertThat(result.get(1).getCallRepository().getRepositoryName()).isEqualTo("metrics");
        assertThat(result.get(1).getCallRepository().getRepositoryOwner().getOwnerName()).isEqualTo("dropwizard");
        assertThat(result.get(1).getCallRepository().getRepositoryOwner().getOwnerId()).isEqualTo(1L);

    }


}
