package io.zeebe.camel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"})
@Slf4j
public class CucumberBookTest
{

    public static class Steps {

        @ClassRule
        public static final ExternalResource rule = new ExternalResource()
        {
            @Override
            public Statement apply(Statement base, Description description)
            {
                log.info("===== applying {}", description);
                return super.apply(base, description);
            }

            @Override
            protected void before() throws Throwable
            {
                log.info("==== before");
                super.before();
            }

            @Override
            protected void after()
            {
                log.info("==== after");
                super.after();
            }
        };


        @Before
        public void setUp() throws Exception
        {
            log.info("==== before");
        }

        @After
        public void tearDown() throws Exception
        {
            log.info("==== after");
        }


        private static Map<String,String> CONTENT = Collections.singletonMap("the book", "awesome");

        private String book;
        private String content;

        @Given("^a book with name '(.+)'$")
        public void a_book_with_name_the_book(String book) throws Exception {
            this.book = book;
        }

        @When("^I read the book$")
        public void i_read_the_book() throws Exception {
            this.content = CONTENT.get(book);
        }

        @Then("^the content is '(.+)'$")
        public void the_content_is_awesome(String content) throws Exception {
            assertThat(this.content).isEqualTo(content);
        }
    }

}
