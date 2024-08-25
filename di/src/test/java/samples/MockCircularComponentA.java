package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Component;

@Component
public class MockCircularComponentA {

    @Autowired
    public MockCircularComponentA(MockCircularComponentB mockCircularComponentB) {

    }
}
