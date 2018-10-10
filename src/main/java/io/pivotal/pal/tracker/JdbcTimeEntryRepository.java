package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;


    public JdbcTimeEntryRepository(DataSource source) {

        jdbcTemplate = new JdbcTemplate(source);

    }

    @Override
    public TimeEntry create(TimeEntry any) {
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement("insert into time_entries(project_id, user_id, date, hours) values (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, any.getProjectId());
                preparedStatement.setLong(2, any.getUserId());
                preparedStatement.setDate(3, Date.valueOf(any.getDate()));
                preparedStatement.setInt(4, any.getHours());
                return preparedStatement;
            }
        };

        jdbcTemplate.update(preparedStatementCreator, key);

        return find(key.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        return jdbcTemplate.query("Select id, project_id, user_id, date, hours from time_entries where id = ?", new Object[]{id}, extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("Select id, project_id, user_id, date, hours from time_entries", mapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {


        jdbcTemplate.update(
                "UPDATE time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                entry.getProjectId(),
                entry.getUserId(),
                Date.valueOf(entry.getDate()),
                entry.getHours(),
                id
        );

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);

    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
