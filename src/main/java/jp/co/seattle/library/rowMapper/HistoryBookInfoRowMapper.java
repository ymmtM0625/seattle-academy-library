package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.HistoryDetailsInfo;

@Configuration
public class HistoryBookInfoRowMapper implements RowMapper<HistoryDetailsInfo> {

	@Override
	public HistoryDetailsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		HistoryDetailsInfo historyDetailsInfo = new HistoryDetailsInfo();

		historyDetailsInfo.setBookId(rs.getInt("book_id"));
		historyDetailsInfo.setTitle(rs.getString("title"));
		historyDetailsInfo.setRentDay(rs.getDate("rent_day"));
		historyDetailsInfo.setReturnDay(rs.getDate("return_day"));

		return historyDetailsInfo;

	}
}
