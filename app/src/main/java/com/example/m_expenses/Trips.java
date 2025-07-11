package com.example.m_expenses;

public class Trips {
    public static final int NEW_TRIP = -1;

    private int TripId;
    private String TripName;
    private long DateOfTrip;
    private String TripDestination;
    private String RiskAssessOption;
    private String TripDescription;
    private int EstimatedSpending;
    private String TripTypeOption;

    public Trips(int TripId, String TripName, long DateOfTrip, String TripDestination,
            String RiskAssessOption, String TripDescription, int EstimatedSpending, String TripTypeOption) {

        setTripId(TripId);
        setTripName(TripName);
        setDateOfTrip(DateOfTrip);
        setTripDestination(TripDestination);
        setRiskAssessOption(RiskAssessOption);
        setTripDescription(TripDescription);
        setEstimatedSpending(EstimatedSpending);
        setTripTypeOption(TripTypeOption);
    }

    public int getTripId() { return TripId; }
    public void setTripId(int Id) { this.TripId = TripId; }

    public String getTripName() { return TripName; }
    public void setTripName(String TripName) { this.TripName = TripName; }

    public long getDateOfTrip() { return DateOfTrip; }
    public void setDateOfTrip(long DateOfTrip) { this.DateOfTrip = DateOfTrip; }

    public String getTripDestination(){return getTripDestination();}
    public void setTripDestination(String TripDestination){this.TripDestination = TripDestination;}

    public String getRiskAssessOption() {return RiskAssessOption;}
    public void setRiskAssessOption(String RiskAssessOption) {this.RiskAssessOption = RiskAssessOption;}

    public String getTripDescription() {return TripDescription;}
    public void setTripDescription(String TripDescription) {this.TripDescription = TripDescription;}

    public int getEstimatedSpending() { return EstimatedSpending; }
    public void setEstimatedSpending(int EstimatedSpending) { this.EstimatedSpending = EstimatedSpending; }

    public String getTripTypeOption() {return TripTypeOption;}
    public void setTripTypeOption(String TripTypeOption) {this.TripTypeOption = TripTypeOption;}

    @Override
    public String toString() { return getTripName();}
}