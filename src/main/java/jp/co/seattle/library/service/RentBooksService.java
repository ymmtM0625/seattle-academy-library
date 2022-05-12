package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
    /**
     * 書籍情報を取得する
     * @param bookId 書籍ID
     */
	public void rentBook(Integer bookId) {
		
		String sql = "insert into rentbooks(book_id) select "+ bookId +" where not exists (select * from rentbooks where book_id = "
				+ bookId +")";
		 jdbcTemplate.update(sql);
	}
	
	/**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
	public Integer countRentBook(Integer bookId) {
        
    	String sql = "select count (book_id) from rentbooks where book_id = " + bookId ;
    	
		return jdbcTemplate.queryForObject(sql,Integer.class);

    }

}
