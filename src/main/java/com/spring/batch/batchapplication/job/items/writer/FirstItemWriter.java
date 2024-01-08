package com.spring.batch.batchapplication.job.items.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<Long> {

	@Override
	public void write(Chunk<? extends Long> chunk) throws Exception {
		System.out.println("Inside Item Writer with values "+chunk.getItems());
		chunk.getItems().stream()
				.map(val -> {
						if(val==26)
							throw new RuntimeException("Value 26 is not allowed");
						return val;
						})
				.forEach(System.out::println);
	}
}
