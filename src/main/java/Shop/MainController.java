package Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path="/api")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;


    @GetMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email) {

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }


    @GetMapping(path="/addnote")
    public @ResponseBody String addNewNote (@RequestParam String header
            , @RequestParam String text, @RequestParam Integer user_id) {

        Note n = new Note();
        n.setHeader(header);
        n.setText(text);

        Optional<User> u = Optional.of(new User());
        u = userRepository.findById(user_id);

        u.get().addNote(n);

        userRepository.save(u.get());

        return "Saved " + header +  " to " + u.get().getName();
    }

    @GetMapping(path="/deluser")
    public @ResponseBody String deleteUser (@RequestParam Integer user_id) {
        Optional<User> u = Optional.of(new User());
        u = userRepository.findById(user_id);
        userRepository.delete(u.get());
        return "Deleted";
    }


    @GetMapping(path="/delnote")
    public @ResponseBody String deleteNote (@RequestParam Integer note_id) {

        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            List<Note> notes = user.getNotes();
            for (Note note : notes) {
                if (note.getId() == note_id) {
                    user.delNote(note);
                    userRepository.save(user);
                    return "Deleted";
                }
            }
        }


        return "Not Found";
    }

    @GetMapping(path="/editnote")
    public @ResponseBody String editNote (@RequestParam Integer note_id, @RequestParam String header, @RequestParam String text) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            List<Note> notes = user.getNotes();
            for (Note note : notes) {
                if (note.getId() == note_id) {
                    note.setHeader(header);
                    note.setText(text);
                    userRepository.save(user);
                    return "Edited";
                }
            }
        }
        return "Not found";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping(path="/stats")
    public @ResponseBody Integer getStats() {
        return noteRepository.countNotes();
    }

    @GetMapping(path="/note/{note_id}")
    public @ResponseBody Note getNote(@PathVariable(value = "note_id") Integer note_id) {

        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            List<Note> notes = user.getNotes();
            for (Note note : notes) {
                if (note.getId() == note_id) {
                    return note;
                }
            }
        }
        return null;
    }




}
