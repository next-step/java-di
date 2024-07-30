package wrong;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Controller;

@Controller
public class SampleController {

    @Autowired
    private SampleController() {
    }

}
