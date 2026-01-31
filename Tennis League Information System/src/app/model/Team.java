package app.model;

public class Team {
    
    private int teamNumber;
    private String name;
    private String city;
    private String managerName;

    public Team() {}

    public Team(int teamNumber, String name, String city, String managerName) {
        this.teamNumber = teamNumber;
        this.name = name;
        this.city = city;
        this.managerName = managerName;
    }

    public int getTeamNumber() { return teamNumber; }
    public void setTeamNumber(int teamNumber) { this.teamNumber = teamNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
}
