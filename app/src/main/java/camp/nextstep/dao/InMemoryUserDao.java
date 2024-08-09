package camp.nextstep.dao;

import camp.nextstep.domain.User;
import com.interface21.context.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserDao implements UserDao {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1, "gugu", "password", "hkkang@woowahan.com");
        database.put(user.getAccount(), user);
    }

    @Override
    public void save(final User user) {
        database.put(user.getAccount(), user);
    }

    @Override
    public User findById(final long id) {
        return database.values()
            .stream()
            .filter(user -> user.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public User findByAccount(final String account) {
        return database.get(account);
    }
}
