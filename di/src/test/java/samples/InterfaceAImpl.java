package samples;

import com.interface21.context.stereotype.Component;

@Component
public class InterfaceAImpl implements InterfaceA {
    private final InterfaceB interfaceB;

    public InterfaceAImpl(InterfaceB interfaceB) {
        this.interfaceB = interfaceB;
    }
}
