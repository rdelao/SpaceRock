package fpga.memory;

/**
 * Created by T10 on 3/3/2017.
 *
 * To be thrown by MemoryMap when trying to write register to memory if register
 * flag indicates it is unavailable.
 */
class MemoryMapException extends Exception {
  MemoryMapException(String message) {
    super(message);
  }
}

class MemoryInitializationException extends MemoryMapException {
  MemoryInitializationException(String message) {
    super(message);
  }
}

class NoSuchRegisterFoundException extends MemoryMapException {
  NoSuchRegisterFoundException(String message) {
    super(message);
  }
}

class EmptyRegisterException extends MemoryMapException {
  EmptyRegisterException(String message) {
    super(message);
  }
}

class UnavailbleRegisterException extends MemoryMapException {
  UnavailbleRegisterException(String message) {
    super(message);
  }
}