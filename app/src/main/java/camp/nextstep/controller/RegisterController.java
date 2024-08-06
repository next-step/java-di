package camp.nextstep.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import camp.nextstep.dao.InMemoryUserDao;
import camp.nextstep.dto.UserDto;

@Controller
public class RegisterController {

    private final InMemoryUserDao userDao;

    public RegisterController() {
        this.userDao = new InMemoryUserDao();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(final UserDto userDto) {
        final var user = userDto.toEntity();
        userDao.save(user);
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView view(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
