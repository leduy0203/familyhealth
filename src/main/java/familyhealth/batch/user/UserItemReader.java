package familyhealth.batch.user;

import familyhealth.model.User;
import familyhealth.repository.UserRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserItemReader extends RepositoryItemReader<User> {

    public UserItemReader(UserRepository userRepository) {
        setRepository(userRepository);
        setMethodName("findAll");
        
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        setSort(sorts);
        
        setPageSize(100);
    }
}
