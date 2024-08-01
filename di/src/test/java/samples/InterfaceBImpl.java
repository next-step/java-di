package samples;

import com.interface21.context.stereotype.Component;

@Component
public class InterfaceBImpl implements InterfaceB {
    private final InterfaceA interfaceA;

    public InterfaceBImpl(InterfaceA interfaceA) {
        this.interfaceA = interfaceA;
    }
}
