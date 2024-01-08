package com.spring.batch.batchapplication.listener;

import com.spring.batch.batchapplication.model.StudentCsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Slf4j
@StepScope
@Component
public class SkipListener {

	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@OnSkipInRead
	public void skipInRead(Throwable th ) {
		if(th instanceof FlatFileParseException) {
			var item = ((FlatFileParseException) th).getInput();
			System.out.println("Skipped value in reader "+item);
		}
	}

	@OnSkipInProcess
	public void skipInProcess(Integer item, Throwable th) {
		System.out.println("Skipped value in processor "+item);
	}
	
	@OnSkipInWrite
	public void skipInWriter(Long item, Throwable th) {
		System.out.println("Skipped value in writer "+item);
	}

}
