package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    public static final String TIME_ENTRIES_URI = "/time-entries";
    public static final String TIME_ENTRY_PATH_URI = TIME_ENTRIES_URI + "/{id}";

    @Autowired
    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping(value = TIME_ENTRIES_URI)
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry entry = timeEntryRepository.create(timeEntryToCreate);

        return new ResponseEntity(entry, HttpStatus.CREATED);
    }

   @GetMapping(value = TIME_ENTRY_PATH_URI)
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry = timeEntryRepository.find(id);

        HttpStatus status = HttpStatus.OK;
        if (entry == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity(entry, status);
    }

    @GetMapping(value = TIME_ENTRIES_URI)
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntries = timeEntryRepository.list();
        return new ResponseEntity(timeEntries, HttpStatus.OK);
    }

    @PutMapping(value = TIME_ENTRY_PATH_URI)
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
        HttpStatus status = HttpStatus.OK;
        TimeEntry updatedEntry = timeEntryRepository.update(id, entry);
        if(updatedEntry == null){
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity(updatedEntry, status);
    }

    @DeleteMapping(value = TIME_ENTRY_PATH_URI)
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        timeEntryRepository.delete(id);
        return new ResponseEntity<>(status);
    }
}
