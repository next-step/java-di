package samples;

import com.interface21.context.stereotype.Component;

@Component
public class CircularA {
    private final CircularB circularB;

    public CircularA(CircularB circularB) {
        this.circularB = circularB;
    }
}
