package com.example.m_expenses;

public class Trips {
    public static final int NEW_TRIP = -1;

    private int Id;
    private String TripName;
    private long DateOfTrip;
    private String Destination;
    private String RiskAssessOption;
    private String Description;
    private int EstimatedSpending;
    private String TripTypeOption;

    public Trips(int Id, String TripName, long DateOfTrip, String Destination,
            String RiskAssessOption, String Description, int EstimatedSpending, String TripTypeOption) {

        setTripId(Id);
        setTripName(TripName);
        setDateOfTrip(DateOfTrip);
        setDestination(Destination);
        setRiskAssessOption(RiskAssessOption);
        setDescription(Description);
        setEstimatedSpending(EstimatedSpending);
        setTripTypeOption(TripTypeOption);
    }

    public int getTripId() { return Id; }
    public void setTripId(int Id) { this.Id = Id; }

    public String getTripName() { return TripName; }
    public void setTripName(String TripName) { this.TripName = TripName; }

    public long getDateOfTrip() { return DateOfTrip; }
    public void setDateOfTrip(long DateOfTrip) { this.DateOfTrip = DateOfTrip; }

    public String getDestination(){return  Destination;}
    public void setDestination(String Destination){this.Destination = Destination;}

    public String getRiskAssessOption() {return RiskAssessOption;}
    public void setRiskAssessOption(String RiskAssessOption) {this.RiskAssessOption = RiskAssessOption;}

    public String getDescription() {return Description;}
    public void setDescription(String Description) {this.Description = Description;}

    public int getEstimatedSpending() { return EstimatedSpending; }
    public void setEstimatedSpending(int EstimatedSpending) { this.EstimatedSpending = EstimatedSpending; }

    public String getTripTypeOption() {return TripTypeOption;}
    public void setTripTypeOption(String TripTypeOption) {this.TripTypeOption = TripTypeOption;}

    @Override
    public String toString() { return getTripName();}
}