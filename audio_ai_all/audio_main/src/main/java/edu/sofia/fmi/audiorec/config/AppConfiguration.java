package edu.sofia.fmi.audiorec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "edu.sofia.fmi.audiorec.database.persistence" })
@ComponentScan(basePackages = { "edu.sofia.fmi.audiorec" })
@PropertySource("classpath:oracle.properties")
public abstract class AppConfiguration { //extends AbstractMongoConfiguration {

    @Value("${mongo.database.name}")
    private String mongoDbName;

    @Value("${mongo.database.host}")
    private String mongoDbHost;

    @Value("${mongo.database.port}")
    private int mongoDbPort;

    @Value("classpath:${mongeez.config}")
    private Resource mongeezConfig;

    //@Override
    protected String getDatabaseName() {
        return mongoDbName;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

//    @Override
//    @Bean
//    public Mongo mongo() throws Exception {
//        MongoClient mongo = new MongoClient(new ServerAddress(mongoDbHost,
//                mongoDbPort));
//        return mongo;
//    }

//    @Bean
//    @DependsOn("mongo")
//    public MongeezRunner mongeez() throws Exception {
//        MongeezRunner mongeez = new MongeezRunner();
//        mongeez.setMongo(mongo());
//        mongeez.setExecuteEnabled(true);
//        mongeez.setDbName(mongoDbName);
//        mongeez.setFile(mongeezConfig);
//        return mongeez;
//    }

    // Application beans

//    @Bean
//    @Autowired
//    public QuestionService questionService(QuestionRepository questionRepository) {
//        return new DefaultQuestionService(questionRepository);
//    }
//
//    @Bean
//    @Autowired
//    public AuthorService authorService(AuthorRepository authorRepository) {
//        return new DefaultAuthorService(authorRepository);
//    }

}
