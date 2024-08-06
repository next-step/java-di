package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Service;

@Service
public class SampleMethodService {

    private  SampleRepository sampleRepository;

    @Autowired
    public void setSampleRepository(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public SampleRepository getSampleRepository() {
        return sampleRepository;
    }
}
