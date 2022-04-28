package jp.co.seattle.library.controller;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

@Controller // APIの入り口
public class EditController {

	final static Logger logger = LoggerFactory.getLogger(EditController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	// 詳細画面から編集画面
	
	/**
	 * 
	 * @param locale ロケール情報
	 * @param bookId　bookId
	 * @param model　モデル
	 * @return　遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/edit", method = RequestMethod.POST) 
	public String edit(
			Locale locale, 
			@RequestParam("bookId") int bookId, 
			Model model) {
		logger.info("Welcome edit! The client locale is {}.", locale);

		model.addAttribute("bookEditInfo", booksService.getBookInfo(bookId));// 編集画面で表示される

		return "edit";

	}
	
	/**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param　publich-date 出版日
     * @param isbn 
     * @param explation 説明文
     * @param model モデル
     * @return 遷移先画面
     */
	// 編集画面から詳細画面
	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String updateBook(
			Locale locale, 
			@RequestParam("bookId") int bookId,
			@RequestParam("title") String title, 
			@RequestParam("author") String author,
			@RequestParam("publisher") String publisher, 
			@RequestParam("thumbnail") MultipartFile file,
			@RequestParam("publish_date") String publish_date, 
			@RequestParam("explation") String explation,
			@RequestParam("isbn") String isbn, Model model) {
		logger.info("Welcome updateBooks.java! The client locale is {}.", locale);

		// 格納
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publish_date);
		bookInfo.setExplation(explation);
		bookInfo.setIsbn(isbn);

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				// サムネイル画像をアップロード
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);

			} catch (Exception e) {

			// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookEditInfo", bookInfo);
				return "edit";
			}
		}

		List<String> list = new ArrayList<String>();

		if (bookInfo.getTitle().isEmpty() || bookInfo.getAuthor().isEmpty() || bookInfo.getPublisher().isEmpty()
				|| bookInfo.getPublishDate().isEmpty()) {
			list.add("必須項目を入力してください");
		}

		if (!(bookInfo.getPublishDate().length() == 8 && bookInfo.getPublishDate().matches("^[0-9]+$"))) {
			list.add("出版日は半角数字のYYYYMMDDの形式で入力してください");
		}
		if (!(bookInfo.getIsbn().length() == 10 || bookInfo.getIsbn().length() == 13
				|| bookInfo.getIsbn().length() == 0)) {
			list.add("ISBNの桁数または半角数字が正しくありません");
		}

			// リストに入ってたらエラー文
			// リストに入ってなかったらOK
		if (list.size() == 0) {
			
			booksService.updateBook(bookInfo);


			model.addAttribute("bookDetailsInfo",booksService.getBookInfo(bookInfo.getBookId()));

			// 詳細画面に遷移
			return "details";

		} else {
			model.addAttribute("errorlists", list);//エラー文出す
			model.addAttribute("bookEditInfo", bookInfo);//情報も出す
			//編集画面に遷移
			return "edit";
		}
	
	}

}
