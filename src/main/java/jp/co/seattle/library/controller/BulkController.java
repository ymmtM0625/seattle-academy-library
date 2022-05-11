package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

@Controller //APIの入り口
public class BulkController {

	final static Logger logger = LoggerFactory.getLogger(BulkController.class);

	@Autowired
	private BooksService booksService;

	
	/**
	 * 書籍を一括登録する
	 * @param model
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/bulk", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulk";
    }
	
	/**
	 * 書籍を一括登録する
	 * @param locale
	 * @param uploadFile
	 * @param model
	 * @return　遷移先画面
	 */
	@Transactional
    @RequestMapping(value = "/bulkupload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkupload(Locale locale,
    		@RequestParam("upload_file") MultipartFile uploadFile,
            Model model) {
		 logger.info("Welcome bulkupload.java! The client locale is {}.", locale);
		 
		 
		 List<BookDetailsInfo> bookLists = new ArrayList<BookDetailsInfo>();
	    	List<String> errorLists = new ArrayList<String>();
	    	 
		 
	//読み込みファイルのインスタンス作成
	//ファイル名を指定
    try (BufferedReader br = new BufferedReader(new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))){
        //読み込み行
    	String line;
    	
    	int count = 1;
    	
        //csvファイルを一行ずつ読み込むを行う
        while ((line = br.readLine()) != null) {
          String[] split = line.split(",",-1);
          
          BookDetailsInfo bookInfo = new BookDetailsInfo();
	  		
	  		boolean requiredCheck = split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty() || split[3].isEmpty();
	        boolean publishDateCheck = !(split[3].length() == 8 && split[3].matches("^[0-9]+$"));
	        boolean isbnCheck =!(split[4].length() == 10 || split[4].length() == 13 || split[4].length() == 0);
	        
	        //errorListに追加&bookListに追加
			if(requiredCheck || publishDateCheck || isbnCheck) {
				errorLists.add(count+"行目でエラーが発生しました。");
			}else {
				bookInfo.setTitle(split[0]);
		  		bookInfo.setAuthor(split[1]);
		  		bookInfo.setPublisher(split[2]);
		  		bookInfo.setPublishDate(split[3]);
		  		bookInfo.setIsbn(split[4]);
		  		bookInfo.setExplation(split[5]);
				
				bookLists.add(bookInfo);
			}
	        
			count ++;
	  	
        }
       
        if(bookLists.size()==0) {
        	model.addAttribute("errorMessage", "CSVに書籍情報がありません。");
        	return "bulk";
        }
        	
      } catch (IOException e) {
        model.addAttribute("errorMessage","csvファイルの形式が正しくありません。");
        return "bulk";
        
      }
	
    	
    
    //エラーリストに入っていたらエラー文表示
    if(errorLists.size() !=0){
		 model.addAttribute("errorListsMessage",errorLists);
		 return "bulk";
	//bookListに入っていたら書籍情報を新規登録&書籍一覧画面表示
	}else {
		for(BookDetailsInfo bookInfo:bookLists) {
			booksService.registBook(bookInfo);
		}
		
	    model.addAttribute("bookList", booksService.getBookList());
	    return "home";
	   
	 }
	
		
	
	}
}
