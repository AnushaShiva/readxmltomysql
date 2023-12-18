package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.demo.listners.Stepitemexecutionlistener;
import com.example.demo.model.User;
import com.example.demo.processors.UserProcessor1;
import com.example.demo.processors.UserProcessor2;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Stepitemexecutionlistener stepitemexecutionlistener;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private DataSource dataSource;

    @Bean
    public MongoItemReader<User> mongoReader1() {
        MongoItemReader<User> reader = new MongoItemReader<User>();
        reader.setTemplate(template);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.ASC);
        }});
        reader.setTargetType(User.class);
        reader.setQuery("{}");
        return reader;
    }
    
    
    @Bean
    public UserProcessor1 processor1() {
        return new UserProcessor1();
    }

    @Bean
    public FlatFileItemWriter<User> csvWriter() {
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<User>();
        writer.setResource(new FileSystemResource("user.csv"));
        writer.setLineAggregator(new DelimitedLineAggregator<User>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
                setNames(new String[]{"user_id", "first_name", "last_name", "age", "email"});
            }});
        }});
        return writer;
    }

  
    
    

    @Bean
    public MongoItemReader<User> mongoReader2() {
        MongoItemReader<User> reader = new MongoItemReader<User>();
        reader.setTemplate(template);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.ASC);
        }});
        reader.setTargetType(User.class);
        reader.setQuery("{}");
        return reader;
    }
    
    
    @Bean
    public UserProcessor2 processor2() {
        return new UserProcessor2();
    }


    @Bean
    public JdbcBatchItemWriter<User> sqlWriter() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
        writer.setDataSource(dataSource);
        writer.setSql("insert into user(user_id, first_name, last_name, age, email) values (:user_id,:first_name,:last_name,:age, :email);");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        ItemPreparedStatementSetter<User> valueSetter =
                new RecordPreparedStatementSetter();
        writer.setItemPreparedStatementSetter(valueSetter);
        return writer;
    }

    final class RecordPreparedStatementSetter implements ItemPreparedStatementSetter<User> {

        @Override
        public void setValues(User user,
                              PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setInt(1, user.getUser_id());
            preparedStatement.setString(2, user.getFirst_name());
            preparedStatement.setString(3, user.getLast_name());
            preparedStatement.setInt(4, user.getAge());
            preparedStatement.setString(5, user.getEmail());
        }
    }

   
 

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<User, User>chunk(100).reader(mongoReader1()).processor(processor1()).writer(csvWriter()).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2").<User, User>chunk(100).reader(mongoReader2()).processor(processor2()).writer(sqlWriter()).listener(stepitemexecutionlistener).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job").flow(step1()).next(step2()).end().build();
    }
}
 