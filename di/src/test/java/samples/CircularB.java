package samples;

import com.interface21.context.stereotype.Component;

@Component
public class CircularB {
    private final CircularC circularC;

    public CircularB(CircularC circularC) {
        this.circularC = circularC;
    }
}
