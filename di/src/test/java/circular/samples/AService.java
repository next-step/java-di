package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class AService {

    private final BService bService;

    public AService(final BService bService) {
        this.bService = bService;
    }
}
