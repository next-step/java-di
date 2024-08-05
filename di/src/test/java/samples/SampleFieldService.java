package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;

@Service
public class SampleFieldService {

    @Autowired
    private SampleRepository sampleRepository;

    public SampleRepository getSampleRepository() {
        return sampleRepository;
    }
}
