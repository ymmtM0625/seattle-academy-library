package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBooksService;

@Controller
public class ReturnBookController {

	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private RentBooksService rentBookService;
    
    
    /**
     * 詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return 詳細画面
     */
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    public String returnBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
    	
    	logger.info("Welcome returnBook.java! The client locale is {}.", locale);
    	
    	Integer rentBook = rentBookService.countRentBook(bookId);
        
    	
        //countの中が０(データがなかったら)エラー文⇦貸出されていない
        if(rentBook==0) {
        	model.addAttribute("errorlists","貸出されていません。");
        }else {
        	rentBookService.returnBook(bookId);
        }
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
	    return "details";
    }

}
