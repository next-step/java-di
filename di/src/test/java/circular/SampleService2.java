package circular;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;
import samples.SampleRepository;

@Service
public class SampleService2 {

    private final SampleService1 SampleService1;

    @Autowired
    public SampleService2(final SampleService1 SampleService1) {
        this.SampleService1 = SampleService1;
    }

}
