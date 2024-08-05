package di;

public class DiService {
    private final DiRepository repository;

    public DiService(final DiRepository repository) {
        this.repository = repository;
    }

    public DiRepository getRepository() {
        return repository;
    }
}
