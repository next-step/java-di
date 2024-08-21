package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Component;

@Component
public class MockCircularComponentB {

    @Autowired
    public MockCircularComponentB(MockCircularComponentA mockCircularComponentA) {

    }
}
