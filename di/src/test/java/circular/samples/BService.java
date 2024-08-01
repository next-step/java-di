package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class BService {

    private final CService cService;

    public BService(final CService cService) {
        this.cService = cService;
    }
}
