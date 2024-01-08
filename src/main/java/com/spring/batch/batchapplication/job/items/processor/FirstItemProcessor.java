package com.spring.batch.batchapplication.job.items.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {

	@Override
	public Long process(Integer item) throws Exception {
		System.out.println("Inside Item Processor for value "+ item);
//		if(item==6)
//			throw new RuntimeException("Not expected value 6");

		return Long.valueOf(item + 20);
	}

}
