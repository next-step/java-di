package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class SampleService {

    private final SampleHelperService sampleHelperService;

    public SampleService(final SampleHelperService sampleHelperService) {
        this.sampleHelperService = sampleHelperService;
    }
}
