package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id,title,author,publisher,publish_date,thumbnail_url from books order by title asc ",
                new BookInfoRowMapper());

        return getedBookList;
    }

    
    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "select * from books left outer join rentbooks on books.id = rentbooks.book_id where books.id = "
        		+bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }
    
    
    /**
     * 最新の書籍情報を取得する
     * 
     * @return 書籍情報
     */
    public BookDetailsInfo newbookInfo() {

        // JSPに渡すデータを設定する
        String sql = "select * from books left outer join rentbooks on books.id = rentbooks.book_id "
        		+"where books.id = (select max(id)from books)";
        		
                

        BookDetailsInfo newbookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return newbookDetailsInfo;
    }
    
    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author, publisher, thumbnail_name,thumbnail_url,publish_date,explation,isbn, reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getExplation() + "','"
                + bookInfo.getIsbn() +"',"
                + "now(),"
                + "now())";

        jdbcTemplate.update(sql);
    }
   
    
    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     */

	public void deleteBook(Integer bookId) {
		
		String sql = "delete from books where id=" +bookId+";";
    	jdbcTemplate.update(sql);
	}
    
	/**
	 * 書籍を更新する
	 * @param id 書籍id
	 * @param bookInfo 書籍情報
	 */
	//task7データ更新
	public void updateBook(BookDetailsInfo bookInfo) {
    	String sql = "UPDATE books SET title ='" + bookInfo.getTitle() + "', author = '" + bookInfo.getAuthor() + "', publisher = '" +  bookInfo.getPublisher() 
    	+ "', publish_date = '" + bookInfo.getPublishDate() + "', thumbnail_url ='" + bookInfo.getThumbnailUrl() + "', isbn ='"  + bookInfo.getIsbn() 
        + "', upd_date = now(), explation ='" + bookInfo.getExplation() + "' Where id = " + bookInfo.getBookId();
    	
    	 jdbcTemplate.update(sql);
    }
	
	/**
	 * 書籍を検索する
	 * @param title
	 */
	 public List<BookInfo> searchBookList(String search) {

	        // TODO 取得したい情報を取得するようにSQLを修正
	        List<BookInfo> searchedBookList = jdbcTemplate.query(
	        		"select * from books where title like '%"+search+"%'",
	                new BookInfoRowMapper());

	        return searchedBookList;
	    }
	
   
}
