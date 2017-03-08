package fpga.memory;

/**
 * Created by T10 on 3/3/2017.
 *  Represents a class containing details about the registers physical layout in memory.
 */
class RegisterMemoryDetails<T> {

    private T object;

    public RegisterMemoryDetails(T object) {
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }

    public void clearObject() {
        this.object = null;
    }

    public void setObject(Object object) throws ClassCastException {
        this.object = (T) object;
    }
}