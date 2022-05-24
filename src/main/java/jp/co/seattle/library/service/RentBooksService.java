package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.HistoryDetailsInfo;
import jp.co.seattle.library.rowMapper.HistoryBookInfoRowMapper;

@Service
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
	
    /**
     * 借りる書籍を登録する
     * @param bookId 書籍ID
     */
	public void rentBook(Integer bookId) {
		
		String sql = "insert into rentbooks(book_id,rent_day) values("+bookId+", now());";
				
		    
		 jdbcTemplate.update(sql);
	}
	
	/**
     * 借りる書籍のbookIdを数える
     * @param bookId 書籍ID
     * @return 書籍情報
     */
	public Integer countRentBook(Integer bookId) {
        
    	String sql = "select count (book_id) from rentbooks where book_id = " + bookId ;
    	
		return jdbcTemplate.queryForObject(sql,Integer.class);

    }
	/**
	 * 2回目書籍を借りるときの貸出し日を更新
	 * @param bookId
	 */
	public void secondrentBook(Integer bookId) {
			
			String sql = "update rentbooks set rent_day = now(), return_day =  null where book_id=" +bookId+";";
	    	jdbcTemplate.update(sql);
		}
	/**
	 * rentdayを取得
	 * @param bookId
	 */
	public HistoryDetailsInfo selectRentDay(Integer bookId) {
		
		String sql ="select book_id, title, rent_day, return_day from books left outer join rentbooks on books.id = rentbooks.book_id where book_id = " + bookId ;
		try{
			
			HistoryDetailsInfo rentDayInfo = jdbcTemplate.queryForObject(sql, new HistoryBookInfoRowMapper());
			
			return rentDayInfo;
        	
        }catch (Exception e) {
        	return null;
            
        }
		
	}
	
	/**
     * 返却する書籍の返却日を表示
     * @param bookId 書籍ID
     */
	public void returnBook(Integer bookId) {
		
		String sql = "update rentbooks set rent_day = null, return_day =  now() where book_id=" +bookId+";";
    	jdbcTemplate.update(sql);
	}
	
	/**
	 * 貸出書籍のリスト
	 * 
	 * @return
	 */
	public List<HistoryDetailsInfo> historyBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<HistoryDetailsInfo> historyBookList = jdbcTemplate.query(
        		"select book_id, books.title, rent_day, return_day from rentbooks left outer join books on books.id = rentbooks.book_id ",
                new HistoryBookInfoRowMapper());

        return historyBookList;
    }

}
