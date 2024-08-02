package ill_dependent;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Component;

@Component
public class IllDependentBean {
    private final NotBean notBean;

    @Autowired
    public IllDependentBean(final NotBean notBean) {
        this.notBean = notBean;
    }
}
