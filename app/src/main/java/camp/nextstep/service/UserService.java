package camp.nextstep.service;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;

import camp.nextstep.dao.InMemoryUserDao;
import camp.nextstep.domain.User;

@Service
public class UserService {

    private final InMemoryUserDao userDao;

    @Autowired
    public UserService(InMemoryUserDao userDao) {
        this.userDao = userDao;
    }

    public User findByAccount(final String account) {
        return userDao.findByAccount(account);
    }

    public void save(final User user) {
        userDao.save(user);
    }
}
