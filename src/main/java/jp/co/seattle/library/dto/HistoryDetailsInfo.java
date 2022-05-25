package jp.co.seattle.library.dto;

import java.sql.Date;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class HistoryDetailsInfo {

	private int bookId;

	private String title;
	
	private Date rentDay;

	private Date returnDay;
	
	public HistoryDetailsInfo() {
		
	}
	
	public HistoryDetailsInfo(int bookId,String title, Date rentDay,Date returnDay) {
		this.bookId = bookId;
		this.title = title;
		this.rentDay = rentDay;
		this.returnDay = returnDay;
	}

}
