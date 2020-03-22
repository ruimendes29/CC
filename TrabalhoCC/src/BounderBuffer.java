public class BounderBuffer {
        private static final int SIZE = 10;

        private Object[] buffer;
        private int inCount;
        private int outCount;

        /**
         Creates an empty buffer.
         */
        public BounderBuffer()
        {
            this.buffer = new Object[BounderBuffer.SIZE];
            this.inCount = 0;
            this.outCount = 0;
        }

        /**
         Puts the given element in this buffer.

         @param element the element to be added.
         */
        public synchronized void put(Object element) throws Exception
        {
            while (this.inCount - this.outCount >= BounderBuffer.SIZE)
            {
                this.wait();
            }
            this.buffer[this.inCount % BounderBuffer.SIZE] = element;
            this.inCount++;
            this.notifyAll();
        }

        /**
         Removes an element from the buffer and returns it.

         @return an element of the buffer.
         */
        public synchronized Object get() throws Exception
        {
            while (this.inCount - this.outCount <=0)
            {
                this.wait();
            }
            Object temp = this.buffer[this.outCount % BounderBuffer.SIZE];
            this.outCount++;
            this.notifyAll();
            return temp;
    }
}
