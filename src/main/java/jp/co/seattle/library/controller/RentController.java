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
	public String rentBook(Locale locale, 
			@RequestParam("bookId") Integer bookId,
			Model model) {
		// デバッグ用ログ

		logger.info("Welcome rentBook.java! The client locale is {}.", locale);

		
		Integer rentcount = rentBookService.countRentBook(bookId);
		
		HistoryDetailsInfo rentDayInfo = rentBookService.selectRentDay(bookId);
		
		//書き直し
		//bookIdのデータをカウントして同じデータがなければ借りる登録
		if(rentcount == 0) {
			rentBookService.rentBook(bookId);
			
			
		}else {
			//2回目rentdayがnullだったら借りる登録
			if(rentDayInfo.getRentDay() == null) {
				rentBookService.secondrentBook(bookId);
				model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
				return "details";
			}else {
				model.addAttribute("errorlists", "貸出済みです。");
				
			}
			model.addAttribute("errorlists", "貸出済みです。");
		}
		
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}
