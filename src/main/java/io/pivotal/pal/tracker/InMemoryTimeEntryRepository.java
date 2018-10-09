package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> map = new HashMap<>();

    public TimeEntry create(TimeEntry entry) {
        TimeEntry newEntry = createWithId(map.size() + 1, entry);

        map.put(newEntry.getId(), newEntry);

        return newEntry;
    }

    public TimeEntry find(long id) {
        return map.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> timeEntries = new ArrayList<>();
        timeEntries.addAll(map.values());
        return timeEntries;
    }

    public TimeEntry update(long id, TimeEntry entry) {
        if(map.containsKey(id)){
            TimeEntry updatedEntry = createWithId(id, entry);
            map.put(id, updatedEntry);
            return updatedEntry;
        }
        return null;
    }

    public void delete(long id) {
        map.remove(id);
    }

    private TimeEntry createWithId(long id, TimeEntry templateEntry) {
        return new TimeEntry(
                id,
                templateEntry.getProjectId(),
                templateEntry.getUserId(),
                templateEntry.getDate(),
                templateEntry.getHours());
    }

}
