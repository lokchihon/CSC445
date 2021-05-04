public class EDataPacket {

    private byte[] data;

    public EDataPacket(byte[] data){
        this.data = data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData(){
        return this.data;
    }
}
