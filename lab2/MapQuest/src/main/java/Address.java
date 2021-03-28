public class Address {
    String road;
    String city;
    String state;
    String zip;
    String houseNumber;


    public Address(String _road, String _city, String _state, String _zip, String _houseNumber){
        this.road = _road;
        this.city = _city;
        this.state = _state;
        this.zip = _zip;
        this.houseNumber = _houseNumber;
    }
    public String toString(){
        return String.format(" %s %s, %s ( %s ) - %s", road,houseNumber, city, state, zip);
    }


}
