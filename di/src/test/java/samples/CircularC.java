package samples;

import com.interface21.context.stereotype.Component;

@Component
public class CircularC {
    private final CircularA circularA;

    public CircularC(CircularA circularA) {
        this.circularA = circularA;
    }
}
