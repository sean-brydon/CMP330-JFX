package CMP330.database;

import com.google.common.collect.Lists;
import CMP330.model.Person;

import java.util.List;


public class PersonService {

    public List<Person> getAll() {
        return Lists.newArrayList(
            new Person("User 1"),
            new Person("User 2")
        );
    }
}
