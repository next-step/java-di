package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class SampleHelperService {

    private final SampleComponent sampleComponent;

    public SampleHelperService(final SampleComponent sampleComponent) {
        this.sampleComponent = sampleComponent;
    }
}
