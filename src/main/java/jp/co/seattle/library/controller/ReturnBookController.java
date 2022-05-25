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

import jp.co.seattle.library.dto.HistoryDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBooksService;

@Controller
public class ReturnBookController {

	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentBooksService rentBooksService;

	/**
	 * 詳細画面に遷移する
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return 詳細画面
	 */
	@Transactional
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String returnBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {

		logger.info("Welcome returnBook.java! The client locale is {}.", locale);

	
		HistoryDetailsInfo rentDayInfo = rentBooksService.selectRentDay(bookId);

		// 書き直し
		//貸出されているかテェック
		// rentdayに値が入っていたらエラー
		// null=返却
		if(rentDayInfo == null) {

			model.addAttribute("errorlists", "貸出されていません。");
			
		} else{
			if (rentDayInfo.getRentDay() == null) {
		
			model.addAttribute("errorlists", "貸出されていません。");
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			return "details";
			
		} else {
			
			rentBooksService.returnBook(bookId);
		}
		
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
	}	
