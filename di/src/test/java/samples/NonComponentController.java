package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Controller;

public class NonComponentController {

  private final SampleService sampleService;

  @Autowired
  public NonComponentController(final SampleService sampleService) {
    this.sampleService = sampleService;
  }

  public SampleService getSampleService() {
    return sampleService;
  }
}
