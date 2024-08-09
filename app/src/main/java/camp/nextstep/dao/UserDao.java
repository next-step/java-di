package camp.nextstep.dao;

import camp.nextstep.domain.User;

public interface UserDao {
    void save(User user);

    User findById(long id);

    User findByAccount(String account);
}
