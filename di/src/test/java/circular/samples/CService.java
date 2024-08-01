package circular.samples;

import com.interface21.context.stereotype.Service;

@Service
public class CService {

    private final AService aService;

    public CService(final AService aService) {
        this.aService = aService;
    }
}
