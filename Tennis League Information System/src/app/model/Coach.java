package app.model;

public class Coach {
    
    private int coachID;
    private String name;
    private String telephoneNumber;
    private int teamNumber;

    // For view output (joined from Team table)
    private String teamName;

    public Coach() {}

    public int getCoachID() { return coachID; }
    public void setCoachID(int coachID) { this.coachID = coachID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTelephoneNumber() { return telephoneNumber; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }

    public int getTeamNumber() { return teamNumber; }
    public void setTeamNumber(int teamNumber) { this.teamNumber = teamNumber; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}
