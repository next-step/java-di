package samples;

import com.interface21.context.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SimpleMemoryRepository implements SampleRepository {

    private final List<Object> values;

    public SimpleMemoryRepository() {
        this.values = new ArrayList<>();
    }
}
