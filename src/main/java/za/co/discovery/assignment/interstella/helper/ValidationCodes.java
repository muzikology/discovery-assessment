package za.co.discovery.assignment.interstella.helper;


public enum ValidationCodes {
    ROUTE_EXISTS(1, "ROUTE EXISTS"),
    ROUTE_TO_SELF(2, "ROUTE TO SELF"),
    TRAFFIC_EXISTS(3, "TRAFFIC EXISTS"),
    TRAFFIC_TO_SELF(4, "TRAFFIC TO SELF");

    //Enum variables
    final int id;
    final String label;

    /**
     * Creates a new instance of ValidationCodes
     */
    ValidationCodes(final int id, final String label) {
        this.id = id;
        this.label = label;
    }

    public static ValidationCodes fromString(final String str) {
        for (ValidationCodes e : ValidationCodes.values()) {
            if (e.toString().equalsIgnoreCase(str)) {
                return e;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return label;
    }
}

