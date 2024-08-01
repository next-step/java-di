package samples;

import com.interface21.context.stereotype.Component;

@Component
public class CrossReferenceB {
    private final CrossReferenceA a;

    public CrossReferenceB(CrossReferenceA a) {
        this.a = a;
    }
}
