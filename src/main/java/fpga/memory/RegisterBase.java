package fpga.memory;

import java.io.Serializable;

/**
 * Created by T10 on 3/3/2017.
 * Methods we should be able to expect at minimum from registers.
 */
public interface RegisterBase extends Serializable {
    public void setReady(boolean value);
    public boolean isReady();
    public String getName();
}
