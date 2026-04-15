public class Region {
    private int regionId;
    private String regionName;
    private String regionCode;
    
    public Region(int regionId, String regionName, String regionCode) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionCode = regionCode;
    }
    
    public int getId() { return regionId; }
    public String getName() { return regionName; }
    public String getCode() { return regionCode; }
}