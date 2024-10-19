package backend.academy.loganalyzer.data;

public record HttpStatusCodeRecord(int value, HttpStatusCode statusCode) {
    @Override
    public String toString() {
        return value + "  " + statusCode.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpStatusCodeRecord that = (HttpStatusCodeRecord) o;
        return value == that.value && statusCode == that.statusCode;
    }
}
