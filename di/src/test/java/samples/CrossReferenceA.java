package samples;

import com.interface21.context.stereotype.Component;

@Component
public class CrossReferenceA {
    private final CrossReferenceB b;

    public CrossReferenceA(CrossReferenceB b) {
        this.b = b;
    }
}
