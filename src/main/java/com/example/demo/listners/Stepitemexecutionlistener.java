package com.example.demo.listners;

import lombok.SneakyThrows;

import java.io.IOException;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.service.S3Operation;

@Component("stepItemExecutionListener")
public class Stepitemexecutionlistener implements StepExecutionListener {

    @Autowired
    private S3Operation s3Operation;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }
    @Override
    @SneakyThrows
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(stepExecution.getExitStatus().equals(ExitStatus.COMPLETED))
        {
            Resource resource = new FileSystemResource("user.csv");
            String result;
			try {
				result = s3Operation.uploadFile(resource.getFile());
				System.out.println("CSV File is {} " + result);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
           
        }
        return stepExecution.getExitStatus();
    }
}
   