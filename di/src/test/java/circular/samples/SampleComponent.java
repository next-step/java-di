package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class SampleComponent {

    private final SampleService sampleService;

    public SampleComponent(final SampleService sampleService) {
        this.sampleService = sampleService;
    }
}
