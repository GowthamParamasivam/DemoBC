package TransactionProcessor;

public class TransactionData {
    public String getSigner() {
        return signer;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    private String name;
    private String description;
    private String signer;

    public TransactionData(String name, String description, String signer) {
        this.name = name;
        this.description = description;
        this.signer = signer;
    }
}
