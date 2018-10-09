package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TimeEntryRepository {
    public TimeEntry create(TimeEntry any);
    public TimeEntry find(long l);
    public List<TimeEntry> list();
    public TimeEntry update(long id, TimeEntry entry);
    public void delete(long l);
}
