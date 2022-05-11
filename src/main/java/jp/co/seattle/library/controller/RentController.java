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
public class RentController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentBooksService rentBookService;

	/**
	 * 詳細画面に遷移する
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		// デバッグ用ログ

		logger.info("Welcome rentBook.java! The client locale is {}.", locale);

		Integer rentcountbefore = rentBookService.countRentBook(bookId);
		rentBookService.rentBook(bookId);
		Integer rentcountafter = rentBookService.countRentBook(bookId);

		if (rentcountbefore < rentcountafter) {
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			return "details";

		} else {
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			model.addAttribute("errorlists", "貸出済みです。");
			return "details";

		}
	}
}
