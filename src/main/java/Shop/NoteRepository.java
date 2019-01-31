package Shop;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface NoteRepository extends CrudRepository<Note, Integer> {
    @Query("SELECT COUNT(n) FROM Notes n")
    Integer countNotes();

}