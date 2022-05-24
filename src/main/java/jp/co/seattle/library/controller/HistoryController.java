package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBooksService;


@Controller
public class HistoryController {
	
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	
	@Autowired
	private RentBooksService rentBookService;
	@Autowired
	private BooksService booksService;
	
	/**
	 * 履歴一覧画面に遷移する
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(Locale locale, 
			
			Model model) {
		// デバッグ用ログ

		logger.info("Welcome history.java! The client locale is {}.", locale);
		 model.addAttribute("historyBookList", rentBookService.historyBookList());
		return "history";
	
	}
	
	@RequestMapping(value = "/historyDetails", method = RequestMethod.GET)
	public String historyDetails(Locale locale, 
			@RequestParam("bookId") Integer bookId,
			Model model) {
		
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
		
	}
}
