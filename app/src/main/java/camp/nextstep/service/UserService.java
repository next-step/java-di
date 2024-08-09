package camp.nextstep.service;

import camp.nextstep.dao.UserDao;
import camp.nextstep.domain.User;
import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findByAccount(final String account) {
        return userDao.findByAccount(account);
    }

    public void save(final User user) {
        userDao.save(user);
    }
}
