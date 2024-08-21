package circular;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;
import samples.SampleRepository;

@Service
public class SampleService1 {

    private final SampleService2 SampleService2;

    @Autowired
    public SampleService1(final SampleService2 SampleService2) {
        this.SampleService2 = SampleService2;
    }

}
